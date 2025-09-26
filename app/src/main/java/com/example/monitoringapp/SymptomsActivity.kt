package com.example.monitoringapp

import androidx.activity.ComponentActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.LinearLayout

class SymptomsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 50, 50, 50)
        }

        val title = TextView(this).apply {
            text = "Symptoms Activity"
            textSize = 24f
        }
        layout.addView(title)

        setContentView(layout)
    }
}