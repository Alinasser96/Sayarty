package com.alyndroid.syarty.ui.daily;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.di.component.CommentsComponent;
import com.alyndroid.syarty.di.component.DaggerCommentsComponent;
import com.alyndroid.syarty.pojo.CameraPreview;
import com.alyndroid.syarty.pojo.Comments;
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

public class CaptureActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.hint_tv)
    TextView hintTv;
    @BindView(R.id.camera_frame)
    ImageView cameraFrame;
    @BindView(R.id.capture_button)
    Button capButton;
    @BindView(R.id.edit_button)
    Button editButton;
    @BindView(R.id.comment_et)
    EditText commentEt;
    private Camera mCamera;
    private boolean isCaptured;
    private File scaledDownCapturedImageFile, rightSideImg, backSideImg, leftSideImg, frontSideImg;
    private int currentSide;
    private Comments comments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        capButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        CommentsComponent component = DaggerCommentsComponent.create();
        comments = component.getComments();
        currentSide = 1;
    }

    @Override
    protected void onResume() {
        checkPermission();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mCamera != null) {
            mCamera.release();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (currentSide == 1) {
            super.onBackPressed();
        } else {
            updateUi(currentSide - 1);
        }
    }

    private void updateUi(int sideNumber) {
        currentSide = sideNumber;
        openCamera();
        switch (sideNumber) {
            case 1:
                setTitle(getString(R.string.right_side_title));
                hintTv.setText(getString(R.string.right_side_hint));
                commentEt.setText("");
                cameraFrame.setImageResource(R.drawable.truck_04);
                break;
            case 2:
                hintTv.setText(getString(R.string.back_side_hint));
                setTitle(getString(R.string.back_Side_title));
                commentEt.setText("");
                cameraFrame.setImageResource(R.drawable.truck_02);
                break;
            case 3:
                hintTv.setText(getString(R.string.left_side_hint));
                setTitle(getString(R.string.left_side_title));
                commentEt.setText("");
                cameraFrame.setImageResource(R.drawable.truck_01);
                break;
            case 4:
                hintTv.setText(getString(R.string.front_side_hint));
                setTitle(getString(R.string.front_Side_title));
                commentEt.setText("");
                cameraFrame.setImageResource(R.drawable.truck_03);
                break;
        }
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
            CameraPreview mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = findViewById(R.id.camera_preview);
            preview.addView(mPreview);
            updateUi(currentSide);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capture_button:
                if (isCaptured) {
                    nextStepAction();
                } else {
                    mCamera.takePicture(null, null, mPicture);
                    showProgress(getString(R.string.capture_loading));
                }
                break;

            case R.id.edit_button:
                openCamera();
                break;

        }
    }

    private void nextStepAction() {
        setComments();
        if (currentSide == 4) {
            Intent intent;
            if (getIntent().getExtras().getString(Constant.INTENT_EXTRAS.isFrom).equals(Constant.INTENT_EXTRAS.NewRequest)) {
                intent = new Intent(this, AttachedActivity.class);
            } else {
                intent = new Intent(this, ConfirmationActivity.class);
            }
            intent.putExtra(Constant.INTENT_EXTRAS.right_Side, rightSideImg);
            intent.putExtra(Constant.INTENT_EXTRAS.back_side, backSideImg);
            intent.putExtra(Constant.INTENT_EXTRAS.left_side, leftSideImg);
            intent.putExtra(Constant.INTENT_EXTRAS.front_side, frontSideImg);
            intent.putExtra(Constant.INTENT_EXTRAS.isFrom, getIntent().getExtras().getString(Constant.INTENT_EXTRAS.isFrom));
            intent.putExtra(Constant.INTENT_EXTRAS.receiverID, getIntent().getExtras().getString(Constant.INTENT_EXTRAS.receiverID));
            intent.putExtra(Constant.INTENT_EXTRAS.Operation_id, getIntent().getExtras().getString(Constant.INTENT_EXTRAS.Operation_id));
            intent.putExtra(Constant.INTENT_EXTRAS.CAR_ID, getIntent().getExtras().getString(Constant.INTENT_EXTRAS.CAR_ID));
            intent.putExtra(Constant.INTENT_EXTRAS.COMMENTS, comments);
            mCamera.release();
            startActivity(intent);

        } else {
            updateUi(currentSide + 1);
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            hideProgress();
            File pictureFile = CommonUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("cap", "Error creating media file, check storage permissions");
                return;
            }

            try {
                capButton.setText(getString(R.string.next_step));
                editButton.setVisibility(View.VISIBLE);
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Uri uri = Uri.fromFile(pictureFile);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                scaledDownCapturedImageFile = CommonUtils.scaleDown(pictureFile, 300, false, bitmap);
                isCaptured = true;
                switch (currentSide) {
                    case 1:
                        rightSideImg = scaledDownCapturedImageFile;
                        break;
                    case 2:
                        backSideImg = scaledDownCapturedImageFile;
                        break;
                    case 3:
                        leftSideImg = scaledDownCapturedImageFile;
                        break;
                    case 4:
                        frontSideImg = scaledDownCapturedImageFile;
                        break;
                }

            } catch (FileNotFoundException e) {
                Log.d("cap", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("cap", "Error accessing file: " + e.getMessage());
            }
        }
    };

    public void openCamera() {
        editButton.setVisibility(View.GONE);
        isCaptured = false;
        capButton.setText(getString(R.string.capture));
        mCamera.startPreview();
    }

    private void setComments() {
        switch (currentSide) {
            case 1:
                comments.setRightSideComment(commentEt.getText().toString());
                break;
            case 2:
                comments.setBackSideComment(commentEt.getText().toString());
                break;
            case 3:
                comments.setLeftSideComment(commentEt.getText().toString());
                break;
            case 4:
                comments.setFrontSideComment(commentEt.getText().toString());
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (isCaptured) {
                        nextStepAction();
                    } else {
                        mCamera.takePicture(null, null, mPicture);
                        showProgress(getString(R.string.capture_loading));
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}
