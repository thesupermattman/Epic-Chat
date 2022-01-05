package com.example.epicchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback
{
    Camera camera;

    Camera.PictureCallback jpegCallback;

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;

    final int CAMERA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mSurfaceView = findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        // Setting Permissions
        if(ActivityCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
        else {
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        // Camera Click Button
        ImageButton mCapture = findViewById(R.id.capture);
        mCapture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        // Code to decode the image and begin to open display it on ShowCaptureActivity
        jpegCallback = new Camera.PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera)
            {
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                Bitmap rotateBitmap = rotate(decodedBitmap);

                String fileLocation = SaveImageToStorage(rotateBitmap);
                if(fileLocation != null)
                {
                    Intent intent = new Intent(CameraActivity.this, ShowCaptureActivity.class);
                    startActivity(intent);
                    return;
                }
            }
        };
    }

    // Method to save image to 'storage'
    public String SaveImageToStorage(Bitmap bitmap)
    {
        String fileName = "imageToSend";
        try
        {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = CameraActivity.this.openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        }catch (Exception e)
        {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }


    // Method to rotate bitmap
    private Bitmap rotate(Bitmap decodedBitmap)
    {
        int w = decodedBitmap.getWidth();
        int h = decodedBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodedBitmap, 0, 0, w, h, matrix, true);
    }


    // Method to capture image
    private void captureImage()
    {
        camera.takePicture(null, null, jpegCallback);
    }

    // Method to set camera parameters
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder)
    {
        camera = Camera.open();

        Camera.Parameters parameters;
        parameters = camera.getParameters();

        camera.setDisplayOrientation(90);
        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
        bestSize = sizeList.get(0);
        for (int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height)>(bestSize.width * bestSize.height)){
                bestSize = sizeList.get(i);
            }
        }
        parameters.setPreviewSize(bestSize.width, bestSize.height);

        camera.setParameters(parameters);

        try
        {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder)
    {

    }

    // Method to request permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case CAMERA_REQUEST_CODE:
                {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mSurfaceHolder.addCallback(this);
                    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }else{
                    Toast.makeText(CameraActivity.this, "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}

// Source of Code: https://www.youtube.com/watch?v=0yzS-1qSC64&t=161s - Make an Android App Like SNAPCHAT - Part 5 - Camera Preview Tutorial
// Source of Code: https://www.youtube.com/watch?v=CI2nZqsWA_g&t=278s - Make an Android App Like SNAPCHAT - Part 6 - Taking a Picture & Displaying the Picture
// Source of Code: https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/fragment/CameraFragment.java
// The two YouTube videos were used as a tutorial to create the camera. However, there is also a github project linked to the videos that I used.