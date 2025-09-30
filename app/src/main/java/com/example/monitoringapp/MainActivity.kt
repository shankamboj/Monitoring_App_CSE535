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

        val layout=LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(99, 99, 99, 98)
        }
        layout.addView(TextView(this).apply {
            text="Health Monitor"
            textSize=27f
            setPadding(2, 2, 2, 98)
        })
        val btnRecordData=Button(this).apply {
            text="Record health data"
            setPadding(1, 49, 1, 49)
        }
        layout.addView(btnRecordData)

        var btnDel=Button(this).apply {
            text="Delete all recorded data"
            setPadding(1, 49, 1, 49)
        }
        layout.addView(btnDel)

        layout.setBackgroundColor(Color.parseColor("#FFBFB4"));


        setContentView(layout)
        btnDel.setOnClickListener {
            val db=DatabaseHelper(this).writableDatabase
            db.delete(DatabaseHelper.TABLE_RECORDS, null, null)
            db.close()
            Toast.makeText(this, "Past data cleaned fully", Toast.LENGTH_SHORT).show()

        }
        btnRecordData.setOnClickListener {
            startActivity(Intent(this, VitalSignsActivity::class.java))
        }
    }
}