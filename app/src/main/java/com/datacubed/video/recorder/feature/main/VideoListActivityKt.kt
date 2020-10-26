package com.datacubed.video.recorder.feature.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.datacubed.video.recorder.R
import com.datacubed.video.recorder.data.kotlin.VideoListViewModel

class VideoListActivityKt : AppCompatActivity() {
    private lateinit var videoListViewModel: VideoListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_saved_recordings)
        val recyclerView = findViewById<RecyclerView>(R.id.saved_list_rv)
        val adapter = VideoListAdapterKt(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this,2)
        videoListViewModel = ViewModelProvider(this).get(VideoListViewModel::class.java)
        videoListViewModel.videoListLiveData.observe(this, { list ->
            run {
                list?.let { adapter.setList(list) }
            }
        })
    }

}