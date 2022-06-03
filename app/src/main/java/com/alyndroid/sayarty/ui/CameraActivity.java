package com.alyndroid.sayarty.ui;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.alyndroid.sayarty.R;
import com.alyndroid.sayarty.pojo.CameraPreview;
import com.alyndroid.sayarty.util.CommonUtils;
import com.alyndroid.sayarty.util.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraActivity extends AppCompatActivity {
    private Camera mCamera;

    @BindView(R.id.camera_frame)
    ImageView cameraFrame;
    private int sideNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        sideNumber = getIntent().getExtras().getInt("CurrentSide");

        switch (sideNumber) {
            case 1:
                cameraFrame.setImageResource(R.drawable.truck_01);
                break;
            case 2:
                cameraFrame.setImageResource(R.drawable.truck_02);
                break;
            case 3:
                cameraFrame.setImageResource(R.drawable.truck_04);
                break;
            case 4:
                cameraFrame.setImageResource(R.drawable.truck_03);
                break;
        }
    }
    @Override
    protected void onResume() {
        checkPermission();
        super.onResume();
    }


    public void openCamera() {
        mCamera.startPreview();
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = CommonUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("cap", "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",pictureFile);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            } catch (FileNotFoundException e) {
                Log.d("cap", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("cap", "Error accessing file: " + e.getMessage());
            }
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                        mCamera.takePicture(null, null, mPicture);
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    private void checkPermission() {


        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, "Storage access needed to manage the picture.", Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.permission.STORAGE);
        } else { // We already have permissions, so handle as normal
            mCamera = Camera.open(0);
            // Create our Preview view and set it as the content of our activity.
            CameraPreview mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = findViewById(R.id.camera_preview_frame);
            preview.addView(mPreview);
//            updateUi(currentSide);
            mCamera.startPreview();
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.permission.STORAGE: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for WRITE_EXTERNAL_STORAGE
                boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                boolean camera = perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                if (camera) {
                    mCamera = Camera.open(0);
                    CameraPreview mPreview = new CameraPreview(this, mCamera);
                    FrameLayout preview = findViewById(R.id.camera_preview);
                    preview.addView(mPreview);

                } else {
                    // Permission Denied
                    Toast.makeText(this, "Storage permission is needed to analyse the picture.", Toast.LENGTH_LONG).show();
                }
            }
            default:
                break;
        }
    }
}