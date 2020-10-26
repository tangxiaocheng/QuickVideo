package com.datacubed.video.recorder.data.kotlin

import androidx.lifecycle.LiveData

class VideoModelRepository(private val videoModelDao: VideoModelDao) {
    val videoList: LiveData<List<VideoModel>> = videoModelDao.getVideoList()
    suspend fun insert(videoModel: VideoModel) {
        videoModelDao.insert(videoModel)
    }
}