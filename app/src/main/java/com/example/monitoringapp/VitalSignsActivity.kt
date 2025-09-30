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

    private lateinit var videoUpload:Button
    private lateinit var csvUpload:Button
    private lateinit var next:Button
    private lateinit var heartRateData:TextView
    private lateinit var respiratoryData:TextView
    private lateinit var loading:ProgressBar
    private var heartRateOutput:Int?=null
    private var repirationRateOutput:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vital_signs)

        videoUpload =findViewById(R.id.buttonForSelectingTheVideo)
        csvUpload =findViewById(R.id.ButtonForLoadingCSV)
        next =findViewById(R.id.takeMeToTheNextPage)
        heartRateData=findViewById(R.id.outputForHeartRate)
        respiratoryData=findViewById(R.id.oytputForRespiration)
        loading=findViewById(R.id.loadingCircle)

        whatHappensOnButonClick()
    }

    fun whatHappensOnButonClick() {
        videoUpload.setOnClickListener {
            uploadVideo()
        }

        csvUpload.setOnClickListener {
            uploadCSV()
        }

        next.setOnClickListener {
            takeMeToNextPage()
        }
    }

    fun uploadVideo() {
        val i=Intent(Intent.ACTION_GET_CONTENT)
        i.type="video/*"
        startActivityForResult(i,1)
    }

    fun uploadCSV() {
        loading.visibility=ProgressBar.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file1=readCSV("file1.csv")
                val file2=readCSV("file2.csv")
                val file3=readCSV("file3.csv")

                repirationRateOutput=respiratoryRateCalculator(file1, file2, file3)

                runOnUiThread {
                    loading.visibility=ProgressBar.GONE
                    respiratoryData.text="Respiratory Rate: $repirationRateOutput BPM"
                    checkIfReadyToProceed()
                    Toast.makeText(this@VitalSignsActivity, "Respiratory rate calculation success", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    loading.visibility = ProgressBar.GONE
                    Toast.makeText(this@VitalSignsActivity, "Error loading the files: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun readCSV(filename: String): MutableList<Float> {
        val output=mutableListOf<Float>()
        try {
            val fileDataStream=assets.open(filename)
            val streamBuffer=fileDataStream.bufferedReader()
            var l:String?
            while (streamBuffer.readLine().also {
                l=it } != null) {
                l?.toFloatOrNull()?.let{
                    output.add(it)}
            }
            streamBuffer.close() // closing the stream
        } catch (e: Exception) {
            throw Exception("Unable to read $filename: ${e.message}")
        }
        return output
    }

    override fun onActivityResult(requestCode: Int,resultCode:Int,data:Intent?) {
        super.onActivityResult(requestCode,resultCode,data)

        if (resultCode==-1 && requestCode==1) {
            data?.data?.let {uri ->
                loading.visibility=ProgressBar.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        heartRateOutput=heartRateCalculator(uri,contentResolver)
                        runOnUiThread {
                            loading.visibility=ProgressBar.GONE
                            heartRateData.text="Heart Rate: $heartRateOutput BPM"
                            checkIfReadyToProceed()
                            Toast.makeText(this@VitalSignsActivity, "Heart rate calculated successfully", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            loading.visibility=ProgressBar.GONE
                            Toast.makeText(this@VitalSignsActivity, "Error calculating heart rate: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun checkIfReadyToProceed() {
        next.isEnabled=(repirationRateOutput != null && heartRateOutput!= null)
    }

    private fun takeMeToNextPage() {
        val i=Intent(this, SymptomsActivity::class.java).apply {
            putExtra("HEART_RATE", heartRateOutput)
            putExtra("RESPIRATORY_RATE", repirationRateOutput)
        }
        startActivity(i)
    }
}
