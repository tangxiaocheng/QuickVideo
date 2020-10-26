package com.datacubed.video.recorder.feature.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.datacubed.video.recorder.R;
import com.datacubed.video.recorder.feature.home.HomeFragment;
import com.datacubed.video.recorder.feature.home.HomeFragment.Callback;
import com.datacubed.video.recorder.feature.video.list.SavedRecordingsFragment;
import org.jetbrains.annotations.NotNull;

public class MainAdapter extends FragmentStateAdapter {

  private final Callback callback;

  public MainAdapter(FragmentActivity fa, Callback callback) {
    super(fa);
    this.callback = callback;
  }

  public static final int[] IDS_MENU = {R.id.navigation_record_fragment,
      R.id.navigation_saved_recordings_fragment};


  @NotNull
  private Fragment homeFragment() {
    HomeFragment recordFragment = new HomeFragment();
    recordFragment.setCallback(callback);
    return recordFragment;
  }


  @NonNull
  @Override
  public Fragment createFragment(int position) {
    if (position == 0) {
      return homeFragment();
    } else {
      return new SavedRecordingsFragment();
    }
  }

  @Override
  public int getItemCount() {
    return 2;
  }
}
