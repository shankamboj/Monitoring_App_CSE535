package com.example.monitoringapp

import android.graphics.Color;
import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create UI programmatically
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(100, 100, 100, 100)
        }



        val btnRecordData = Button(this).apply {
            text = "Record health data"
            setPadding(0, 50, 0, 50)
        }
        layout.addView(btnRecordData)

        val btnDeleteData = Button(this).apply {
            text = "Delete all recorded data"
            setPadding(0, 50, 0, 50)
        }
        layout.addView(btnDeleteData)

        val title = TextView(this).apply {
            text = "Health Monitor"
            textSize = 28f
            setPadding(0, 0, 0, 100)
        }
        layout.setBackgroundColor(Color.parseColor("#FFBFB5"));
        layout.addView(title)

        setContentView(layout)

        // Set click listeners
        btnRecordData.setOnClickListener {
            val intent = Intent(this, VitalSignsActivity::class.java)
            startActivity(intent)
        }

        btnDeleteData.setOnClickListener {
            deleteAllData()
        }
    }

    private fun deleteAllData() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_RECORDS, null, null)
        db.close()
        Toast.makeText(this, "All data deleted successfully", Toast.LENGTH_SHORT).show()
    }
}