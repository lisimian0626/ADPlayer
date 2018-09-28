package com.example.administrator.myapplication.http.download;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}