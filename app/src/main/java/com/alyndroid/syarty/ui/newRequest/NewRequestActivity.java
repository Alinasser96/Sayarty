package com.alyndroid.syarty.ui.newRequest;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.di.component.DaggerPresenterComponent;
import com.alyndroid.syarty.di.component.PresenterComponent;
import com.alyndroid.syarty.ui.base.BaseActivity;
import com.alyndroid.syarty.ui.daily.CaptureActivity;
import com.alyndroid.syarty.ui.dashboard.DashboardActivity;
import com.alyndroid.syarty.util.CommonUtils;
import com.alyndroid.syarty.util.Constant;
import com.alyndroid.syarty.util.helpers.NotificationHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewRequestActivity extends BaseActivity implements View.OnClickListener, NewRequestView {

    @BindView(R.id.btn_accept)
    Button btnAccept;
    @BindView(R.id.btn_snooze)
    Button btnSnooze;
    @BindView(R.id.tv_remaining_time)
    TextView tvRemainingTime;
    @BindView(R.id.btn_reject)
    Button btnReject;
    @BindView(R.id.btn_with_photos)
    Button btnWithPhotos;
    @BindView(R.id.btn_without_photos)
    Button btnWithoutPhotos;
    @BindView(R.id.btn_delay_half_hour)
    Button btnDelayHalfHour;
    @BindView(R.id.btn_delay_one_hour)
    Button btnDelayOneHour;
    @BindView(R.id.new_request_title)
    TextView newRequestTitle;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private CountDownTimer countDownTimer;
    private int remainingSeconds;
    private NotificationHelper notificationHelper;
    private NewRequestPresenter newRequestPresenter;
    private String operationID;
    private boolean isDelayed;
    private static final String TAG = "NewRequestActivity";
    private boolean isWithPhoto = false;
    private boolean isReminder = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_new_request);
        ButterKnife.bind(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        remainingSeconds = 60;

        if (getIntent().getExtras().get(Constant.INTENT_EXTRAS.Operation_id) == null) {
            btnReject.setVisibility(View.GONE);
            isReminder = true;
        } else {
            operationID = getIntent().getExtras().getString(Constant.INTENT_EXTRAS.Operation_id);
            newRequestTitle.setText(String.format(getString(R.string.new_request_title), getIntent().getExtras().getString(Constant.INTENT_EXTRAS.SENDER_NAME)));
        }

        notificationHelper = new NotificationHelper(this);
        btnAccept.setOnClickListener(this);
        btnSnooze.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        btnWithPhotos.setOnClickListener(this);
        btnWithoutPhotos.setOnClickListener(this);
        btnDelayHalfHour.setOnClickListener(this);
        btnDelayOneHour.setOnClickListener(this);
        countDownTimer = new CountDownTimer(remainingSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvRemainingTime.setText(millisUntilFinished / 1000 + "");
                vibrator.vibrate(500);
            }

            @Override
            public void onFinish() {
                tvRemainingTime.setText("0");

                stopCountDownTimer();

                notificationHelper.generateNotification("عملية تسليم فائتة",
                        "لقد فاتتك عملية تسليم سيارة");
                hideProgress();
                startActivity(new Intent(NewRequestActivity.this, DashboardActivity.class));
                finish();

            }
        };
        startCountDownTimer();

        PresenterComponent component = DaggerPresenterComponent.builder()
                .acc(this)
                .build();
        newRequestPresenter = component.getNewRequestPresenter();
        newRequestPresenter.attachView(this);
//        newRequestPresenter.setVisible(Integer.parseInt(operationID));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_accept:
                if (isReminder) {
                    acceptAction();
                } else {
                    controlViews();
                }

                break;
            case R.id.btn_snooze:
                hideViews(btnSnooze, btnAccept);
                showViews(btnDelayHalfHour, btnDelayOneHour);
                break;

            case R.id.btn_reject:
                newRequestPresenter.sendStatus(3, Integer.parseInt(operationID));
                break;
            case R.id.btn_with_photos:
                acceptAction();
                break;
            case R.id.btn_without_photos:
                newRequestPresenter.sendStatus(2, Integer.parseInt(operationID));
                break;
            case R.id.btn_delay_one_hour:
                delayAction(CommonUtils.addTimeToCurrent(60));
                break;
            case R.id.btn_delay_half_hour:
                delayAction(CommonUtils.addTimeToCurrent(30));
                break;
        }
    }

    private void delayAction(String delay) {
        isDelayed = true;
        newRequestPresenter.sendStatus(4, delay, Integer.parseInt(operationID));
    }


    private void acceptAction() {
        isWithPhoto = true;
        if (isReminder) {
            Intent intent = new Intent(this, CaptureActivity.class);
            intent.putExtra(Constant.INTENT_EXTRAS.isFrom, Constant.INTENT_EXTRAS.FREQ_REMINDER);
            intent.putExtra(Constant.INTENT_EXTRAS.CAR_ID, getIntent().getExtras().getString(Constant.INTENT_EXTRAS.CAR_ID));
            hideProgress();
            startActivity(intent);
            stopCountDownTimer();
            vibrator.cancel();
            finish();
        } else {
            newRequestPresenter.sendStatus(1, Integer.parseInt(operationID), "0", new ArrayList());
        }
    }

    private void controlViews() {
        hideViews(btnSnooze, btnAccept);
        showViews(btnWithoutPhotos, btnWithPhotos);
    }

    private void startCountDownTimer() {

        if (null != mediaPlayer && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setLooping(true);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });

        countDownTimer.cancel();
        countDownTimer.start();

    }

    private void stopCountDownTimer() {
        mediaPlayer.stop();
        countDownTimer.cancel();

        finish();

    }

    @Override
    public void onSendStatusSuccess() {
        if (isWithPhoto) {
            Intent intent = new Intent(this, CaptureActivity.class);
            intent.putExtra(Constant.INTENT_EXTRAS.isFrom, Constant.INTENT_EXTRAS.NewRequest);
            intent.putExtra(Constant.INTENT_EXTRAS.Operation_id, operationID);
            intent.putExtra(Constant.INTENT_EXTRAS.CAR_ID, getIntent().getExtras().getString(Constant.INTENT_EXTRAS.CAR_ID));
            hideProgress();
            startActivity(intent);
            stopCountDownTimer();
            vibrator.cancel();
            finish();
        } else {
            stopCountDownTimer();
            vibrator.cancel();
            finish();
        }
    }

    @Override
    public void onSendStatusFail() {
        CommonUtils.showToast(this, "Server error");
        finish();
    }

    @Override
    public void setLoading(int apiCode) {
        if (apiCode != 151)
            showProgress(getString(R.string.loading));
    }

    @Override
    public void setLoaded(int apiCode) {
        if (apiCode != 151)
            hideProgress();
    }


}
