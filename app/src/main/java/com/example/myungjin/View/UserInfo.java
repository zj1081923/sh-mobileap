package com.example.myungjin.withusplanet.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myungjin.withusplanet.R;

public class UserInfo extends AppCompatActivity {
    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        final TextView idtx = (TextView)findViewById(R.id.textView);
        final String userID = getIntent().getStringExtra("id");
        idtx.setText("ID | " + userID);

        ImageView userImage = (ImageView)findViewById(R.id.profile);


        Button changePW = (Button)findViewById(R.id.changepw);
        Button leaveAccount = (Button)findViewById(R.id.leaveaccount);
        Button logoutBtn = (Button)findViewById(R.id.logout);

        changePW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intentChangePWActivity = new Intent(mContext, ChangePW.class);
                intentChangePWActivity.putExtra("id",userID);
                startActivity(intentChangePWActivity);
            }
        });
        leaveAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intentLeaveAccountActivity = new Intent(mContext, LeaveAccount.class);
                intentLeaveAccountActivity.putExtra("id",userID);
                startActivity(intentLeaveAccountActivity);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();
                Intent intentLoginActivity = new Intent(mContext, Login_Activity.class);
                intentLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intentLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentLoginActivity);
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfileActivity = new Intent(mContext, User_profile.class);
                intentProfileActivity.putExtra("id",userID);
                startActivity(intentProfileActivity);
            }
        });
    }
}
