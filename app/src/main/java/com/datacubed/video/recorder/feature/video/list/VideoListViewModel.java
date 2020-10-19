package com.datacubed.video.recorder.feature.video.list;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.datacubed.video.recorder.data.VideoInfo;
import com.datacubed.video.recorder.data.VideoInfoRepository;
import java.util.List;

public class VideoListViewModel extends AndroidViewModel {

  private final LiveData<List<VideoInfo>> liveDataOfVideoList;

  public VideoListViewModel(Application application) {
    super(application);
    VideoInfoRepository videoInfoRepository = new VideoInfoRepository(application);
    liveDataOfVideoList = videoInfoRepository.getLiveDataOfVideoList();
  }

  public LiveData<List<VideoInfo>> videoListLiveData() {
    return liveDataOfVideoList;
  }
}
