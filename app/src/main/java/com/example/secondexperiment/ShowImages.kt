package com.example.secondexperiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.secondexperiment.databinding.ActivityShowImagesBinding
import java.util.Collections.list

class ShowImages : AppCompatActivity() {
    private lateinit var binding: ActivityShowImagesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityShowImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var dbHelper=MyDataBaseHelper(this,"Photos.db",1)
        var db=dbHelper.writableDatabase
        var list = ArrayList<Photo>()
        var cursor = db.rawQuery("select * from Photo",null)
        while(cursor.moveToNext()){
            list.add(Photo(cursor.getInt(0),cursor.getString(1)))
        }
        binding.gridview.adapter = ImageAdapter(this, R.layout.photo_item, list)
    }
}