package com.example.monitoringapp

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.monitoringapp.ui.theme.MonitoringAppTheme
import android.widget.Toast
import com.example.healthmonitor.VitalSignsActivity
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRecordData = findViewById<Button>(R.id.btnRecordData)
        val btnDeleteData = findViewById<Button>(R.id.btnDeleteData)

        // Button 1: Navigate to Activity 1 (Vital Signs)
        btnRecordData.setOnClickListener {
            val intent = Intent(this, VitalSignsActivity::class.java)
            startActivity(intent)
        }

        // Button 2: Delete all data from database
        btnDeleteData.setOnClickListener {
            deleteAllData()
        }
    }

    private fun deleteAllData() {
        Toast.makeText(this, "All data deleted successfully", Toast.LENGTH_SHORT).show()
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MonitoringAppTheme {
        Greeting("Android")
    }
}