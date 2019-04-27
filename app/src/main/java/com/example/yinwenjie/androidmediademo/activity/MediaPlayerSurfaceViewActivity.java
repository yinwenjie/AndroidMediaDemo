package com.example.yinwenjie.androidmediademo.activity;
import com.example.yinwenjie.androidmediademo.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;

import java.io.IOException;

import utils.UriPath;

public class MediaPlayerSurfaceViewActivity extends AppCompatActivity
        implements SurfaceHolder.Callback, MediaController.MediaPlayerControl {
    private static final String TAG = "MediaPlayerSurfaceView";

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private MediaController mMediaController;

    private Handler mHandler = new Handler();

    private String mVideoFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player_surface_view);

        chooseVideoFile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mVideoFilePath = UriPath.getPath(this, data.getData());
            Log.d(TAG, "Selected file:" + mVideoFilePath);

            mSurfaceView = findViewById(R.id.video);
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);

            mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (mMediaController != null) {
                        mMediaController.show();
                    }
                    return false;
                }
            });

            mMediaController = new MediaController(this);
        }
    }

    private void adjustVideoSize() {
        int surfaceWidth = mSurfaceView.getWidth();
        int surfaceHeight = mSurfaceView.getHeight();
        int videoWidth = mMediaPlayer.getVideoWidth();
        int videoHeight = mMediaPlayer.getVideoHeight();
        int displayWidth = 0, displayHeight = 0;

        Log.d(TAG, "Adjust videoWidth:" + videoWidth + ", videoHeight:" + videoHeight + " to surfaceWidth:" + surfaceWidth + ", surfaceHeight:" + surfaceHeight);

        float videoRatio = (float)videoWidth/videoHeight, surfaceRatio = (float)surfaceWidth/surfaceHeight;
        if (videoRatio >= surfaceRatio) {
            displayWidth = surfaceWidth;
            displayHeight = (int) (displayWidth/videoRatio);
        } else {
            displayHeight = surfaceHeight;
            displayWidth = (int) (displayHeight * videoRatio);
        }

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(displayWidth, displayHeight);
        mSurfaceView.setLayoutParams(params);

    }

    private void chooseVideoFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "选择视频"), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    // SurfaceHolder callback:
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            if (mMediaPlayer == null) {
                // Initializing critical instances...
                mMediaPlayer = new MediaPlayer();
                if (mMediaPlayer != null) {
                    mMediaPlayer.setLooping(true);
                } else {
                    Log.e(TAG, "creating mMediaPlayer instance failed, exit.");
                    return;
                }

                mMediaPlayer.setDataSource(mVideoFilePath);
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        adjustVideoSize();
                        mMediaPlayer.start();
                    }
                });
                mMediaPlayer.prepare();

                mMediaController.setMediaPlayer(this);
                mMediaController.setAnchorView(mSurfaceView);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMediaController.setEnabled(true);
                        mMediaController.show();
                    }
                });
            } else if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
            mMediaPlayer.setDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mMediaPlayer.pause();
    }

    // MediaController callback
    @Override
    public void start() {
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        mMediaPlayer.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return mMediaPlayer.getAudioSessionId();
    }
}
