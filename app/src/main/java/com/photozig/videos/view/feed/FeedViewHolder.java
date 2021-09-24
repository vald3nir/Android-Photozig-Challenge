package com.photozig.videos.view.feed;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photozig.videos.R;
import com.photozig.videos.model.Video;
import com.photozig.videos.interfaces.ISelectVideoListener;
import com.squareup.picasso.Picasso;

public class FeedViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleTextview;
    private final ImageView backgroundImageview;
    private final ISelectVideoListener listener;

    public FeedViewHolder(@NonNull View itemView, ISelectVideoListener listener) {
        super(itemView);
        titleTextview = itemView.findViewById(R.id.title_textView);
        backgroundImageview = itemView.findViewById(R.id.background_imageView);
        this.listener = listener;
    }

    void bind(String baseURL, Video video) {

        if (video != null && baseURL != null) {
            titleTextview.setText(video.getName());
            Picasso.get().load(baseURL + "/" + video.getImagePath()).into(backgroundImageview);
        }

        backgroundImageview.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectVideo(video);
            }
        });
    }

}
