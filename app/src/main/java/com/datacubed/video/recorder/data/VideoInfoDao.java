package com.datacubed.video.recorder.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface VideoInfoDao {

  @Insert
  void insert(VideoInfo videoInfo);

  @Query("DELETE FROM video_table")
  void deleteAll();

  @Query("SELECT * from video_table ORDER BY createdTime DESC")
  LiveData<List<VideoInfo>> getListByAlphabetizedInTittle();


  @Query("SELECT * from video_table ORDER BY title ASC")
  List<VideoInfo> getVideoList();
}
