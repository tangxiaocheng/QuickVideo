package com.datacubed.video.recorder.feature.main;

import static com.datacubed.video.recorder.feature.main.MainAdapter.IDS_MENU;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavItemSelectedListener
    implements BottomNavigationView.OnNavigationItemSelectedListener {

  private final ActionBar actionBar;
  private final ViewPager2 viewPager;

  public BottomNavItemSelectedListener(ViewPager2 viewPager, ActionBar actionBar) {
    this.actionBar = actionBar;
    this.viewPager = viewPager;
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    actionBar.setTitle(item.getTitle());
    int itemId = item.getItemId();
    for (int i = 0; i < IDS_MENU.length; i++) {
      if (itemId == IDS_MENU[i]) {
        viewPager.setCurrentItem(i);
        return true;
      }
    }
    return false;
  }
}
