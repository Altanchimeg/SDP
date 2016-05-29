package com.skytel.sdp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.skytel.sdp.R;

public class CustomProgressDialog extends ProgressDialog {
    AnimationDrawable runningAnimation;

    public CustomProgressDialog(Context context) {
        super(context, R.style.full_screen_dialog);
        this.setIndeterminate(true);
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progressdialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        ImageView mRunningManView = (ImageView) findViewById(R.id.animation);
        mRunningManView.setBackgroundResource(R.drawable.loading_animation_list);
        runningAnimation = (AnimationDrawable) mRunningManView.getBackground();

    }

    @Override
    public void show() {
        super.show();
        runningAnimation.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        runningAnimation.stop();
    }
}
