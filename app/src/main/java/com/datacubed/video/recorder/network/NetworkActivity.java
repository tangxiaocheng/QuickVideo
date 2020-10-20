package com.datacubed.video.recorder.network;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.datacubed.video.recorder.R;
import java.util.List;
import retrofit.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NetworkActivity extends AppCompatActivity {

  Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("https://api.github.com/")
//      .addConverterFactory(GsonConverterFactory.create())
      .build();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_network);



  }
}