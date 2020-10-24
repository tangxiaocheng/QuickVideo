package com.datacubed.video.recorder.data.kotlin

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface VideoModelDao {
    @Query("SELECT * FROM video_table ORDER BY title ASC")
    fun getVideoList(): LiveData<List<VideoModel>>

    suspend fun insert(videoModel: VideoModel)
}