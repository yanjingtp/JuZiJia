package com.heretip.juzijia.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.heretip.juzijia.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        final boolean isFirstLogin = sp.getBoolean("isFirstLogin", true);

        //3秒后跳转到主页面/登录页面
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isFirstLogin) {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }


            }
        }, 3000);


    }
}
