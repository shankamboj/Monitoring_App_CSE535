package com.example.monitoringapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
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
        setContentView(R.layout.activity_vital_signs)

        btnSelectVideo = findViewById(R.id.btnSelectVideo)
        btnLoadCSV = findViewById(R.id.btnLoadCSV)
        btnNextToSymptoms = findViewById(R.id.btnNextToSymptoms)
        tvHeartRateResult = findViewById(R.id.tvHeartRateResult)
        tvRespiratoryRateResult = findViewById(R.id.tvRespiratoryRateResult)
        progressBar = findViewById(R.id.progressBar)

        setupClickListeners()
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
                val accelX = readCSVFile("CSVBreatheX.csv")
                val accelY = readCSVFile("CSVBreatheY.csv")
                val accelZ = readCSVFile("CSVBreatheZ.csv")

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
