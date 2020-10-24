package com.datacubed.video.recorder.feature.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.datacubed.video.recorder.R
import com.datacubed.video.recorder.feature.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityKt : AppCompatActivity(), HomeFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var navigation: BottomNavigationView = findViewById(R.id.nav_view)
        val viewPager: ViewPager2 = findViewById(R.id.host_view_pager)
//        viewPager.adapter(MainAdapter(this,this))
    }

    override fun onNewVideoRecorded(pos: Int) {
        TODO("Not yet implemented")
    }
}