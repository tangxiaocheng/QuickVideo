package com.datacubed.video.recorder.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource.Factory;
import java.util.List;

public class VideoInfoRepository {

  private final VideoInfoDao videoInfoDao;
  private final LiveData<List<VideoInfo>> liveDataOfVideoList;

  public VideoInfoRepository(Context context) {
    VideoRoomDatabase db = VideoRoomDatabase.getDataBase(context);
    videoInfoDao = db.videoInfoDao();
    liveDataOfVideoList = videoInfoDao.getListByAlphabetizedInTittle();
  }

  public LiveData<List<VideoInfo>> getLiveDataOfVideoList() {
    return liveDataOfVideoList;
  }

  public void insert(VideoInfo videoInfo) {
    VideoRoomDatabase.databaseWriteExecutor.execute(() -> videoInfoDao.insert(videoInfo));
  }
  
  public Factory<Integer, VideoInfo> liveDataOfPagedList(){
    return videoInfoDao.pageSizeVideo();
  }
}
