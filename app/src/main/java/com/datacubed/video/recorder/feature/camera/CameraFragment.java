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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.datacubed.video.recorder.R;
import com.datacubed.video.recorder.util.CameraUtil;
import com.datacubed.video.recorder.util.Constant;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import timber.log.Timber;

/**
 * This class manages all logic related to camera.When this fragment is attached, it means that the
 * camera permission is granted and all is set, so in this class, we focus on managing the camera.
 */
public class CameraFragment extends Fragment {

  /**
   * A {@link Semaphore} to prevent the app from exiting before closing the camera.
   */
  private final Semaphore semaphore = new Semaphore(1);
  /**
   * An {@link AutoFitTextureView} for camera preview.
   */
  private AutoFitTextureView autoFitTextureView;
  /**
   * A reference to the opened {@link CameraDevice}.
   */
  private CameraDevice cameraDevice;
  /**
   * A reference to the current {@link CameraCaptureSession} for preview.
   */
  private CameraCaptureSession previewSession;
  /**
   * The {@link Size} of camera preview.
   */
  private Size previewSize;
  /**
   * The {@link Size} of video recording.
   */
  private Size videoSize;
  /**
   * MediaRecorder
   */
  private MediaRecorder mediaRecorder;
  /**
   * Whether the app is recording video now
   */
  private boolean isRecording;
  /**
   * An additional thread for running tasks that shouldn't block the UI.
   */
  private HandlerThread handlerThread;
  /**
   * A {@link Handler} for running tasks in the background.
   */
  private Handler backgroundHandler;

  private CountDownTimer countDownTimer;

  private Integer sensorOrientation;
  private String nextVideoAbsolutePath;
  private CaptureRequest.Builder previewBuilder;

  /**
   * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a {@link
   * TextureView}.
   */
  private final TextureView.SurfaceTextureListener surfaceTextureListener =
      new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(
            SurfaceTexture surfaceTexture, int width, int height) {
          openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(
            SurfaceTexture surfaceTexture, int width, int height) {
          configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
          return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }
      };

  private int duration = DEFAULT_DURATION;
  private long createdTime;
  /**
   * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its status.
   */
  private final CameraDevice.StateCallback mStateCallback =
      new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
          CameraFragment.this.cameraDevice = cameraDevice;
          startPreview();
          semaphore.release();
          if (null != autoFitTextureView) {
            configureTransform(autoFitTextureView.getWidth(), autoFitTextureView.getHeight());
          }
          // auto start recording after the camera is opening.
          prepareRecordingVideo();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
          semaphore.release();
          cameraDevice.close();
          CameraFragment.this.cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
          semaphore.release();
          cameraDevice.close();
          CameraFragment.this.cameraDevice = null;
          Activity activity = requireActivity();
          activity.finish();
        }
      };

  private String title;

  public static CameraFragment newInstance(int duration, String title) {
    Bundle args = new Bundle();
    args.putInt(TAG_DURATION, duration);
    args.putString(TAG_TITLE, title);
    CameraFragment fragment = new CameraFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
    this.autoFitTextureView = rootView.findViewById(R.id.texture);
    return rootView;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initArguments();
    initCountdownTimer();
    // lock the orientation when starting shooting.
    requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
  }

  private void initArguments() {
    Bundle arguments = getArguments();
    if (arguments == null) {
      throw new IllegalArgumentException("duration and title must be provided");
    }
    duration = arguments.getInt(TAG_DURATION, duration);
    title = arguments.getString(TAG_TITLE);
  }

  @Override
  public void onResume() {
    super.onResume();
    startBackgroundThread();
    if (autoFitTextureView.isAvailable()) {
      openCamera(autoFitTextureView.getWidth(), autoFitTextureView.getHeight());
    } else {
      autoFitTextureView.setSurfaceTextureListener(surfaceTextureListener);
    }
  }

  private void initCountdownTimer() {
    countDownTimer =
        new CountDownTimer(duration * 1000L, 1000L) {
          @Override
          public void onTick(long millisUntilFinished) {
            if (callback != null) {
              callback.updateTimer(millisUntilFinished);
            }
          }

          @Override
          public void onFinish() {
            stopRecordingVideo();
            Intent intent = new Intent();
            intent.putExtra(Constant.TAG_ABS_PATH, nextVideoAbsolutePath);
            intent.putExtra(Constant.TAG_CREATED_TIME, createdTime);
            intent.putExtra(Constant.TAG_TITLE, title);
            requireActivity().setResult(Activity.RESULT_OK, intent);
            requireActivity().finish();
          }
        };
  }

  @Override
  public void onPause() {
    closeCamera();
    stopBackgroundThread();
    countDownTimer.cancel();
    super.onPause();
    //    potential solution for close previewSession NullPointException
    //
    // https://stackoverflow.com/questions/42510285/java-lang-illegalstateexception-session-has-been-closed-further-changes-are-il
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  //   original button for recording video
  //  @Override
  //  public void onClick(View view) {
  //    if (view.getId() == R.id.tv_timer) {
  //      if (isRecording) {
  //        stopRecordingVideo();
  //      } else {
  //        prepareRecordingVideo();
  //      }
  //    }
  //  }

  /**
   * Starts a background thread and its {@link Handler}.
   */
  private void startBackgroundThread() {
    handlerThread = new HandlerThread("CameraBackground");
    handlerThread.start();
    backgroundHandler = new Handler(handlerThread.getLooper());
  }

  /**
   * Stops the background thread and its {@link Handler}.
   */
  private void stopBackgroundThread() {
    handlerThread.quitSafely();
    try {
      handlerThread.join();
      handlerThread = null;
      backgroundHandler = null;
    } catch (InterruptedException e) {
      Timber.e(e);
    }
  }

  /**
   * Tries to open a {@link CameraDevice}. The result is listened by `mStateCallback`.
   */
  @SuppressWarnings("MissingPermission")
  private void openCamera(int width, int height) {
    CameraManager manager =
        (CameraManager) requireActivity().getSystemService(Context.CAMERA_SERVICE);
    try {
      Timber.d("tryAcquire");
      if (!semaphore.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
        throw new RuntimeException("Time out waiting to lock camera opening.");
      }
      assert manager != null;
      String cameraId = manager.getCameraIdList()[0];

      // Choose the sizes for camera preview and video recording
      CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
      StreamConfigurationMap map =
          characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
      sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
      if (map == null) {
        throw new RuntimeException("Cannot get available preview/video sizes");
      }
      videoSize = CameraUtil.chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
      previewSize =
          CameraUtil.chooseOptimalSize(
              map.getOutputSizes(SurfaceTexture.class), width, height, videoSize);

      int orientation = getResources().getConfiguration().orientation;
      if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        autoFitTextureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
      } else {
        autoFitTextureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
      }
      configureTransform(width, height);
      mediaRecorder = new MediaRecorder();
      manager.openCamera(cameraId, mStateCallback, null);

    } catch (CameraAccessException e) {
      Toast.makeText(requireActivity(), "Cannot access the camera.", Toast.LENGTH_SHORT).show();
      requireActivity().finish();
    } catch (InterruptedException e) {
      throw new RuntimeException("Interrupted while trying to lock camera opening.");
    }
  }

  private void closeCamera() {
    try {
      semaphore.acquire();
      closePreviewSession();
      if (null != cameraDevice) {
        cameraDevice.close();
        cameraDevice = null;
      }
      if (null != mediaRecorder) {
        mediaRecorder.release();
        mediaRecorder = null;
      }
    } catch (InterruptedException e) {
      throw new RuntimeException("Interrupted while trying to lock camera closing.");
    } finally {
      semaphore.release();
    }
  }

  /**
   * Start the camera preview.
   */
  private void startPreview() {
    if (null == cameraDevice || !autoFitTextureView.isAvailable() || null == previewSize) {
      return;
    }
    try {
      closePreviewSession();
      SurfaceTexture texture = autoFitTextureView.getSurfaceTexture();
      assert texture != null;
      texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
      previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

      Surface previewSurface = new Surface(texture);
      previewBuilder.addTarget(previewSurface);

      cameraDevice.createCaptureSession(
          Collections.singletonList(previewSurface),
          new CameraCaptureSession.StateCallback() {

            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
              previewSession = session;
              updatePreview();
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
              Activity activity = requireActivity();
              Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
            }
          },
          backgroundHandler);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * Update the camera preview. {@link #startPreview()} needs to be called in advance.
   */
  private void updatePreview() {
    if (null == cameraDevice) {
      return;
    }
    try {
      setUpCaptureRequestBuilder(previewBuilder);
      HandlerThread thread = new HandlerThread("CameraPreview");
      thread.start();
      if (previewSession != null) {
        previewSession.setRepeatingRequest(previewBuilder.build(), null, backgroundHandler);
      }
    } catch (CameraAccessException e) {
      e.printStackTrace();
    } catch (IllegalStateException e) {
      Timber.e(e);
    }
  }

  private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
    builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
  }

  /**
   * Configures the necessary {@link Matrix} transformation to `mTextureView`. This method should
   * not to be called until the camera preview size is determined in openCamera, or until the size
   * of `mTextureView` is fixed.
   *
   * @param viewWidth  The width of `mTextureView`
   * @param viewHeight The height of `mTextureView`
   */
  private void configureTransform(int viewWidth, int viewHeight) {
    Activity activity = requireActivity();
    if (null == autoFitTextureView || null == previewSize) {
      return;
    }
    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    Matrix matrix = new Matrix();
    RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
    RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
    float centerX = viewRect.centerX();
    float centerY = viewRect.centerY();
    if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
      bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
      matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
      float scale =
          Math.max(
              (float) viewHeight / previewSize.getHeight(),
              (float) viewWidth / previewSize.getWidth());
      matrix.postScale(scale, scale, centerX, centerY);
      matrix.postRotate(90 * (rotation - 2), centerX, centerY);
    }
    autoFitTextureView.setTransform(matrix);
  }

  private void setUpMediaRecorder() throws IOException {
    final Activity activity = requireActivity();

    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
    if (nextVideoAbsolutePath == null || nextVideoAbsolutePath.isEmpty()) {
      nextVideoAbsolutePath = getVideoFilePath(requireActivity());
    }
    mediaRecorder.setOutputFile(nextVideoAbsolutePath);
    mediaRecorder.setVideoEncodingBitRate(10000000);
    mediaRecorder.setVideoFrameRate(30);
    mediaRecorder.setVideoSize(videoSize.getWidth(), videoSize.getHeight());
    mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    switch (sensorOrientation) {
      case Constant.SENSOR_ORIENTATION_DEFAULT_DEGREES:
        mediaRecorder.setOrientationHint(Constant.DEFAULT_ORIENTATIONS.get(rotation));
        break;
      case Constant.SENSOR_ORIENTATION_INVERSE_DEGREES:
        mediaRecorder.setOrientationHint(Constant.INVERSE_ORIENTATIONS.get(rotation));
        break;
    }
    mediaRecorder.prepare();
  }

  private String getVideoFilePath(Context context) {
    final File dir = context.getExternalFilesDir(null);
    createdTime = System.currentTimeMillis();
    return (dir == null ? "" : (dir.getAbsolutePath() + "/")) + createdTime + ".mp4";
  }

  private void prepareRecordingVideo() {
    if (null == cameraDevice || !autoFitTextureView.isAvailable() || null == previewSize) {
      return;
    }
    try {
      closePreviewSession();
      setUpMediaRecorder();
      SurfaceTexture texture = autoFitTextureView.getSurfaceTexture();
      assert texture != null;
      texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
      previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
      List<Surface> surfaces = new ArrayList<>();

      // Set up Surface for the camera preview
      Surface previewSurface = new Surface(texture);
      surfaces.add(previewSurface);
      previewBuilder.addTarget(previewSurface);

      // Set up Surface for the MediaRecorder
      Surface recorderSurface = mediaRecorder.getSurface();
      surfaces.add(recorderSurface);
      previewBuilder.addTarget(recorderSurface);

      // Start a capture session
      // Once the session starts, we can update the UI and start recording
      cameraDevice.createCaptureSession(surfaces, new CameraStateCallback(), backgroundHandler);
    } catch (CameraAccessException | IOException e) {
      e.printStackTrace();
    }
  }

  private void onCameraConfigFailed() {
    Activity activity = requireActivity();
    countDownTimer.cancel();
    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
  }

  private void onCameraConfigureReady(@NonNull CameraCaptureSession cameraCaptureSession) {
    previewSession = cameraCaptureSession;
    updatePreview();
    requireActivity().runOnUiThread(this::startRecording);
  }

  private void startRecording() {
    isRecording = true;
    mediaRecorder.start();
    countDownTimer.start();
  }

  private void closePreviewSession() {
    if (previewSession != null) {
      previewSession.close();
      previewSession = null;
    }
  }

  private void stopRecordingVideo() {
    isRecording = false;
    // Stop recording
    if (mediaRecorder != null) {
      mediaRecorder.stop();
      mediaRecorder.reset();
    }
  }

  private class CameraStateCallback extends CameraCaptureSession.StateCallback {

    @Override
    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
      onCameraConfigureReady(cameraCaptureSession);
    }

    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
      onCameraConfigFailed();
    }
  }

  public interface Callback {

    void updateTimer(long millisUntilFinished);
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  private Callback callback;
}
