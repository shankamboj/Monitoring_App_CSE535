package com.example.monitoringapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class SymptomsActivity : ComponentActivity() {

    private lateinit var btnUploadSymptoms: Button

    // Radio Groups for each symptom
    private lateinit var rgHeadache: RadioGroup
    private lateinit var rgSoreThroat: RadioGroup
    private lateinit var rgFever: RadioGroup
    private lateinit var rgMuscleAche: RadioGroup
    private lateinit var rgLossOfTaste: RadioGroup
    private lateinit var rgCough: RadioGroup
    private lateinit var rgShortnessOfBreath: RadioGroup
    private lateinit var rgFatigue: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptoms)

        initializeViews()
        setupClickListeners()
        setDefaultRatings()
    }

    private fun initializeViews() {
        btnUploadSymptoms = findViewById(R.id.btnUploadSymptoms)

        // Initialize all radio groups
        rgHeadache = findViewById(R.id.rgHeadache)
        rgSoreThroat = findViewById(R.id.rgSoreThroat)
        rgFever = findViewById(R.id.rgFever)
        rgMuscleAche = findViewById(R.id.rgMuscleAche)
        rgLossOfTaste = findViewById(R.id.rgLossOfTaste)
        rgCough = findViewById(R.id.rgCough)
        rgShortnessOfBreath = findViewById(R.id.rgShortnessOfBreath)
        rgFatigue = findViewById(R.id.rgFatigue)
    }

    private fun setupClickListeners() {
        btnUploadSymptoms.setOnClickListener {
            uploadSymptomsToDatabase()
        }
    }

    private fun setDefaultRatings() {
        // Set all ratings to 0 by default
        val radioGroups = listOf(
            rgHeadache, rgSoreThroat, rgFever, rgMuscleAche,
            rgLossOfTaste, rgCough, rgShortnessOfBreath, rgFatigue
        )

        radioGroups.forEach { radioGroup ->
            // Check the first radio button (rating 0) in each group
            val firstChild = radioGroup.getChildAt(0) as? RadioButton
            firstChild?.isChecked = true
        }
    }

    private fun getRadioGroupRating(radioGroup: RadioGroup): Int {
        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as RadioButton
            if (radioButton.isChecked) {
                return radioButton.text.toString().toIntOrNull() ?: 0
            }
        }
        return 0 // Default to 0 if nothing selected
    }

    private fun uploadSymptomsToDatabase() {
        val heartRate = intent.getIntExtra("HEART_RATE", 0)
        val respiratoryRate = intent.getIntExtra("RESPIRATORY_RATE", 0)

        if (heartRate == 0 || respiratoryRate == 0) {
            Toast.makeText(this, "Vital signs data missing!", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        // Get ratings from all radio groups
        val headacheRating = getRadioGroupRating(rgHeadache)
        val soreThroatRating = getRadioGroupRating(rgSoreThroat)
        val feverRating = getRadioGroupRating(rgFever)
        val muscleAcheRating = getRadioGroupRating(rgMuscleAche)
        val lossOfTasteRating = getRadioGroupRating(rgLossOfTaste)
        val coughRating = getRadioGroupRating(rgCough)
        val shortnessOfBreathRating = getRadioGroupRating(rgShortnessOfBreath)
        val fatigueRating = getRadioGroupRating(rgFatigue)

        // Debug: Show what ratings we're getting
        Toast.makeText(this,
            "Ratings - Headache: $headacheRating, Fever: $feverRating, SoreThroat: $soreThroatRating",
            Toast.LENGTH_LONG
        ).show()

        val values = ContentValues().apply {
            put(DatabaseHelper.heart_rate, heartRate)
            put(DatabaseHelper.respiratory_rate, respiratoryRate)

            // Store all symptom ratings
            put(DatabaseHelper.headache, headacheRating)
            put(DatabaseHelper.sore_throat, soreThroatRating)
            put(DatabaseHelper.fever, feverRating)
            put(DatabaseHelper.muscle_ache, muscleAcheRating)
            put(DatabaseHelper.loss_of_taste, lossOfTasteRating)
            put(DatabaseHelper.cough, coughRating)
            put(DatabaseHelper.shortness_of_breath, shortnessOfBreathRating)
            put(DatabaseHelper.COLUMN_FATIGUE, fatigueRating)
        }

        try {
            val newRowId = db.insert(DatabaseHelper.TABLE_RECORDS, null, values)
            if (newRowId != -1L) {
                Toast.makeText(this, "Symptoms uploaded successfully!", Toast.LENGTH_SHORT).show()
                finish() // Return to main activity
            } else {
                Toast.makeText(this, "Failed to upload symptoms", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db.close()
        }
    }
}