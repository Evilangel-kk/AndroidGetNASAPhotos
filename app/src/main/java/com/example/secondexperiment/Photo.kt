package com.example.secondexperiment

class Photo constructor(id:Int,url:String){
    private var id:Int
    private var url:String
    init{
        this.id=id
        this.url=url
    }
    fun setId(id:Int){
        this.id=id
    }
    fun setUrl(url:String){
        this.url=url
    }
    fun getId(): Int {
        return this.id
    }
    fun getUrl():String{
        return this.url
    }
}