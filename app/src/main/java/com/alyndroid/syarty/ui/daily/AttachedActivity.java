package com.alyndroid.syarty.ui.daily;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.pojo.Comments;
import com.alyndroid.syarty.ui.base.BaseActivity;
import com.alyndroid.syarty.util.Constant;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttachedActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.comment_et)
    EditText commentEt;
    @BindView(R.id.il)
    TextInputLayout il;
    @BindView(R.id.next_button)
    Button nextButton;
    @BindView(R.id.toolKitCheckBox)
    CheckBox toolKitCheckBox;
    @BindView(R.id.fireCancelCheckBox)
    CheckBox fireCancelCheckBox;
    @BindView(R.id.firstAidCheckBox)
    CheckBox firstAidCheckBox;
    @BindView(R.id.estipnCheckBox)
    CheckBox estipnCheckBox;
    @BindView(R.id.electricCheckBox)
    CheckBox electricCheckBox;

    private ArrayList<String> attached = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attached);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.Attached_title));
        nextButton.setOnClickListener(this);
        toolKitCheckBox.setOnClickListener(this);
        fireCancelCheckBox.setOnClickListener(this);
        firstAidCheckBox.setOnClickListener(this);
        estipnCheckBox.setOnClickListener(this);
        electricCheckBox.setOnClickListener(this);

        attached.add("tool_kit");
        attached.add("fire_cancel");
        attached.add("first_aid");
        attached.add("estipn");
        attached.add("electric");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                Intent intent = new Intent(this, ConfirmationActivity.class);

                intent.putExtra(Constant.INTENT_EXTRAS.right_Side, (File) getIntent().getExtras().get(Constant.INTENT_EXTRAS.right_Side));
                intent.putExtra(Constant.INTENT_EXTRAS.back_side, (File) getIntent().getExtras().get(Constant.INTENT_EXTRAS.back_side));
                intent.putExtra(Constant.INTENT_EXTRAS.left_side, (File) getIntent().getExtras().get(Constant.INTENT_EXTRAS.left_side));
                intent.putExtra(Constant.INTENT_EXTRAS.front_side, (File) getIntent().getExtras().get(Constant.INTENT_EXTRAS.front_side));
                intent.putExtra(Constant.INTENT_EXTRAS.isFrom, getIntent().getExtras().getString(Constant.INTENT_EXTRAS.isFrom));
                intent.putExtra(Constant.INTENT_EXTRAS.receiverID, getIntent().getExtras().getString(Constant.INTENT_EXTRAS.receiverID));
                intent.putExtra(Constant.INTENT_EXTRAS.Operation_id, getIntent().getExtras().getString(Constant.INTENT_EXTRAS.Operation_id));
                intent.putExtra(Constant.INTENT_EXTRAS.CAR_ID, getIntent().getExtras().getString(Constant.INTENT_EXTRAS.CAR_ID));
                intent.putExtra(Constant.INTENT_EXTRAS.COMMENTS, (Comments) getIntent().getExtras().get(Constant.INTENT_EXTRAS.COMMENTS));
                intent.putExtra(Constant.INTENT_EXTRAS.ATTACHED, attached);
                startActivity(intent);
                break;
            case R.id.toolKitCheckBox:
                if (toolKitCheckBox.isChecked()) {
                    attached.add("tool_kit");
                } else {
                    attached.remove("tool_kit");
                }
                break;
            case R.id.fireCancelCheckBox:
                if (fireCancelCheckBox.isChecked()) {
                    attached.add("fire_cancel");
                } else {
                    attached.remove("fire_cancel");
                }
                break;
            case R.id.firstAidCheckBox:
                if (firstAidCheckBox.isChecked()) {
                    attached.add("first_aid");
                } else {
                    attached.remove("first_aid");
                }
                break;
            case R.id.estipnCheckBox:
                if (estipnCheckBox.isChecked()) {
                    attached.add("estipn");
                } else {
                    attached.remove("estipn");
                }
                break;
            case R.id.electricCheckBox:
                if (electricCheckBox.isChecked()) {
                    attached.add("electric");
                } else {
                    attached.remove("electric");
                }
                break;

        }
    }
}
