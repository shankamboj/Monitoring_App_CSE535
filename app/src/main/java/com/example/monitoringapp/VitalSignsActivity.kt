package com.example.monitoringapp

import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.LinearLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VitalSignsActivity : ComponentActivity() {

    private lateinit var btnSelectVideo: Button
    private lateinit var btnLoadCSV: Button
    private lateinit var btnNextToSymptoms: Button
    private lateinit var tvHeartRateResult: TextView
    private lateinit var tvRespiratoryRateResult: TextView
    private lateinit var progressBar: ProgressBar

    private var heartRate: Int? = null
    private var respiratoryRate: Int? = null

    companion object {
        private const val PICK_VIDEO_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the UI programmatically instead of using XML
        createUI()

        setupClickListeners()
    }

    private fun createUI() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 50, 50, 50)
        }

        // Title
        val title = TextView(this).apply {
            text = "Vital Signs Monitoring"
            textSize = 24f
            setPadding(0, 0, 0, 50)
        }
        layout.addView(title)

        // Heart Rate Section
        val heartRateTitle = TextView(this).apply {
            text = "Heart Rate Measurement"
            textSize = 18f
            setPadding(0, 0, 0, 20)
        }
        layout.addView(heartRateTitle)

        btnSelectVideo = Button(this).apply {
            text = "Select Heart Rate Video"
            setPadding(0, 30, 0, 30)
        }
        layout.addView(btnSelectVideo)

        tvHeartRateResult = TextView(this).apply {
            text = "Heart Rate: Not measured"
            textSize = 16f
            setPadding(30, 30, 30, 30)
            setBackgroundColor(0xFFE8F5E8.toInt())
            setPadding(0, 20, 0, 50)
        }
        layout.addView(tvHeartRateResult)

        // Respiratory Rate Section
        val respRateTitle = TextView(this).apply {
            text = "Respiratory Rate Measurement"
            textSize = 18f
            setPadding(0, 0, 0, 20)
        }
        layout.addView(respRateTitle)

        btnLoadCSV = Button(this).apply {
            text = "Load Respiratory Rate CSV Data"
            setPadding(0, 30, 0, 30)
        }
        layout.addView(btnLoadCSV)

        tvRespiratoryRateResult = TextView(this).apply {
            text = "Respiratory Rate: Not measured"
            textSize = 16f
            setPadding(30, 30, 30, 30)
            setBackgroundColor(0xFFFFF3E0.toInt())
            setPadding(0, 20, 0, 50)
        }
        layout.addView(tvRespiratoryRateResult)

        // Next Button
        btnNextToSymptoms = Button(this).apply {
            text = "Next: Symptoms Logging"
            setPadding(0, 30, 0, 30)
            isEnabled = false
        }
        layout.addView(btnNextToSymptoms)

        // Progress Bar
        progressBar = ProgressBar(this).apply {
            visibility = ProgressBar.GONE
        }
        layout.addView(progressBar)

        setContentView(layout)
    }

    private fun setupClickListeners() {
        btnSelectVideo.setOnClickListener {
            selectVideoFile()
        }

        btnLoadCSV.setOnClickListener {
            loadCSVData()
        }

        btnNextToSymptoms.setOnClickListener {
            navigateToSymptomsActivity()
        }
    }

    private fun selectVideoFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*"
        startActivityForResult(intent, PICK_VIDEO_REQUEST)
    }

    private fun loadCSVData() {
        progressBar.visibility = ProgressBar.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Read CSV files from assets
                val accelX = readCSVFile("CSVBreatheX.csv")
                val accelY = readCSVFile("CSVBreatheY.csv")
                val accelZ = readCSVFile("CSVBreatheZ.csv")

                // Calculate respiratory rate
                respiratoryRate = respiratoryRateCalculator(accelX, accelY, accelZ)

                runOnUiThread {
                    progressBar.visibility = ProgressBar.GONE
                    tvRespiratoryRateResult.text = "Respiratory Rate: $respiratoryRate BPM"
                    checkIfReadyToProceed()
                    Toast.makeText(this@VitalSignsActivity, "Respiratory rate calculated successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    progressBar.visibility = ProgressBar.GONE
                    Toast.makeText(this@VitalSignsActivity, "Error loading CSV files: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun readCSVFile(filename: String): MutableList<Float> {
        val values = mutableListOf<Float>()
        try {
            val inputStream = assets.open(filename)
            val reader = inputStream.bufferedReader()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                line?.toFloatOrNull()?.let { values.add(it) }
            }
            reader.close()
        } catch (e: Exception) {
            throw Exception("Failed to read $filename: ${e.message}")
        }
        return values
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let { videoUri ->
                progressBar.visibility = ProgressBar.VISIBLE

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        heartRate = heartRateCalculator(videoUri, contentResolver)

                        runOnUiThread {
                            progressBar.visibility = ProgressBar.GONE
                            tvHeartRateResult.text = "Heart Rate: $heartRate BPM"
                            checkIfReadyToProceed()
                            Toast.makeText(this@VitalSignsActivity, "Heart rate calculated successfully", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            progressBar.visibility = ProgressBar.GONE
                            Toast.makeText(this@VitalSignsActivity, "Error calculating heart rate: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun checkIfReadyToProceed() {
        btnNextToSymptoms.isEnabled = (heartRate != null && respiratoryRate != null)
    }

    private fun navigateToSymptomsActivity() {
        val intent = Intent(this, SymptomsActivity::class.java).apply {
            putExtra("HEART_RATE", heartRate)
            putExtra("RESPIRATORY_RATE", respiratoryRate)
        }
        startActivity(intent)
    }
}