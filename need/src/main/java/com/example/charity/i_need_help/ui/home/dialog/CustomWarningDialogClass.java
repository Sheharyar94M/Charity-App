package com.example.charity.i_need_help.ui.home.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.widget.AppCompatButton;

import com.example.charity.databinding.WarningDialogLayoutBinding;

public class CustomWarningDialogClass extends Dialog implements
        View.OnClickListener {


    public CustomWarningDialogClass(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.charity.databinding.WarningDialogLayoutBinding warningDialogLayoutBinding = WarningDialogLayoutBinding.inflate(getLayoutInflater());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(warningDialogLayoutBinding.getRoot());

        warningDialogLayoutBinding.okButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}