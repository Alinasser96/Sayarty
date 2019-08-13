package com.alyndroid.syarty.ui.refused;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.ui.base.BaseActivity;
import com.alyndroid.syarty.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RefuseActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_call)
    Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuse);
        ButterKnife.bind(this);
        btnCancel.setOnClickListener(this);
        btnCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_call:
                makeCall();
                break;
        }
    }


    private void makeCall() {
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        } else {

            if (ActivityCompat.checkSelfPermission(RefuseActivity.this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            } else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(RefuseActivity.this, PERMISSIONS_STORAGE, 9);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch (requestCode) {
            case 9:
                permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (permissionGranted) {
            phoneCall();
        } else {
            Toast.makeText(RefuseActivity.this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneCall() {
        if (ActivityCompat.checkSelfPermission(RefuseActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + getIntent().getExtras().get(Constant.INTENT_EXTRAS.RECEIVER_PHONE)));
            RefuseActivity.this.startActivity(callIntent);
            finish();
        } else {
            Toast.makeText(RefuseActivity.this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }
}
