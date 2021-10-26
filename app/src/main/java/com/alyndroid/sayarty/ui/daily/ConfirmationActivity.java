package com.alyndroid.sayarty.ui.daily;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.alyndroid.sayarty.R;
import com.alyndroid.sayarty.data.local.SharedPreferenceHelper;
import com.alyndroid.sayarty.di.component.DaggerPresenterComponent;
import com.alyndroid.sayarty.di.component.PresenterComponent;
import com.alyndroid.sayarty.pojo.Comments;
import com.alyndroid.sayarty.pojo.StoreResponse;
import com.alyndroid.sayarty.ui.base.BaseActivity;
import com.alyndroid.sayarty.ui.dashboard.DashboardActivity;
import com.alyndroid.sayarty.ui.newRequest.NewRequestPresenter;
import com.alyndroid.sayarty.ui.newRequest.NewRequestView;
import com.alyndroid.sayarty.util.CommonUtils;
import com.alyndroid.sayarty.util.Constant;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class ConfirmationActivity extends BaseActivity implements View.OnClickListener, DailyView, NewRequestView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.right_side_iv)
    ImageView rightSideIv;
    @BindView(R.id.back_side_iv)
    ImageView backsideIv;
    @BindView(R.id.left_side_iv)
    ImageView leftSideIv;
    @BindView(R.id.front_side_iv)
    ImageView frontSideIv;
    @BindView(R.id.cap_button)
    Button capButton;
    private static File capturedImageFile;
    @BindView(R.id.comment_et)
    EditText commentEt;
    private File rightSideFile;
    private File backSideFile;
    private File leftSideFile;
    private File frontSideFile;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private DailyPresenter dailyPresenter;
    private NewRequestPresenter newRequestPresenter;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private static final String TAG = "ConfirmationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_pics_confirmation);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.confirm));
        rightSideIv.setOnClickListener(this);
        backsideIv.setOnClickListener(this);
        leftSideIv.setOnClickListener(this);
        frontSideIv.setOnClickListener(this);
        capButton.setOnClickListener(this);
        sharedPreferenceHelper = new SharedPreferenceHelper();
        PresenterComponent component = DaggerPresenterComponent.builder()
                .acc(this)
                .build();
        newRequestPresenter = component.getNewRequestPresenter();
        newRequestPresenter.attachView(this);

        dailyPresenter = new DailyPresenter(compositeDisposable, this);
        dailyPresenter.attachView(this);


        rightSideFile = (File) getIntent().getExtras().get(Constant.INTENT_EXTRAS.right_Side);
        Uri rightSideUri = Uri.fromFile(rightSideFile);
        Picasso.get().load(rightSideUri).into(rightSideIv);

        backSideFile = (File) getIntent().getExtras().get(Constant.INTENT_EXTRAS.back_side);
        Uri backSideUri = Uri.fromFile(backSideFile);
        Picasso.get().load(backSideUri).rotate(90).into(backsideIv);

        leftSideFile = (File) getIntent().getExtras().get(Constant.INTENT_EXTRAS.left_side);
        Uri leftSideUri = Uri.fromFile(leftSideFile);
        Picasso.get().load(leftSideUri).rotate(180).into(leftSideIv);

        frontSideFile = (File) getIntent().getExtras().get(Constant.INTENT_EXTRAS.front_side);
        Uri frontSideUri = Uri.fromFile(frontSideFile);
        Picasso.get().load(frontSideUri).rotate(90).into(frontSideIv);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cap_button:
                store();
                break;
            case R.id.right_side_iv:
                edit(Constant.permission.right_side_IMAGE);
                break;
            case R.id.back_side_iv:
                edit(Constant.permission.back_side_IMAGE);
                break;
            case R.id.left_side_iv:
                edit(Constant.permission.left_side_IMAGE);
                break;
            case R.id.front_side_iv:
                edit(Constant.permission.front_side_IMAGE);
                break;

        }
    }

    private void store() {
        int status = 1;
        switch (getIntent().getExtras().get(Constant.INTENT_EXTRAS.isFrom).toString()) {
            case Constant.INTENT_EXTRAS.NewCase:
                status = 1;
                break;
            case Constant.INTENT_EXTRAS.NewRequest:
                status = 2;
                break;

            case Constant.INTENT_EXTRAS.SelectReceiver:
                status = 3;
                break;
            case Constant.INTENT_EXTRAS.FREQ_REMINDER:
                //ToDo should send car id
                status = 4;
                break;
        }
        dailyPresenter.store(rightSideFile, backSideFile, leftSideFile, frontSideFile,
                getComments(),
                sharedPreferenceHelper.getCoreUserData().getUserId(),
                Integer.parseInt(getIntent().getExtras().getString(Constant.INTENT_EXTRAS.CAR_ID)),
                status);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                //scale down the photo
                capturedImageFile = (File) data.getExtras().get(Constant.INTENT_EXTRAS.EDITED_PHOTO);
                Uri uri = Uri.fromFile(capturedImageFile);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                switch (requestCode) {
                    case Constant.permission.right_side_IMAGE:
                        rightSideFile = CommonUtils.scaleDown(capturedImageFile, bitmap);
                        Picasso.get().load(uri).into(rightSideIv);
                        break;
                    case Constant.permission.back_side_IMAGE:
                        backSideFile = CommonUtils.scaleDown(capturedImageFile, bitmap);
                        Picasso.get().load(uri).into(backsideIv);
                        break;
                    case Constant.permission.left_side_IMAGE:
                        leftSideFile = CommonUtils.scaleDown(capturedImageFile, bitmap);
                        Picasso.get().load(uri).into(leftSideIv);
                        break;
                    case Constant.permission.front_side_IMAGE:
                        frontSideFile = CommonUtils.scaleDown(capturedImageFile, bitmap);
                        Picasso.get().load(uri).into(frontSideIv);
                        break;

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void edit(int sideNumber) {
        String title;
        switch (sideNumber) {
            case Constant.permission.right_side_IMAGE:
                title = getString(R.string.right_side_update);
                break;
            case Constant.permission.back_side_IMAGE:
                title = getString(R.string.back_side_update);
                break;
            case Constant.permission.left_side_IMAGE:
                title = getString(R.string.left_side_update);
                break;
            case Constant.permission.front_side_IMAGE:
                title = getString(R.string.front_side_update);
                break;
            default:
                title = getString(R.string.right_side_update);
                break;

        }
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(getString(R.string.edit))
                .setMessage(title)
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    Intent i = new Intent(this, EditActivity.class);
                    i.putExtra(Constant.INTENT_EXTRAS.SIDE_NUMBER, sideNumber);
                    startActivityForResult(i, sideNumber);
                })
                .setNegativeButton(getString(R.string.no), (dialog, which) ->
                        dialog.dismiss())
                .create()
                .show();
    }


    @Override
    public void onStoreSuccess(StoreResponse storeResponse) {

        if (rightSideFile.exists())
            rightSideFile.delete();
        if (backSideFile.exists())
            backSideFile.delete();
        if (leftSideFile.exists())
            leftSideFile.delete();
        if (frontSideFile.exists())
            frontSideFile.delete();

        switch (getIntent().getExtras().get(Constant.INTENT_EXTRAS.isFrom).toString()) {
            case Constant.INTENT_EXTRAS.NewCase:
                CommonUtils.showToast(this, getString(R.string.sendIsDone));
                startActivity(new Intent(this, DashboardActivity.class));
                hideProgress();
                finish();
                break;
            case Constant.INTENT_EXTRAS.FREQ_REMINDER:
                CommonUtils.showToast(this, getString(R.string.sendIsDone));
                startActivity(new Intent(this, DashboardActivity.class));
                hideProgress();
                finish();
                break;
            case Constant.INTENT_EXTRAS.NewRequest:
                Log.d(TAG, "onStoreSuccess: " + storeResponse.getData().getId());
                Log.d(TAG, "onStoreSuccess: " + getIntent().getExtras().get(Constant.INTENT_EXTRAS.Operation_id));
                Log.d(TAG, "onStoreSuccess: " + sharedPreferenceHelper.getCoreUserData().getUserId());
                newRequestPresenter.sendStatus(1,
                        Integer.parseInt(getIntent().getExtras().get(Constant.INTENT_EXTRAS.Operation_id).toString()),
                        String.valueOf(storeResponse.getData().getId()),
                        (ArrayList) getIntent().getExtras().get((Constant.INTENT_EXTRAS.ATTACHED)));
                break;

            case Constant.INTENT_EXTRAS.SelectReceiver:
                HashMap<String, Object> map = new HashMap<>();
                map.put("sender_id", sharedPreferenceHelper.getCoreUserData().getUserId());
                map.put("receiver_id", getIntent().getExtras().get(Constant.INTENT_EXTRAS.receiverID));
                map.put("photo_sender_id", storeResponse.getData().getId());
                dailyPresenter.sendToReceiver(map);
                Log.d(TAG, "onStoreSuccess: " + storeResponse.getData().getId());
                Log.d(TAG, "onStoreSuccess: " + sharedPreferenceHelper.getCoreUserData().getUserId());
                Log.d(TAG, "onStoreSuccess: " + getIntent().getExtras().get(Constant.INTENT_EXTRAS.receiverID));

                break;
        }

    }

    @Override
    public void onStoreFail(String message) {
        if (message.contains(": No address associated with hostname")) {
            CommonUtils.showToast(this, getString(R.string.offline));
        }
        if (message.contains("java.io.FileNotFoundException:")) {
            CommonUtils.showToast(this, getString(R.string.deleted));
            startActivity(new Intent(this, CaptureActivity.class));
            hideProgress();
            finish();
        }

    }

    @Override
    public void onSendToReceiverSuccess() {
        CommonUtils.showToast(this, getString(R.string.sendIsDone));
        startActivity(new Intent(this, DashboardActivity.class));
        hideProgress();
        finish();
    }

    @Override
    public void onSendToReceiverFail(String error) {
        CommonUtils.showToast(this, getString(R.string.some_error));
    }

    @Override
    public void setLoading(int apiCode) {
        showProgress(getString(R.string.loading));
    }

    @Override
    public void setLoaded(int apiCode) {
    }

    @Override
    public void onSendStatusSuccess() {
        CommonUtils.showToast(this, getString(R.string.sendIsDone));
        startActivity(new Intent(this, DashboardActivity.class));
        hideProgress();
        finish();
    }

    @Override
    public void onSendStatusFail() {
        CommonUtils.showToast(this, getString(R.string.some_error));
    }

    public Comments getComments() {
        Comments comments = (Comments) getIntent().getExtras().get(Constant.INTENT_EXTRAS.COMMENTS);
        comments.setGeneralComment(commentEt.getText() + "");
        return comments;
    }
}
