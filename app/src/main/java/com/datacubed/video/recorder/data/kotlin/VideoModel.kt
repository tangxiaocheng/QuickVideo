package com.datacubed.video.recorder.data.kotlin

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_table")
data class VideoModel(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val title: String,
        val duration: Int,
        val createdTime: Long,
        @NonNull val absolutePath: String)
