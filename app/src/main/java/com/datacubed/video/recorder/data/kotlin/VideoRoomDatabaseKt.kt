package com.datacubed.video.recorder.data.kotlin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [VideoModel::class], version = 1, exportSchema = false)
public abstract class VideoRoomDatabaseKt : RoomDatabase() {

    abstract fun videoModelDao(): VideoModelDao

    companion object {
        @Volatile
        private var INSTANCE: VideoRoomDatabaseKt? = null

        fun getDatabase(context: Context, scope: CoroutineScope): VideoRoomDatabaseKt {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        VideoRoomDatabaseKt::class.java,
                        "videos_db")
                        .build()
                INSTANCE = instance
                return instance
            }

        }
    }

//    private class VideoRoomDatabaseKtCallback(
//            private val scope: CoroutineScope
//    ) : RoomDatabase.Callback() {
//        override fun onOpen(db: SupportSQLiteDatabase) {
//            super.onOpen(db)
//            INSTANCE?.let {
//                scope.launch {
//                    initDatabase(it.videoModelDao())
//                }
//            }
//        }
//
//        suspend fun initDatabase(videoModelDao: VideoModelDao) {
//            val video = VideoModel(1,
//                    "title",
//                    21,
//                    System.currentTimeMillis(),
//                    "mock path")
//            videoModelDao.insert(video)
//        }
//    }
}