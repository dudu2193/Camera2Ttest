package com.xy.hellonewworld;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Surface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XY on 2018/4/1.
 */

public class CameraManager {
    private static CameraManager mInstence;
    private android.hardware.camera2.CameraManager mManager;
    private Context mConetxt;
    private String[] mDeviceList;
    private String mCurrentDeviceId = "";
    private Handler handler = new Handler();
    private CameraDevice mCameraDevcie;
    private Surface mDisplaySurface;
    private Surface mCaptureSurface;

    private CameraCaptureSession mCurSession;
    //private CaptureRequest mRequest;

    private CameraManager(Context context) throws Exception {
        mConetxt = context;
        mManager = (android.hardware.camera2.CameraManager) mConetxt
                .getSystemService(Context.CAMERA_SERVICE);

        try {
            mDeviceList = mManager.getCameraIdList();
            if (mDeviceList != null && mDeviceList.length != 0) {
                mCurrentDeviceId = mDeviceList[0];
            } else {
                throw new Exception("no device, init failed");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    public static CameraManager getInstence(Context context) throws Exception {
        if (mInstence == null) {
            mInstence = new CameraManager(context);
        }
        return mInstence;
    }

    public void init(Surface surface){
        mDisplaySurface = surface;
        openDevice();
    }

    private void openDevice() {
        if (ActivityCompat.checkSelfPermission(mConetxt, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            LogUtils.e("Permission check failed.");
            return;
        }
        try {
            mManager.openCamera(mCurrentDeviceId, mDevcieCallBack, handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void onCameraOpen() throws Exception{

        if(mDisplaySurface == null) throw new Exception("display surface is empty require a new one");


        List<Surface> list = new ArrayList<>();
        list.add(mDisplaySurface);
        mCameraDevcie.createCaptureSession(list,mStateCallback,handler);
    }

    private CaptureRequest getNewRequest (CameraDevice device ) throws Exception{
        //TODO： Expend args.
        CaptureRequest.Builder builder = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        builder.addTarget(mDisplaySurface);
        builder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO);
        CaptureRequest request  = builder.build();
        return request;
    }
    //private

    private CameraDevice.StateCallback mDevcieCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            LogUtils.d("onOpen successfully");
            mCameraDevcie = cameraDevice;
            try {
                onCameraOpen();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            LogUtils.d("onDisconnected");
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            LogUtils.e("onError error code: "+i);
        }
    };

    private CameraCaptureSession.StateCallback mStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
            LogUtils.d("onConfigured");
            mCurSession = cameraCaptureSession;
            try {
                //mCurSession.prepare(mDisplaySurface);
                mCurSession.setRepeatingRequest(getNewRequest(mCameraDevcie),mCaptureCallback,handler);
            } catch (Exception e) {
                LogUtils.e(e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
            LogUtils.d("onConfigureFailed");
        }
    };

    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
            LogUtils.d("onCaptureStarted");
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            LogUtils.e("onCaptureFailed");
        }
    };


}
