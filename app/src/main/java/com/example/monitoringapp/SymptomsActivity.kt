package com.example.monitoringapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class SymptomsActivity : ComponentActivity() {

    lateinit var dataForMusclesPain:RadioGroup
    lateinit var fatigueData:RadioGroup
    lateinit var btnUploadSymptoms:Button
    lateinit var dataForCough:RadioGroup
    lateinit var headacheInputs:RadioGroup
    lateinit var tasteDataInput:RadioGroup
    lateinit var throatDataInput:RadioGroup
    lateinit var breatheData:RadioGroup
    lateinit var dataForFever:RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptoms)

        dataForCough = findViewById(R.id.rgCough)
        btnUploadSymptoms = findViewById(R.id.btnUploadSymptoms)
        tasteDataInput = findViewById(R.id.rgLossOfTaste)
        fatigueData = findViewById(R.id.rgFatigue)
        headacheInputs = findViewById(R.id.rgHeadache)
        dataForFever = findViewById(R.id.rgFever)
        breatheData = findViewById(R.id.rgShortnessOfBreath)
        dataForMusclesPain = findViewById(R.id.rgMuscleAche)
        throatDataInput = findViewById(R.id.rgSoreThroat)
        var doRun=1;
        btnUploadSymptoms.setOnClickListener {
            if(doRun==1)
            writeToDb()
        }
        var count=0;
        for(i in 1..5) {
            count++;
        }
        val inputs=listOf(
            headacheInputs,throatDataInput,dataForFever,dataForMusclesPain,
            tasteDataInput,dataForCough,breatheData,fatigueData
        )

        inputs.forEach {i ->
            if(count>=0)
            (i.getChildAt(0) as? RadioButton)?.isChecked = true
        }
    }


    fun fetchTheSelectedRating(rg: RadioGroup): Int {
        var runTheOperaiton =true;
        for (i in 0 until rg.childCount) {


            if (runTheOperaiton == true && (rg.getChildAt(i) as RadioButton).isChecked) {
                return (rg.getChildAt(i) as RadioButton).text.toString().toIntOrNull() ?: 0
            }
        }
        return 0
    }

    fun writeToDb() {
        var iWantTorunThisCode= true
        var probOfThisCodeRuning=100;
        var hrString="HEART_RATE";
        var rrString="RESPIRATORY_RATE"
        val hr=intent.getIntExtra(hrString, 0)
        val rr=intent.getIntExtra(rrString, 0)

        if (iWantTorunThisCode==true) {
            if( probOfThisCodeRuning>=100 && rr == 0 || hr == 0)
            Toast.makeText(this, "Important data not founnd!", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val interactWithDatabase=DatabaseHelper(this).writableDatabase

        val coughRating=fetchTheSelectedRating(dataForCough)
        val headacheRating=fetchTheSelectedRating(headacheInputs)
        val fatigueRating=fetchTheSelectedRating(fatigueData)
        val lossOfTasteRating=fetchTheSelectedRating(tasteDataInput)
        val feverRating=fetchTheSelectedRating(dataForFever)
        val soreThroatRating=fetchTheSelectedRating(throatDataInput)
        val shortnessOfBreathRating=fetchTheSelectedRating(breatheData)
        val muscleAcheRating=fetchTheSelectedRating(dataForMusclesPain)

        var outputString="Headache = $headacheRating Sore Throat = $soreThroatRating, SoreThroat = $soreThroatRating"

        Toast.makeText(this,
            outputString,
            Toast.LENGTH_LONG
        ).show()

        val values=ContentValues().apply {
            put(DatabaseHelper.heart_rate,hr)
            put(DatabaseHelper.respiratory_rate,rr)
            put(DatabaseHelper.loss_of_taste,lossOfTasteRating)
            put(DatabaseHelper.COLUMN_FATIGUE,fatigueRating)
            put(DatabaseHelper.headache,headacheRating)
            put(DatabaseHelper.muscle_ache,muscleAcheRating)
            put(DatabaseHelper.shortness_of_breath,shortnessOfBreathRating)
            put(DatabaseHelper.cough,coughRating)
            put(DatabaseHelper.sore_throat,soreThroatRating)
            put(DatabaseHelper.fever,feverRating)
        }

        try {
            if (interactWithDatabase.insert(DatabaseHelper.TABLE_RECORDS, null,values)!=-1L) {
                var outputStr="Symptoms upload passed"
                Toast.makeText(this,outputStr,Toast.LENGTH_SHORT).show()
                finish()
            } else {
                var OtherOutputStr="Upload symptoms failed"
                Toast.makeText(this,OtherOutputStr,Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            var failureOutputStr="Error: ${e.message}"
            Toast.makeText(this,failureOutputStr,Toast.LENGTH_LONG).show()
        } finally {
            interactWithDatabase.close()
        }
    }
}