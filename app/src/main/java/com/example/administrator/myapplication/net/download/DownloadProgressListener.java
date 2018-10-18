package com.example.administrator.myapplication.net.download;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}