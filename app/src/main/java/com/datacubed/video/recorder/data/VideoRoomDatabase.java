package com.datacubed.video.recorder.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {VideoInfo.class},
    version = 1,
    exportSchema = false)
public abstract class VideoRoomDatabase extends RoomDatabase {

  public static final String VIDEO_DB = "videos_db";
  private static final int NUMBER_OF_THREADS = 4;
  static final ExecutorService databaseWriteExecutor =
      Executors.newFixedThreadPool(NUMBER_OF_THREADS);
  private static volatile VideoRoomDatabase INSTANCE;
  private static final RoomDatabase.Callback roomDbCallback =
      new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
          super.onOpen(db);
          databaseWriteExecutor.execute(
              () -> {
                // TODO
                //                for (int i = 0; i < 20; i++) {
                //                  INSTANCE.videoInfoDao()
                //                      .insert(
                //                          new VideoInfo("title" + i, 200,
                // System.currentTimeMillis(), "mock path"));
                //                }
              });
        }
      };

  static VideoRoomDatabase getDataBase(final Context context) {
    if (INSTANCE == null) {
      synchronized ((VideoRoomDatabase.class)) {
        if (INSTANCE == null) {
          INSTANCE =
              Room.databaseBuilder(
                      context.getApplicationContext(), VideoRoomDatabase.class, VIDEO_DB)
                  .addCallback(roomDbCallback)
                  .build();
        }
      }
    }
    return INSTANCE;
  }

  public abstract VideoInfoDao videoInfoDao();
}
