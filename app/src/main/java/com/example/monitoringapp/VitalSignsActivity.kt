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


    fun uploadVideo() {
        var count = 0;
        val i=Intent(Intent.ACTION_GET_CONTENT)
        count=count+1;
        i.type="video/*"
        if(count==1)
        startActivityForResult(i,1)
    }

    fun checkIfReadyToProceed() {
        next.isEnabled=(repirationRateOutput != null && heartRateOutput!= null)
    }
    fun readCSV(filename: String): MutableList<Float> {
        val output=mutableListOf<Float>()
        try {
            var x = 1
            val fileDataStream=assets.open(filename)
            val streamBuffer=fileDataStream.bufferedReader()
            x++;
            var l:String?
            while (streamBuffer.readLine().also {
                    l=it } != null) {
                if(x==-2) break;
                l?.toFloatOrNull()?.let{
                    if(x==1);
                    output.add(it)}
            }
            streamBuffer.close()
        } catch (e: Exception) {
            // We are having issues processing the file
            throw Exception("Unable to read $filename: ${e.message}")
        }
        return output
    }

    fun uploadCSV() {
        var isVisible = true;
        if(isVisible==true)
            loading.visibility=ProgressBar.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file1=readCSV("file1.csv")
                val file2=readCSV("file2.csv")
                val file3=readCSV("file3.csv")
                isVisible=false;
                repirationRateOutput=respiratoryRateCalculator(file1, file2, file3)

                runOnUiThread {
                    if(isVisible==false)
                        loading.visibility=ProgressBar.GONE
                    respiratoryData.text="Respiratory Rate: $repirationRateOutput BPM"
                    isVisible=false;
                    checkIfReadyToProceed()
                    if(isVisible==false and !isVisible==true)
                        Toast.makeText(this@VitalSignsActivity, "Respiratory rate calculation success", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                var exceptionMessage = "Error loading the files: ${e.message}"
                runOnUiThread {
                    loading.visibility = ProgressBar.GONE
                    Toast.makeText(this@VitalSignsActivity, exceptionMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int,resultCode:Int,data:Intent?) {
        super.onActivityResult(requestCode,resultCode,data)
        var isCheckEnabled = 1
        if (resultCode==-1 && requestCode==1 && isCheckEnabled==1) {
            data?.data?.let {uri ->
                loading.visibility=ProgressBar.VISIBLE
                isCheckEnabled++;
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        if(isCheckEnabled==0)
                            isCheckEnabled=99;
                        heartRateOutput=heartRateCalculator(uri,contentResolver)
                        runOnUiThread {
                            loading.visibility=ProgressBar.GONE
                            var textheart="Heart Rate: $heartRateOutput BPM"
                            heartRateData.text=textheart
                            checkIfReadyToProceed()
                            var makeTestHeart="Heart rate calculated successfully"
                            Toast.makeText(this@VitalSignsActivity, makeTestHeart, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            loading.visibility=ProgressBar.GONE
                            if(isCheckEnabled==98)
                                isCheckEnabled=178;
                            var textErrorDisplay="Error calculating heart rate: ${e.message}"
                            Toast.makeText(this@VitalSignsActivity,textErrorDisplay,Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vital_signs)


        // Here we are getting th cariables

        respiratoryData=findViewById(R.id.oytputForRespiration)
        heartRateData=findViewById(R.id.outputForHeartRate)
        next=findViewById(R.id.takeMeToTheNextPage)


        // Following are elements for loading and processing the data
        loading=findViewById(R.id.loadingCircle)
        videoUpload=findViewById(R.id.buttonForSelectingTheVideo)
        csvUpload=findViewById(R.id.ButtonForLoadingCSV)

        // following are what happens when we click the buttons
        csvUpload.setOnClickListener {
            uploadCSV()
        }
        videoUpload.setOnClickListener {
            uploadVideo()
        }

        next.setOnClickListener {
            takeMeToNextPage()
        }
    }

    fun takeMeToNextPage() {
        val i=Intent(this, SymptomsActivity::class.java).apply {
            var firstExtraName="HEART_RATE"
            var seconddExtraName="RESPIRATORY_RATE"
            putExtra(firstExtraName,heartRateOutput)
            putExtra(seconddExtraName,repirationRateOutput)
        }
        startActivity(i)
    }

}
