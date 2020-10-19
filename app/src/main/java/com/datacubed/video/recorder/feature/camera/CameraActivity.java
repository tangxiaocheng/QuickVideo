/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datacubed.video.recorder.feature.camera;

import static com.datacubed.video.recorder.util.Constant.DEFAULT_DURATION;
import static com.datacubed.video.recorder.util.Constant.TAG_DURATION;
import static com.datacubed.video.recorder.util.Constant.TAG_TITLE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.datacubed.video.recorder.R;
import com.datacubed.video.recorder.feature.camera.CameraFragment.Callback;
import com.datacubed.video.recorder.util.Constant;
import timber.log.Timber;

/**
 * Separation of concerns
 * This class mainly handles the camera permission management in a isolated environment. If we mix
 * the permission logic and camera logic together, the code could become messy.
 */
public class CameraActivity extends AppCompatActivity implements Callback {

  private TextView tvTimer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    tvTimer = findViewById(R.id.tv_timer);
    start();
  }

  /** Requests permissions needed for recording video. */
  private void start() {
    if (PermissionUtil.hasPermissionsGranted(Constant.VIDEO_PERMISSIONS, this)) {
      bindCameraFragmentToStartRecordingVideo();
    } else {
      requestPermission();
    }
  }

  private void requestPermission() {
    if (PermissionUtil.shouldShowRequestPermissionRationale(Constant.VIDEO_PERMISSIONS, this)) {
      showRationalAlertDialog();
    } else {
      ActivityCompat.requestPermissions(
          this, Constant.VIDEO_PERMISSIONS, Constant.REQUEST_CODE_VIDEO_PERMISSIONS);
    }
  }

  private void bindCameraFragmentToStartRecordingVideo() {

    Intent intent = getIntent();
    int duration = intent.getIntExtra(TAG_DURATION, DEFAULT_DURATION);
    String title = intent.getStringExtra(TAG_TITLE);
    CameraFragment fragment = CameraFragment.newInstance(duration, title);
    fragment.setCallback(this);
    commit(fragment);
  }

  private void commit(CameraFragment fragment) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fl_camera_fragment, fragment)
        .commit();
  }

  private void showRationalAlertDialog() {
    new AlertDialog.Builder(this)
        .setMessage(R.string.permission_request)
        .setPositiveButton(
            android.R.string.ok,
            (dialog, which) ->
                ActivityCompat.requestPermissions(
                    this, Constant.VIDEO_PERMISSIONS, Constant.REQUEST_CODE_VIDEO_PERMISSIONS))
        .setNegativeButton(android.R.string.cancel, (dialog, which) -> this.finish())
        .create()
        .show();
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (PermissionUtil.granted(grantResults)) {
      bindCameraFragmentToStartRecordingVideo();
    } else {
      showRationalAlertDialog();
    }
  }

  @Override
  public void updateTimer(long millisUntilFinished) {
    tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
  }

  @Override
  public void onBackPressed() {

    Fragment f = getSupportFragmentManager().findFragmentById(R.id.fl_camera_fragment);
    if (f instanceof CameraFragment) {
      // disable back button in camera activity.
      Timber.i("BACK PRESSED");
    } else {
      super.onBackPressed();
    }
  }
}
