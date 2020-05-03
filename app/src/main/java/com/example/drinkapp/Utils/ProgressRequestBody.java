package com.example.drinkapp.Utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {

    private File file;
    private  static  final int DEFAULT_SIZE=4233;
    private UploadCallBack listener;

    public ProgressRequestBody(File file, UploadCallBack listener) {
        this.file = file;
        this.listener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse("image/*");
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = file.length();
        byte[] buffer = new byte[DEFAULT_SIZE];
        FileInputStream in = new FileInputStream(file);
        long upload = 0;
        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read=in.read(buffer))!=-1)
            {
                handler.post(new ProgressUpload(upload,fileLength));
                upload+=read;
                sink.write(buffer,0,read);

            }

        }finally {
            in.close();
        }

    }

    private class ProgressUpload implements Runnable {
        private long upload,fileLength;
        public ProgressUpload(long upload, long fileLength) {
            this.fileLength=fileLength;
            this.upload=upload;
        }

        @Override
        public void run() {
            listener.onProgressUpdate((int)(100*upload/fileLength));
        }
    }
}
