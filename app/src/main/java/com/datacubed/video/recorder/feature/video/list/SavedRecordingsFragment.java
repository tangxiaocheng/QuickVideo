package com.datacubed.video.recorder.feature.video.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.datacubed.video.recorder.R;

public class SavedRecordingsFragment extends Fragment {

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    ViewModelProvider viewModelProvider = new ViewModelProvider(this);
    View root = inflater.inflate(R.layout.fragment_saved_recordings, container, false);
    RecyclerView recyclerView = root.findViewById(R.id.saved_list_rv);

//    VideoListAdapter adapter = new VideoListAdapter(getContext());
    VideoPageListAdapter adapter = new VideoPageListAdapter(getContext());
    VideoListViewModel videoListViewModel = viewModelProvider.get(VideoListViewModel.class);
//    videoListViewModel.videoListLiveData().observe(getViewLifecycleOwner(), adapter::submitList);
    videoListViewModel.getLiveDataOfPagedList().observe(getViewLifecycleOwner(),
        pagedList -> {
          if (pagedList != null) {
            adapter.submitList(pagedList);
          }
        });
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    recyclerView.setAdapter(adapter);

   

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    VideoListViewModel videoListViewModel = viewModelProvider.get(VideoListViewModel.class);
    videoListViewModel.videoListLiveData().observe(getViewLifecycleOwner(), adapter::submitList);


    return root;
  }
}
