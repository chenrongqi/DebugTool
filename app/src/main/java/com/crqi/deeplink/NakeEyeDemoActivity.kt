package com.crqi.deeplink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crqi.deeplink.nakedeye3d.DefaultSensorAdapter
import com.crqi.deeplink.nakedeye3d.croe.SensorNakedEyeLayout

class NakeEyeDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3d)

        var data = mutableListOf(
            "https://github.com/chenrongqi/data/raw/main/text/1.png",
            "https://github.com/chenrongqi/data/raw/main/text/2.png",
            "https://github.com/chenrongqi/data/raw/main/text/3.png"
        )

        findViewById<SensorNakedEyeLayout>(R.id.sensor).apply {
            setData(lifecycle,
                DefaultSensorAdapter(data)
            )
        }
    }
}