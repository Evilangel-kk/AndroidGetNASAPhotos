package com.example.secondexperiment

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MyDataBaseHelper(context: AppCompatActivity, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    private val createBook = "create table Photo (" +
            " id integer primary key," +
            "url text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createBook)
        Log.d("MyDataBaseHelper","Create succeeded")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}