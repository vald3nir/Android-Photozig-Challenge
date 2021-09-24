package com.photozig.videos.control;

import android.content.Context;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.photozig.videos.AppConfig;
import com.photozig.videos.interfaces.IFeedLoaderCallback;
import com.photozig.videos.interfaces.IVideoDownloadCallback;
import com.photozig.videos.model.Feed;
import com.photozig.videos.model.Video;
import com.photozig.videos.rest.RetrofitClient;

public class VideoController {

    private final RetrofitClient client = new RetrofitClient();

    public void loadFeed(IFeedLoaderCallback listener) {
        client.getFeed(listener);
    }

    public void downloadVideo(Context context, Feed feed, Video video, IVideoDownloadCallback callback) {
        String fileName = video.getVideoPath();
        String dirPath = AppConfig.getFolderDownload(context);
        String url = feed.getAssetsLocation() + "/" + video.getVideoPath();
        PRDownloader.download(url, dirPath, fileName).build().start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                callback.onDownloadComplete();
            }

            @Override
            public void onError(Error error) {
                callback.onDownloadError(error.getConnectionException());
            }
        });

    }
}