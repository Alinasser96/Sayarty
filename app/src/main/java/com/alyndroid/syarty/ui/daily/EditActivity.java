package com.alyndroid.syarty.ui.daily;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.pojo.CameraPreview;
import com.alyndroid.syarty.ui.base.BaseActivity;
import com.alyndroid.syarty.util.CommonUtils;
import com.alyndroid.syarty.util.Constant;

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

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class EditActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_button)
    Button editButton;
    @BindView(R.id.hint_tv)
    TextView hintTv;
    @BindView(R.id.capture_button)
    Button captureButton;
    @BindView(R.id.camera_frame)
    ImageView cameraFrame;

    private boolean isCaptured;
    private File scaledDownCapturedImageFile;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.edit));
        captureButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        isCaptured = false;
        switch ((int) getIntent().getExtras().get(Constant.INTENT_EXTRAS.SIDE_NUMBER)) {
            case Constant.permission.right_side_IMAGE:
                hintTv.setText(getString(R.string.right_side_hint));
                cameraFrame.setImageResource(R.drawable.truck_04);
                break;
            case Constant.permission.back_side_IMAGE:
                hintTv.setText(getString(R.string.back_side_hint));
                cameraFrame.setImageResource(R.drawable.truck_02);
                break;
            case Constant.permission.left_side_IMAGE:
                hintTv.setText(getString(R.string.left_side_hint));
                cameraFrame.setImageResource(R.drawable.truck_01);
                break;
            case Constant.permission.front_side_IMAGE:
                hintTv.setText(getString(R.string.front_side_hint));
                cameraFrame.setImageResource(R.drawable.truck_03);
                break;
        }
    }

    @Override
    protected void onResume() {
        checkPermission();
        editButton.setVisibility(View.GONE);
        isCaptured = false;
        captureButton.setText(getString(R.string.capture));
        super.onResume();
    }

    private void checkPermission() {


        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, "Storage access needed to manage the picture.", Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.permission.STORAGE);
        } else { // We already have permissions, so handle as normal


            mCamera = Camera.open(0);
            // Create our Preview view and set it as the content of our activity.
            FrameLayout preview = findViewById(R.id.camera_preview);
            CameraPreview mPreview = new CameraPreview(this, mCamera);
            preview.addView(mPreview);
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
                    // permission was granted, yay!
//                    capturedImageFile = CommonUtils.takePicture(this, Constant.permission.right_side_IMAGE);
                    // Create an instance of Camera
                    mCamera = Camera.open(0);
                    // Create our Preview view and set it as the content of our activity.
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


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = CommonUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("cap", "Error creating media file, check storage permissions");
                return;
            }

            try {

//
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Uri uri = Uri.fromFile(pictureFile);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                scaledDownCapturedImageFile = CommonUtils.scaleDown(pictureFile, 300, false, bitmap);
                isCaptured = true;
                captureButton.setText(getString(R.string.next_step));
                editButton.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                Log.d("cap", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("cap", "Error accessing file: " + e.getMessage());
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capture_button:
                if (isCaptured) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(Constant.INTENT_EXTRAS.EDITED_PHOTO, scaledDownCapturedImageFile);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    mCamera.takePicture(null, null, mPicture);

                }
                break;

            case R.id.edit_button:
                checkPermission();
                editButton.setVisibility(View.GONE);
                isCaptured = false;
                captureButton.setText(getString(R.string.capture));

        }
    }


}
