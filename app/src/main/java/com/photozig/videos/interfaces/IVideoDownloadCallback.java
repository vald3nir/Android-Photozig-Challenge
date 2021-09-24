package com.photozig.videos.interfaces;

public interface IVideoDownloadCallback {

    void onDownloadComplete();

    void onDownloadError(Throwable connectionException);
}