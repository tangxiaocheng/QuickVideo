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

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class PermissionUtil {

  public static boolean granted(int[] grantResults) {
    boolean granted = true;
    for (int result : grantResults) {
      if (result != PackageManager.PERMISSION_GRANTED) {
        granted = false;
        break;
      }
    }
    return granted;
  }

  public static boolean hasPermissionsGranted(String[] permissions, Context context) {
    for (String permission : permissions) {
      if (ActivityCompat.checkSelfPermission(context, permission)
          != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  /**
   * Gets whether you should show UI with rationale for requesting permissions.
   *
   * @param permissions The permissions your app wants to request.
   * @param context     context
   * @return Whether you can show permission rationale UI.
   */
  public static boolean shouldShowRequestPermissionRationale(
      @NonNull String[] permissions, Context context) {
    for (String permission : permissions) {
      if (ActivityCompat.checkSelfPermission(context, permission)
          != PackageManager.PERMISSION_GRANTED) {
        return true;
      }
    }
    return false;
  }
}
