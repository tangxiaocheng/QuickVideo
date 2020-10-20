package com.datacubed.video.recorder.feature.video.list;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.datacubed.video.recorder.data.VideoInfo;
import com.datacubed.video.recorder.data.VideoInfoRepository;

public class VideoListViewModel extends AndroidViewModel {


  //    private final LiveData<List<VideoInfo>> liveDataOfVideoList;
  private final LiveData<PagedList<VideoInfo>> liveDataOfPagedList;

  public VideoListViewModel(Application application) {
    super(application);
    VideoInfoRepository videoInfoRepository = new VideoInfoRepository(application);
//    liveDataOfVideoList = videoInfoRepository.getLiveDataOfVideoList();
    PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(false)
        .setPrefetchDistance(20)
        .setInitialLoadSizeHint(10).setPageSize(10).build();
    liveDataOfPagedList = new LivePagedListBuilder<>(videoInfoRepository.liveDataOfPagedList(),
        config).build();

  }

  public LiveData<PagedList<VideoInfo>> getLiveDataOfPagedList() {
    return liveDataOfPagedList;
  }
//  public LiveData<List<VideoInfo>> videoListLiveData() {
//    return liveDataOfVideoList;
//  }

}
