package com.datacubed.video.recorder.util;

import android.Manifest;
import android.util.SparseIntArray;
import android.view.Surface;

public class Constant {

  public static final String TAG_TITLE = "name";
  public static final String TAG_DURATION = "duration";
  public static final String TAG_ABS_PATH = "videoAbsolutePath";
  public static final String TAG_CREATED_TIME = "createdTime";
  public static final int DEFAULT_DURATION = 15;

  public static final int HOME = 0;
  public static final int VIDEO_LIST = 1;

  public static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
  public static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
  public static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
  public static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();
  public static final String TAG = "Camera2VideoFragment";
  public static final int REQUEST_CODE_VIDEO_PERMISSIONS = 1;
  public static final String[] VIDEO_PERMISSIONS = {
    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
  };

  static {
    Constant.DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
    Constant.DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
    Constant.DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
    Constant.DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
  }

  static {
    Constant.INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
    Constant.INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
    Constant.INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
    Constant.INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
  }
}
