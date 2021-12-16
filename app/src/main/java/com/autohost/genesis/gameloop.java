package com.autohost.genesis;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

public class gameloop extends Service {


    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    private FrameLayout mFrameLayout;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity =   Gravity.CENTER;
        params.x = 0;
        params.y = 0;



        mFrameLayout = new FrameLayout(this);
        windowManager.addView(mFrameLayout, params);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.layout_gameloop, mFrameLayout);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFrameLayout!=null)
            windowManager.removeView(mFrameLayout);
    }
}
