package com.example.myungjin.withusplanet.View;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myungjin.withusplanet.R;

public class IntroActivity extends AppCompatActivity {
    private Context mContext = this;
    private Handler handler;
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            Intent intent = new Intent(mContext, Login_Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        getSupportActionBar().hide();
        handler = new Handler();
        handler.postDelayed(runnable,2100);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }
}
