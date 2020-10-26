package com.datacubed.video.recorder.data.kotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope

class VideoListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: VideoModelRepository
    val videoListLiveData: LiveData<List<VideoModel>>

    init {
        val videoModelDao = VideoRoomDatabaseKt.getDatabase(application,viewModelScope).videoModelDao()
        repository = VideoModelRepository(videoModelDao)
        videoListLiveData = repository.videoList
    }
}