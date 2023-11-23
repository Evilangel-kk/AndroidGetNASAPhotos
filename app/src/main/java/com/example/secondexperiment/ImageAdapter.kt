package com.example.secondexperiment

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

class ImageAdapter(activity: Activity, val resourceId: Int, data: List<Photo>) :
    ArrayAdapter<Photo>(activity, resourceId, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
        } else {
            view = convertView
        }
        val photoImage: ImageView = view.findViewById(R.id.photoImg)
        val photo: Photo? = getItem(position) // 获取当前项的Photo实例
        if (photo != null) {
            Log.d("Adapter",photo.getUrl())
            Picasso.get()
                .load(photo.getUrl())
                .placeholder(R.drawable.before)
                .error(R.drawable.none)
                .fit()
                .into(photoImage)
        }
        return view
    }
}