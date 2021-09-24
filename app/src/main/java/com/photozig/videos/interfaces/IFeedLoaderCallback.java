package com.photozig.videos.interfaces;

import com.photozig.videos.model.Feed;

public interface IFeedLoaderCallback {
    void onSuccess(Feed feed);

    void onFailure(Throwable e);
}
