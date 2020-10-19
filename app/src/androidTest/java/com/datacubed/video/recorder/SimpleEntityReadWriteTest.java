package com.datacubed.video.recorder;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.datacubed.video.recorder.data.VideoInfo;
import com.datacubed.video.recorder.data.VideoInfoDao;
import com.datacubed.video.recorder.data.VideoRoomDatabase;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {

  private VideoInfoDao userDao;
  private VideoRoomDatabase db;

  @Before
  public void createDb() {
    Context context = ApplicationProvider.getApplicationContext();
    db = Room.inMemoryDatabaseBuilder(context, VideoRoomDatabase.class).build();
    userDao = db.videoInfoDao();
  }

  @After
  public void closeDb() {
    db.close();
  }

  @Test
  public void writeUserAndReadInList() {
    VideoInfo user = new VideoInfo("randy", 120, System.currentTimeMillis(), "testPath");
    userDao.insert(user);
    List<VideoInfo> byName = userDao.getVideoList();
    assertThat(byName.get(0).getTitle(), equalTo(user.getTitle()));
  }
}