package com.datacubed.video.recorder.network;

import androidx.annotation.NonNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class Network {

  private static final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("https://api.github.com/")
      .addConverterFactory(GsonConverterFactory.create())
      .build();

  private void test() {

    GitHubService gitHubService = retrofit.create(GitHubService.class);
    Call<List<Repo>> call = gitHubService.listRepos("tangxiaocheng");
    call.enqueue(new Callback<List<Repo>>() {
      @Override
      public void onResponse(@NonNull Call<List<Repo>> call,
          @NonNull Response<List<Repo>> response) {
        List<Repo> list = response.body();
        Timber.i("onResponse: %s", list);
      }

      @Override
      public void onFailure(@NonNull Call<List<Repo>> call, @NonNull Throwable t) {

      }
    });
  }
}
