package com.example.secondexperiment

interface HttpCallbackListener {
    fun onFinish(response: String)
    fun onError(e: Exception)
}