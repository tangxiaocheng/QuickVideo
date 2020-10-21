package com.datacubed.video.recorder.feature.video.list;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.datacubed.video.recorder.R;
import com.datacubed.video.recorder.util.Constant;
import timber.log.Timber;

public class VideoPlayActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_play);
    String absolutePath = getIntent().getStringExtra(Constant.TAG_ABS_PATH);
    Timber.i("ABS:%s", absolutePath);
    VideoView simpleVideoView = findViewById(R.id.simpleVideoView);
    simpleVideoView.setVideoURI(Uri.parse(absolutePath));
    MediaController mediaController = new MediaController(
        this);
    mediaController.setAnchorView(simpleVideoView);
    simpleVideoView.setMediaController(mediaController);
    simpleVideoView.start();
  }
}