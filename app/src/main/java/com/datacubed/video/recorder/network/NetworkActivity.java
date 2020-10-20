package com.datacubed.video.recorder.network;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.datacubed.video.recorder.R;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NetworkActivity extends AppCompatActivity {
  Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("https://api.github.com/")
      .build();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_network);

    GitHubService service = retrofit.create(GitHubService.class);
    Call<List<Repo>> call = service.listRepos("octocat");
    call.enqueue(new Callback<List<Repo>>() {
      @Override
      public void onResponse( Call<List<Repo>> call, Response<List<Repo>> response) {

      }

      @Override
      public void onFailure(Call<List<Repo>> call, Throwable t) {

      }
    });

  }
}