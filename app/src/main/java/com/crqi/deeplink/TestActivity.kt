package com.crqi.deeplink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        Log.d("TestActivity","onCreate")
        finish()
    }

    override fun onStart() {
        Log.d("TestActivity","onStart")
        super.onStart()
    }

    override fun onStop() {
        Log.d("TestActivity","onStop")
        super.onStop()
    }

    override fun onResume() {
        Log.d("TestActivity","onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("TestActivity","onPause")
        super.onPause()
    }

    override fun onDestroy() {
        Log.d("TestActivity","onDestroy")
        super.onDestroy()
    }
}