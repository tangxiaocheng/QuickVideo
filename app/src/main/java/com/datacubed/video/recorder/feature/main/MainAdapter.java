package com.datacubed.video.recorder.feature.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.datacubed.video.recorder.R;
import com.datacubed.video.recorder.feature.home.HomeFragment;
import com.datacubed.video.recorder.feature.home.HomeFragment.Callback;
import com.datacubed.video.recorder.feature.video.list.SavedRecordingsFragment;
import com.datacubed.video.recorder.util.Constant;
import org.jetbrains.annotations.NotNull;

public class MainAdapter extends FragmentPagerAdapter {

  private final Callback callback;

  public MainAdapter(FragmentManager fm, Callback callback) {
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    this.callback = callback;
  }

  public static int mapToMenuId(int position) {
    switch (position) {
      case Constant.HOME:
        return R.id.navigation_record_fragment;
      case Constant.VIDEO_LIST:
        return R.id.navigation_saved_recordings_fragment;
      default:
        throw new IllegalStateException("Only support 2 items");
    }
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    if (position == 0) {
      return homeFragment();
    } else {
      return new SavedRecordingsFragment();
    }
  }

  @NotNull
  private Fragment homeFragment() {
    HomeFragment recordFragment = new HomeFragment();
    recordFragment.setCallback(callback);
    return recordFragment;
  }

  @Override
  public int getCount() {
    return 2;
  }
}
