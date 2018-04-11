package com.xy.hellonewworld;

import android.Manifest;
import android.app.Activity;
import android.hardware.camera2.CameraDevice;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2018/4/10.
 */

public class MainActivity extends Activity {
    private CameraManager mManager;
    SurfaceView surfaceView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surface);
        requestPermissions(new String[]{Manifest.permission.CAMERA},1);
        try {
            mManager = CameraManager.getInstence(this);
            mManager.init(surfaceView.getHolder().getSurface());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mManager.init(surfaceView.getHolder().getSurface());
    }
}
