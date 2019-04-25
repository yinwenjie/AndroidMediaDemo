package com.example.yinwenjie.androidmediademo.activity;

import com.example.yinwenjie.androidmediademo.R;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import utils.UriPath;

public class VideoViewPlayerActivity extends AppCompatActivity {
    private static final String TAG = "VideoViewPlayerActivity";

    private String mVideoFilePath;

    private VideoView mVideoView;
    private Button mPlayButton;
    private Button mPauseButton;
    private Button mResumeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_player);
        setTitle("VideoView Player");

        mVideoView = findViewById(R.id.video_view);

        mPlayButton = findViewById(R.id.play);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mVideoView.isPlaying()) {
                    mVideoView.start();
                }
            }
        });
        mPauseButton = findViewById(R.id.pause);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                }
            }
        });
        mResumeButton = findViewById(R.id.resume);
        mResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVideoView.isPlaying()) {
                    mVideoView.resume();
                }
            }
        });

        chooseVideoFile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mVideoFilePath = UriPath.getPath(this, data.getData());
            Log.d(TAG, "Selected file:" + mVideoFilePath);
            mVideoView.setVideoPath(mVideoFilePath);
        }
    }

    private void chooseVideoFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "选择视频"), 0);
    }
}
