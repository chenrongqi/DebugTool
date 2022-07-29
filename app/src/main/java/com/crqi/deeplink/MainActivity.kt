package com.crqi.deeplink

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crqi.deeplink.qrcodescan.CaptureActivity
import com.crqi.deeplink.util.MMkvUtils
import com.facebook.drawee.view.SimpleDraweeView
import java.net.URLEncoder
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    val commonButton: Button by lazy {
        findViewById<Button>(R.id.button_send)
    }


    val enCodeButton: Button by lazy {
        findViewById<Button>(R.id.button_send_encode)
    }

    val editText: EditText by lazy {
        findViewById<EditText>(R.id.edit)
    }

    val preText: TextView by lazy {
        findViewById<TextView>(R.id.text)
    }

    val captureBtn: Button by lazy {
        findViewById(R.id.q_card)
    }

    val eyeBtn: Button by lazy {
        findViewById(R.id.eye_card)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        commonButton.setOnClickListener {
            var data = editText.text.toString()
            if (!TextUtils.isEmpty(data)) {
                MMkvUtils.setString("MainActivity_deeplink_pre", data)
                data = data.replace(" ", "")
                preText.setText(data)
                val intent = Intent(Intent.ACTION_VIEW)
                // 原生App中可以使用hap、http、https三种链接
                // 原生App中可以使用hap、http、https三种链接
                intent.setData(Uri.parse(data));
                startActivity(intent)
            }
        }
        enCodeButton.setOnClickListener {
            var data = editText.text.toString()
            if (!TextUtils.isEmpty(data) && data.contains("data=")) {
                MMkvUtils.setString("MainActivity_deeplink_pre", data)
                data = data.replace(" ", "")
                val index = data.indexOf("data=") + 5;
                val dataValue = data.substring(index)
                val deelplinkValue = data.substring(0, index)
                val fullDeeplink = deelplinkValue + URLEncoder.encode(dataValue, "UTF-8")
                preText.setText(fullDeeplink)
                val intent = Intent(Intent.ACTION_VIEW)
                // 原生App中可以使用hap、http、https三种链接
                // 原生App中可以使用hap、http、https三种链接
                intent.setData(Uri.parse(fullDeeplink));
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "只允许带data的url跳转", Toast.LENGTH_SHORT).show()
            }
        }

        editText.setText(MMkvUtils.getString("MainActivity_deeplink_pre", ""))


        captureBtn?.setOnClickListener {
            clickQr();
        }

        eyeBtn?.setOnClickListener {
            startActivity(Intent(this,NakeEyeDemoActivity::class.java))
        }
    }

    private fun clickQr(){
        if(PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)){
            startActivity(Intent(this, CaptureActivity::class.java))
        }else{
            var perms = arrayOf(android.Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this,perms,101);
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startActivity(Intent(this, CaptureActivity::class.java))
        }
    }


}