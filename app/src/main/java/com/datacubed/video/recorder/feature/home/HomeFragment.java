package com.datacubed.video.recorder.feature.home;

import static com.datacubed.video.recorder.util.Constant.DEFAULT_DURATION;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.datacubed.video.recorder.R;
import com.datacubed.video.recorder.data.VideoInfo;
import com.datacubed.video.recorder.data.VideoInfoRepository;
import com.datacubed.video.recorder.feature.camera.CameraActivity;
import com.datacubed.video.recorder.util.Constant;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;

public class HomeFragment extends Fragment {

  private static final int REQUEST_CODE_CAMERA_ACTIVITY = 100;
  private TextInputEditText etTitle;
  private TextInputLayout textInputLayout;
  private Slider slider;
  private int duration = DEFAULT_DURATION;
  private Callback callback;

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_record, container, false);
    etTitle = root.findViewById(R.id.title_input_et);
    textInputLayout = root.findViewById(R.id.text_input_layout);
    root.findViewById(R.id.record_button).setOnClickListener(this::onRecordButtonClicked);
    slider = root.findViewById(R.id.slider);
    return root;
  }

  private void onRecordButtonClicked(View view) {
    String titleStr = Objects.requireNonNull(etTitle.getText()).toString();
//    if (TextUtils.isEmpty(titleStr)) {
//      textInputLayout.setError("Title can't be empty");
//      return;
//    }
    startCameraActivity(titleStr);
  }

  private void startCameraActivity(String titleStr) {
    Intent intent = new Intent(getContext(), CameraActivity.class);
    duration = (int) slider.getValue();
    intent.putExtra(Constant.TAG_TITLE, titleStr);
    intent.putExtra(Constant.TAG_DURATION, duration);
    startActivityForResult(intent, REQUEST_CODE_CAMERA_ACTIVITY);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_CAMERA_ACTIVITY && resultCode == Activity.RESULT_OK) {
      onVideoRecordedSuccess(data);
    }
  }

  private void onVideoRecordedSuccess(Intent data) {
    String absolutePath = data.getStringExtra(Constant.TAG_ABS_PATH);
    long createdTime = data.getLongExtra(Constant.TAG_CREATED_TIME, System.currentTimeMillis());
    String title = data.getStringExtra(Constant.TAG_TITLE);
    VideoInfo videoInfo = new VideoInfo(title, duration, createdTime, absolutePath);
    new VideoInfoRepository(getContext()).insert(videoInfo);
    etTitle.setText("");
    slider.setValue(DEFAULT_DURATION);
    if (callback != null) {
      callback.onNewVideoRecorded();
    }
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  public interface Callback {
    void onNewVideoRecorded();
  }
}
