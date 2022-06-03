package com.alyndroid.sayarty.ui.daily;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
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

import com.alyndroid.sayarty.R;
import com.alyndroid.sayarty.di.component.CommentsComponent;
import com.alyndroid.sayarty.di.component.DaggerCommentsComponent;
import com.alyndroid.sayarty.pojo.CameraPreview;
import com.alyndroid.sayarty.pojo.Comments;
import com.alyndroid.sayarty.ui.CameraActivity;
import com.alyndroid.sayarty.ui.base.BaseActivity;
import com.alyndroid.sayarty.util.CommonUtils;
import com.alyndroid.sayarty.util.Constant;
import android.hardware.camera2.*;
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
    @BindView(R.id.capture_button)
    Button capButton;
    @BindView(R.id.edit_button)
    Button editButton;
    @BindView(R.id.comment_et)
    EditText commentEt;
    @BindView(R.id.photo_img)
    ImageView photoImg;
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
        updateUi(currentSide);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
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
        photoImg.setImageBitmap(null);
        openCamera();
        switch (sideNumber) {
            case 1:
                setTitle(getString(R.string.right_side_title));
                hintTv.setText(getString(R.string.right_side_hint));
                commentEt.setText("");
//                cameraFrame.setImageResource(R.drawable.truck_01);
                break;
            case 2:
                hintTv.setText(getString(R.string.back_side_hint));
                setTitle(getString(R.string.back_Side_title));
                commentEt.setText("");
//                cameraFrame.setImageResource(R.drawable.truck_02);
                break;
            case 3:
                hintTv.setText(getString(R.string.left_side_hint));
                setTitle(getString(R.string.left_side_title));
                commentEt.setText("");
//                cameraFrame.setImageResource(R.drawable.truck_04);
                break;
            case 4:
                hintTv.setText(getString(R.string.front_side_hint));
                setTitle(getString(R.string.front_Side_title));
                commentEt.setText("");
//                cameraFrame.setImageResource(R.drawable.truck_03);
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
                    Intent intent = new Intent(this, CameraActivity.class);
                    intent.putExtra("CurrentSide", currentSide);
                    startActivityForResult(intent,1000);
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
            startActivity(intent);

        } else {
            updateUi(currentSide + 1);
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
                capButton.setText(getString(R.string.next_step));
                editButton.setVisibility(View.VISIBLE);
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                scaledDownCapturedImageFile = pictureFile;
                isCaptured = true;
                hideProgress();
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
                        Intent intent = new Intent(this, CameraActivity.class);
                        intent.putExtra("CurrentSide", currentSide);
                        startActivityForResult(intent,1000);
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK){
                capButton.setText(getString(R.string.next_step));
                editButton.setVisibility(View.VISIBLE);
                scaledDownCapturedImageFile = (File) data.getExtras().get("result");
                isCaptured = true;
                hideProgress();
                photoImg.setImageBitmap(BitmapFactory.decodeFile(scaledDownCapturedImageFile.getAbsolutePath()));
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
        }
    } //onActivityResult
}}
