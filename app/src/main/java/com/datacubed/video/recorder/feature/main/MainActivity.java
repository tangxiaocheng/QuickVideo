package com.datacubed.video.recorder.feature.main;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.datacubed.video.recorder.R;
import com.datacubed.video.recorder.feature.home.HomeFragment.Callback;
import com.datacubed.video.recorder.util.Constant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements OnPageChangeListener, Callback {

  private BottomNavigationView navigation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    navigation = findViewById(R.id.nav_view);
    ViewPager viewPager = findViewById(R.id.host_view_pager);
    viewPager.addOnPageChangeListener(this);
    viewPager.setAdapter(new MainAdapter(getSupportFragmentManager(), this));
    ActionBar actionBar = getSupportActionBar();
    navigation.setOnNavigationItemSelectedListener(
        new BottomNavItemSelectedListener(viewPager, actionBar));
  }

  @Override
  public void onPageSelected(int position) {
    navigation.setSelectedItemId(MainAdapter.mapToMenuId(position));
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
  }

  @Override
  public void onPageScrollStateChanged(int state) {
  }

  @Override
  public void onNewVideoRecorded() {
    navigation.setSelectedItemId(MainAdapter.mapToMenuId(Constant.VIDEO_LIST));
  }
}
