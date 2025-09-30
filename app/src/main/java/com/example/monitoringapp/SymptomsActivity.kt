package com.example.monitoringapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class SymptomsActivity : ComponentActivity() {

    lateinit var btnUploadSymptoms:Button
    lateinit var headacheInputs:RadioGroup
    lateinit var throatDataInput:RadioGroup
    lateinit var dataForFever:RadioGroup
    lateinit var dataForMusclesPain:RadioGroup
    lateinit var tasteDataInput:RadioGroup
    lateinit var dataForCough:RadioGroup
    lateinit var breatheData:RadioGroup
    lateinit var fatigueData:RadioGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptoms)

        assignVariablesForInpuTheirElement()
        whatHappensOnClick()
        byDefaultWeShouldTickRadioWith0Value()
    }

    fun assignVariablesForInpuTheirElement() {
        btnUploadSymptoms = findViewById(R.id.btnUploadSymptoms)
        headacheInputs=findViewById(R.id.rgHeadache)
        throatDataInput=findViewById(R.id.rgSoreThroat)
        dataForFever=findViewById(R.id.rgFever)
        dataForMusclesPain=findViewById(R.id.rgMuscleAche)
        tasteDataInput=findViewById(R.id.rgLossOfTaste)
        dataForCough=findViewById(R.id.rgCough)
        breatheData=findViewById(R.id.rgShortnessOfBreath)
        fatigueData=findViewById(R.id.rgFatigue)
    }

    fun whatHappensOnClick() {
        btnUploadSymptoms.setOnClickListener {
            writeToDb()
        }
    }

    fun byDefaultWeShouldTickRadioWith0Value() {
        val inputs=listOf(
            headacheInputs,throatDataInput,dataForFever,dataForMusclesPain,
            tasteDataInput,dataForCough,breatheData,fatigueData
        )

        inputs.forEach {i ->
            (i.getChildAt(0) as? RadioButton)?.isChecked = true
        }
    }

    fun fetchTheSelectedRating(rg: RadioGroup): Int {
        for (i in 0 until rg.childCount) {
            if ((rg.getChildAt(i) as RadioButton).isChecked) {
                return (rg.getChildAt(i) as RadioButton).text.toString().toIntOrNull() ?: 0
            }
        }
        return 0
    }

    fun writeToDb() {
        var hrString = "HEART_RATE";
        var rrString = "RESPIRATORY_RATE"
        val hr=intent.getIntExtra(hrString, 0)
        val rr=intent.getIntExtra(rrString, 0)

        if (rr == 0 || hr == 0) {
            Toast.makeText(this, "Important data not founnd!", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val interactWithDatabase=DatabaseHelper(this).writableDatabase

        val headacheRating=fetchTheSelectedRating(headacheInputs)
        val soreThroatRating=fetchTheSelectedRating(throatDataInput)
        val feverRating=fetchTheSelectedRating(dataForFever)
        val muscleAcheRating=fetchTheSelectedRating(dataForMusclesPain)
        val lossOfTasteRating=fetchTheSelectedRating(tasteDataInput)
        val coughRating=fetchTheSelectedRating(dataForCough)
        val shortnessOfBreathRating=fetchTheSelectedRating(breatheData)
        val fatigueRating=fetchTheSelectedRating(fatigueData)

        var outputString="Headache = $headacheRating Sore Throat = $soreThroatRating, SoreThroat = $soreThroatRating"

        Toast.makeText(this,
            outputString,
            Toast.LENGTH_LONG
        ).show()

        val values=ContentValues().apply {
            put(DatabaseHelper.heart_rate,hr)
            put(DatabaseHelper.respiratory_rate,rr)

            // Store all symptom ratings
            put(DatabaseHelper.headache,headacheRating)
            put(DatabaseHelper.sore_throat,soreThroatRating)
            put(DatabaseHelper.fever,feverRating)
            put(DatabaseHelper.muscle_ache,muscleAcheRating)
            put(DatabaseHelper.loss_of_taste,lossOfTasteRating)
            put(DatabaseHelper.cough,coughRating)
            put(DatabaseHelper.shortness_of_breath,shortnessOfBreathRating)
            put(DatabaseHelper.COLUMN_FATIGUE,fatigueRating)
        }

        try {
            if (interactWithDatabase.insert(DatabaseHelper.TABLE_RECORDS, null,values)!=-1L) {
                Toast.makeText(this, "Symptoms upload passed",Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Upload symptoms failed",Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}",Toast.LENGTH_LONG).show()
        } finally {
            interactWithDatabase.close()
        }
    }
}