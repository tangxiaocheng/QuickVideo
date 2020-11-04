package com.datacubed.video.recorder.feature.video.list;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.datacubed.video.recorder.data.VideoInfo;
import com.datacubed.video.recorder.data.VideoInfoRepository;

public class VideoListViewModel extends AndroidViewModel {


  private final LiveData<PagedList<VideoInfo>> liveDataOfPagedList;

  public VideoListViewModel(Application application) {
    super(application);
    VideoInfoRepository videoInfoRepository = new VideoInfoRepository(application);
    PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(true)
        .setPrefetchDistance(20)
        .setInitialLoadSizeHint(10).setPageSize(5).build();
    liveDataOfPagedList = new LivePagedListBuilder<>(videoInfoRepository.liveDataOfPagedList(),
        config).build();

  }

  public LiveData<PagedList<VideoInfo>> getLiveDataOfPagedList() {
    return liveDataOfPagedList;
  }


}
