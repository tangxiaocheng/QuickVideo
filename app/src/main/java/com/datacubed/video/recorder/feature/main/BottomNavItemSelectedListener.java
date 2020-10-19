package com.datacubed.video.recorder.feature.main;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.ViewPager;
import com.datacubed.video.recorder.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavItemSelectedListener
    implements BottomNavigationView.OnNavigationItemSelectedListener {

  private final ActionBar actionBar;
  private final ViewPager viewPager;

  public BottomNavItemSelectedListener(ViewPager viewPager, ActionBar actionBar) {
    this.actionBar = actionBar;
    this.viewPager = viewPager;
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    actionBar.setTitle(item.getTitle());
    int itemId = item.getItemId();
    if (itemId == R.id.navigation_record_fragment) {
      viewPager.setCurrentItem(0);
      return true;
    } else if (itemId == R.id.navigation_saved_recordings_fragment) {
      viewPager.setCurrentItem(1);
      return true;
    }
    return false;
  }
}
