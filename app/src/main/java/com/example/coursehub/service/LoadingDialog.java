package com.example.coursehub.service;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.example.coursehub.R;

public class LoadingDialog {

    Dialog dialog;
    Context contexts;

    public LoadingDialog(Context context) {
        dialog =  new Dialog(context);
        contexts = context;
    }

    public void createLoadingDialgg(){
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(contexts.getDrawable(R.drawable.card_curve));
        dialog.setCancelable(false);
    }


    public void dismissDialog(){
        dialog.dismiss();
    }

    public void OpenlogoutDialog() {
        dialog.show();
    }

}
