package com.datacubed.video.recorder.feature.video.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacubed.video.recorder.R;
import com.datacubed.video.recorder.data.VideoInfo;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import static com.datacubed.video.recorder.util.Constant.TAG_ABS_PATH;

class VideoPageListAdapter extends PagedListAdapter<VideoInfo, VideoPageListAdapter.ViewHolder> {

    private static final String DATE_FORMAT = "MM-dd-yyyy hh:mm:ss";

    private final LayoutInflater layoutInflater;

    public VideoPageListAdapter(Context context) {
        super(DIFF_CALLBACK);
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
        VideoInfo videoInfo = getItem(position);
        if (videoInfo != null) {
            holder.bindData(videoInfo);
        } else {
            holder.clear();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tvTitle;
        final TextView tvDuration;
        final TextView tvCreateTime;
        final ImageView iconView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvCreateTime = itemView.findViewById(R.id.tv_created_time);
            iconView = itemView.findViewById(R.id.iconIV);
        }

        private void bindData(VideoInfo videoInfo) {
            long createdTime = videoInfo.getCreatedTime();
            this.tvCreateTime.setText(DateFormat.format(DATE_FORMAT, new Date(createdTime)));
            this.tvDuration.setText(String.valueOf(videoInfo.getDuration()));
            this.tvTitle.setText(videoInfo.getTitle());
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoInfo.getAbsolutePath(),
                    MediaStore.Video.Thumbnails.MICRO_KIND);
            this.iconView.setImageBitmap(bMap);
            itemView.setOnClickListener(view -> goToPlayVideoActivity(videoInfo));
        }

        private void goToPlayVideoActivity(VideoInfo info) {
            Intent intent = new Intent(itemView.getContext(), VideoPlayActivity.class);
            intent.putExtra(TAG_ABS_PATH, info.getAbsolutePath());
            itemView.getContext().startActivity(intent);
        }

        void clear() {
            itemView.invalidate();
            tvTitle.invalidate();
            tvDuration.invalidate();
            tvCreateTime.invalidate();
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
}
