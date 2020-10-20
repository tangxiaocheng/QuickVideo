package com.datacubed.video.recorder.feature.video.list;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.datacubed.video.recorder.R;
import com.datacubed.video.recorder.data.VideoInfo;
import java.util.Date;
import java.util.List;

class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

  private static final String DATE_FORMAT = "MM-dd-yyyy hh:mm:ss";
  /*
   * To improve the refresh efficiency in RecycleView adapter, here I introduced AsyncListDiffer
   * AsyncListDiffer will compare the new data set the old data set before UI refreshment.
   * It only updates the items who has difference in its data model.
   * */
  private final AsyncListDiffer<VideoInfo> asyncListDiffer = new AsyncListDiffer<>(this,
      DIFF_CALLBACK);
  private final LayoutInflater layoutInflater;

  public VideoListAdapter(Context context) {
    this.layoutInflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = layoutInflater.inflate(R.layout.saved_recordings_rv_item, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bindData(asyncListDiffer.getCurrentList().get(position));
  }

  @Override
  public int getItemCount() {

    return asyncListDiffer.getCurrentList().size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    TextView tvTitle;
    TextView tvDuration;
    TextView tvCreateTime;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvTitle = itemView.findViewById(R.id.tv_title);
      tvDuration = itemView.findViewById(R.id.tv_duration);
      tvCreateTime = itemView.findViewById(R.id.tv_created_time);
    }

    private void bindData(VideoInfo videoInfo) {
      long createdTime = videoInfo.getCreatedTime();
      this.tvCreateTime.setText(DateFormat.format(DATE_FORMAT, new Date(createdTime)));
      this.tvDuration.setText(String.valueOf(videoInfo.getDuration()));
      this.tvTitle.setText(videoInfo.getTitle());
    }
  }

  public static final DiffUtil.ItemCallback<VideoInfo> DIFF_CALLBACK
      = new DiffUtil.ItemCallback<VideoInfo>() {
    @Override
    public boolean areItemsTheSame(
        @NonNull VideoInfo oldVideoInfo, @NonNull VideoInfo newVideoInfo) {
      return oldVideoInfo.getId() == newVideoInfo.getId();
    }

    @Override
    public boolean areContentsTheSame(
        @NonNull VideoInfo oldVideoInfo, @NonNull VideoInfo newVideoInfo) {
      return oldVideoInfo.equals(newVideoInfo);
    }
  };

  public void submitList(List<VideoInfo> list) {
    asyncListDiffer.submitList(list);
  }
}