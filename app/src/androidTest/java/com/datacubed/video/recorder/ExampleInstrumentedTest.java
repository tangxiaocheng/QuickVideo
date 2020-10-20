package com.datacubed.video.recorder;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.util.Log;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.datacubed.video.recorder.network.GitHubService;
import com.datacubed.video.recorder.network.Repo;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

  @Test
  public void useAppContext() {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    assertEquals("com.datacubed.video.recorder", appContext.getPackageName());
  }
  @Test
  public void retrofit(){
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .build();
    GitHubService service = retrofit.create(GitHubService.class);
    Call<List<Repo>> call = service.listRepos("octocat");
    call.enqueue(new Callback<List<Repo>>() {
      @Override
      public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
        Log.i("TAG", response.toString());
      }

      @Override
      public void onFailure(Call<List<Repo>> call, Throwable t) {

      }
    });
  }
}
