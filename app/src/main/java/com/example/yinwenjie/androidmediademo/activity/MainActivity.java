package com.example.yinwenjie.androidmediademo.activity;

import com.example.yinwenjie.androidmediademo.R;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private boolean mPermissionExternalStorageGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity created.");
        getPermission();
    }

    public void onClickVideoViewPlayer(View v) {
        Log.d(TAG, "VideoViewPlayer clicked. WRITE_EXTERNAL_STORAGE:" + mPermissionExternalStorageGranted);
        goToActivity(VideoViewPlayerActivity.class);
    }

    public void onClickMediaPlayerSurfaceView(View view) {
        Log.d(TAG, "MediaPlayerSurfaceView clicked. WRITE_EXTERNAL_STORAGE:" + mPermissionExternalStorageGranted);
        goToActivity(MediaPlayerSurfaceViewActivity.class);
    }

    private void goToActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    // Permission Manager ...
    private void getPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            mPermissionExternalStorageGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionExternalStorageGranted = true;
                } else {
                    Toast.makeText(this, "拒绝内存读取权限将无法使用", Toast.LENGTH_SHORT);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
