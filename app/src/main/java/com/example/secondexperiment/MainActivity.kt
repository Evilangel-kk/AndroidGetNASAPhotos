package com.example.secondexperiment

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.secondexperiment.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import org.json.JSONArray
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //静态常量权限码
    private val REQUEST_EXTERNAL_STORAGE = 101
    //静态数组，具体权限
    private val PERMISSIONS_STORAGE = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.MOUNT_UNMOUNT_FILESYSTEMS",
        "android.permission.MANAGE_EXTERNAL_STORAGE",
        "android.permission.READ_CONTACTS",
        "android.permission.READ_PHONE_STATE",
        "android.permission.READ_MEDIA_IMAGES",
        "android.permission.WRITE_EXTERNA"
    )
    private lateinit var dbHelper:MyDataBaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d("MainActivity", "请开通相关权限")
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show()
            }else{
                //申请权限  参数1.context 2.具体权限 3.权限码
                Log.d("MainActivity", "申请授权")
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
            }
        } else {
            Toast.makeText(this, "已授权！", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", "已授权")
            //有权限再获取资源，否则获取也无法下载到本地
        }
        createDataBase()
        binding.sendRequestBtn.setOnClickListener {
            sendRequestWithOkHttp()
            Picasso.get()
                .load(R.drawable.loading)
                .into(binding.imageView)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            1-> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "onRequestPermissionsResult: done")
                    Toast.makeText(this, "你可以正常使用app", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("MainActivity", "onRequestPermissionsResult: failed")
                    Toast.makeText(this, "你拒绝了权限，无法正常保存图片", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun sendRequestWithOkHttp() {
        thread {
            try {
                var address="https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key="
                val myAPIKey="g3c2e3X1sVJDgNJkdO7WkUWAPgVv3tjxLlijlDOk"
                address+=myAPIKey
                HttpUtil.sendHttpRequest(address,object:HttpCallbackListener{
                    override fun onFinish(response: String) {
                        val finish=parseJSONWithJSONObject(response)
                        if(finish){
                            val intent= Intent(this@MainActivity,ShowImages::class.java)
                            startActivity(intent)
                        }
                    }
                    override fun onError(e: Exception) {
                        e.printStackTrace()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseJSONWithJSONObject(jsonData: String):Boolean {
        try {
            val begin=jsonData.indexOf("[")
            val end=jsonData.lastIndexOf("]")
            var data=jsonData.substring(begin..end)
            val jsonArray = JSONArray(data)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val imgSrc = jsonObject.getString("img_src")
//                存入单例类
                PhotoList.add(Photo(id,imgSrc))
//                存入数据库
                insertToPhotos(Photo(id,imgSrc))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    //    创建数据库
    private fun createDataBase(){
        dbHelper = MyDataBaseHelper(this, "Photos.db", 1)
        dbHelper.writableDatabase
    }

//    插入数据库
    private fun insertToPhotos(photo:Photo){
        val db=dbHelper.writableDatabase
        val cursor = db.rawQuery("select count(*) from Photo where id=?",arrayOf(photo.getId().toString()))
        cursor.moveToNext()
        val isExist=cursor.getInt(0)
        cursor.close();
        if(isExist==0){
            // 插入数据
            val values = ContentValues().apply {
                // 开始组装数据
                put("id", photo.getId())
                put("url", photo.getUrl())
            }
            db.insert("Photo", null, values) // 插入数据
            Log.d("MainActivity","Inserted One Photo")
        }
        db.close()
    }

}