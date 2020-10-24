package com.datacubed.video.recorder.feature.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.datacubed.video.recorder.R

class EntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_acitviy)
        findViewById<View>(R.id.kotlin_button).setOnClickListener {
            val intent = Intent(this, VideoListActivityKt::class.java)
            startActivity(intent)
        }
        findViewById<View>(R.id.java_button).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        }
    }
}