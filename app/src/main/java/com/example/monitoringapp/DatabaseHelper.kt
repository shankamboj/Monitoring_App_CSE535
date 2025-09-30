package com.example.monitoringapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {

        const val sore_throat = "sore_throat"
        const val id = "id"
        const val COLUMN_FATIGUE = "fatigue"
        const val timestamp = "timestamp"
        const val loss_of_taste = "loss_of_taste"
        const val fever = "fever"
        const val TABLE_RECORDS = "health_records"
        const val shortness_of_breath = "shortness_of_breath"
        const val DATABASE_NAME = "HealthMonitor.db"
        const val cough = "cough"
        const val respiratory_rate = "respiratory_rate"
        const val headache = "headache"
        const val muscle_ache = "muscle_ache"
        const val heart_rate = "heart_rate"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
           CREATE TABLE $TABLE_RECORDS (
            $id INTEGER PRIMARY KEY AUTOINCREMENT,
            $fever INTEGER DEFAULT 0,
            $respiratory_rate REAL,
            $loss_of_taste INTEGER DEFAULT 0,
            $timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
            $COLUMN_FATIGUE INTEGER DEFAULT 0,
            $sore_throat INTEGER DEFAULT 0,
            $muscle_ache INTEGER DEFAULT 0,
            $heart_rate REAL,
            $shortness_of_breath INTEGER DEFAULT 0,
            $headache INTEGER DEFAULT 0,
            $cough INTEGER DEFAULT 0
        )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECORDS")
        onCreate(db)
    }
}