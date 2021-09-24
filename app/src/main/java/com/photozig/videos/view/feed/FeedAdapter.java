package com.photozig.videos.view.feed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photozig.videos.R;
import com.photozig.videos.model.Video;
import com.photozig.videos.interfaces.ISelectVideoListener;

import java.util.ArrayList;
import java.util.Collection;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private ArrayList<Video> videos = new ArrayList<>();
    private String baseURL;
    private final ISelectVideoListener listener;

    public FeedAdapter(ISelectVideoListener listener) {
        this.listener = listener;
    }

    public void bind(String baseURL, Collection<Video> videos) {
        this.videos = new ArrayList<>(videos);
        this.baseURL = baseURL;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_feed, parent, false);
        return new FeedViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder multimediaViewHolder, int position) {
        multimediaViewHolder.bind(baseURL, videos.get(position));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}
