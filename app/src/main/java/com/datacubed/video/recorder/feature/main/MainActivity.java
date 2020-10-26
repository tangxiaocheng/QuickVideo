package com.datacubed.video.recorder.feature.main;

import static com.datacubed.video.recorder.feature.main.MainAdapter.IDS_MENU;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;
import com.datacubed.video.recorder.R;
import com.datacubed.video.recorder.feature.home.HomeFragment.Callback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements Callback {

  private BottomNavigationView navigation;
  final OnPageChangeCallback onPageChangeCallback = new OnPageChangeCallback() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
      super.onPageSelected(position);
      navigation.setSelectedItemId(IDS_MENU[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
      super.onPageScrollStateChanged(state);
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    navigation = findViewById(R.id.nav_view);
    ViewPager2 viewPager = findViewById(R.id.host_view_pager);
    viewPager.setAdapter(new MainAdapter(MainActivity.this, this));
    viewPager.registerOnPageChangeCallback(onPageChangeCallback);
    ActionBar actionBar = getSupportActionBar();
    navigation.setOnNavigationItemSelectedListener(
        new BottomNavItemSelectedListener(viewPager, actionBar));
  }

  @Override
  public void onNewVideoRecorded(int pos) {
    navigation.setSelectedItemId(IDS_MENU[pos]);
  }
}
