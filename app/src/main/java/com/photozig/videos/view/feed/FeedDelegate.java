package com.photozig.videos.view.feed;

import android.Manifest;

import com.photozig.videos.R;
import com.photozig.videos.control.VideoController;
import com.photozig.videos.interfaces.IFeedLoaderCallback;
import com.photozig.videos.interfaces.IVideoDownloadCallback;
import com.photozig.videos.model.Feed;
import com.photozig.videos.model.Video;
import com.photozig.videos.view.video.VideoActivity;

public class FeedDelegate {

    private final VideoController controller = new VideoController();
    private final FeedActivity view;
    private Feed feed;
    private final int requestPermissionsCode = 1721;
    private Video video;

    public FeedDelegate(FeedActivity view) {
        this.view = view;
    }

    public void loadFeed() {
        view.showLoading(true);
        controller.loadFeed(new IFeedLoaderCallback() {
            @Override
            public void onSuccess(Feed feedParam) {
                view.showLoading(false);
                if (feedParam != null) {
                    feed = feedParam;
                    view.bindAdapter(feed.getAssetsLocation(), feed.getVideos());
                } else {
                    view.showMessage(view.getString(R.string.load_feed_fail));
                }
            }

            @Override
            public void onFailure(Throwable e) {
                view.showLoading(false);
                view.showMessage(view.getString(R.string.load_feed_fail));
                e.printStackTrace();
            }
        });
    }

    public void showVideo(Video video) {
        view.openNewScreen(VideoActivity.createIntent(view, video, feed.getAssetsLocation()));
    }

    public void downloadVideo(Video video) {
        this.video = video;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (!view.hasPermissions(PERMISSIONS)) {
            view.requestPermissions(requestPermissionsCode, PERMISSIONS);
        } else {
            view.showLoading(true);
            controller.downloadVideo(view, feed, video, new IVideoDownloadCallback() {

                @Override
                public void onDownloadComplete() {
                    view.showLoading(false);
                    showVideo(video);
                }

                @Override
                public void onDownloadError(Throwable exception) {
                    view.showLoading(false);
                    exception.printStackTrace();
                    view.showMessage(view.getString(R.string.download_video_error));
                }
            });
        }
    }

    public void onRequestPermissionsResult(int requestCode) {
        if (requestCode == requestPermissionsCode) {
            downloadVideo(video);
        }
    }
}
