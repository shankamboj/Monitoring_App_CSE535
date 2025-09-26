package com.example.monitoringapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "HealthMonitor.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_RECORDS = "health_records"

        // Column names
        const val COLUMN_ID = "id"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_HEART_RATE = "heart_rate"
        const val COLUMN_RESPIRATORY_RATE = "respiratory_rate"
        const val COLUMN_HEADACHE = "headache"
        const val COLUMN_SORE_THROAT = "sore_throat"
        const val COLUMN_FEVER = "fever"
        const val COLUMN_MUSCLE_ACHE = "muscle_ache"
        const val COLUMN_LOSS_OF_TASTE = "loss_of_taste"
        const val COLUMN_COUGH = "cough"
        const val COLUMN_SHORTNESS_OF_BREATH = "shortness_of_breath"
        const val COLUMN_FATIGUE = "fatigue"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_RECORDS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP,
                $COLUMN_HEART_RATE REAL,
                $COLUMN_RESPIRATORY_RATE REAL,
                $COLUMN_HEADACHE INTEGER DEFAULT 0,
                $COLUMN_SORE_THROAT INTEGER DEFAULT 0,
                $COLUMN_FEVER INTEGER DEFAULT 0,
                $COLUMN_MUSCLE_ACHE INTEGER DEFAULT 0,
                $COLUMN_LOSS_OF_TASTE INTEGER DEFAULT 0,
                $COLUMN_COUGH INTEGER DEFAULT 0,
                $COLUMN_SHORTNESS_OF_BREATH INTEGER DEFAULT 0,
                $COLUMN_FATIGUE INTEGER DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECORDS")
        onCreate(db)
    }
}