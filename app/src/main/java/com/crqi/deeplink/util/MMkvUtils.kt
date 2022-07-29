package com.crqi.deeplink.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object MMkvUtils {

    var context: Application?=null;

    private fun getSP(): SharedPreferences? {
        return MMkvUtils.context?.getSharedPreferences("deeplink_demo",Context.MODE_PRIVATE)
    }

    @JvmStatic
    fun getBoolean(key:String,defaultValue:Boolean):Boolean{
        return getSP()?.getBoolean(key,defaultValue)?:defaultValue
    }

    @JvmStatic
    fun getString(key:String,defaultValue:String):String{
        return getSP()?.getString(key,defaultValue)?:defaultValue
    }
    @JvmStatic
    fun setString(key:String,value:String){
         getSP()?.edit()?.putString(key,value)?.commit();
    }
}