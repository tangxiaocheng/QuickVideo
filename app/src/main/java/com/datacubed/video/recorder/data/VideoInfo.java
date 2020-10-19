package com.datacubed.video.recorder.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Objects;

@Entity(tableName = "video_table")
public class VideoInfo {

  String title;
  int duration;
  long createdTime;
  @NonNull
  String absolutePath;

  @PrimaryKey(autoGenerate = true)
  int id;

  public VideoInfo(String title, int duration, long createdTime, @NonNull String absolutePath) {
    this.title = title;
    this.duration = duration;
    this.createdTime = createdTime;
    this.absolutePath = absolutePath;
  }

  public @NonNull
  String getAbsolutePath() {
    return absolutePath;
  }

  public String getTitle() {
    return title;
  }

  public int getDuration() {
    return duration;
  }

  public long getCreatedTime() {
    return createdTime;
  }

  public int getId() {
    return id;
  }

  @Override
  public @NonNull
  String toString() {
    return "VideoInfo{"
        + "title='"
        + title
        + '\''
        + ", duration="
        + duration
        + ", createdTime="
        + createdTime
        + ", absPath='"
        + absolutePath
        + '\''
        + ", id="
        + id
        + '}';
  }

  /*
   * For AsyncListDiffer usage.
   * */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VideoInfo videoInfo = (VideoInfo) o;
    return id == videoInfo.id &&
        duration == videoInfo.duration &&
        createdTime == videoInfo.createdTime &&
        Objects.equals(title, videoInfo.title) &&
        absolutePath.equals(videoInfo.absolutePath);
  }

  /*
   * For this demo app, since id is the PrimaryKey, for simplicity and efficiency, we can just hash the id here.
   * */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
