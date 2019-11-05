package com.example.myungjin.withusplanet.View;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myungjin.withusplanet.DataConnect.ServerConnect;
import com.example.myungjin.withusplanet.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class SignupActivity extends AppCompatActivity {
    private Context mContext = this;
    String email,pw,confirmpw;
    private TimerTask second;
    private TextView timer_text;
    private final Handler handler = new Handler();
    int timer_sec;
    int count;
    int check=0;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        final ServerConnect serverConnect = new ServerConnect();

        final EditText emailET= (EditText)findViewById(R.id.email);
        final EditText pwET = (EditText)findViewById(R.id.pw);
        final EditText confirmpwET = (EditText)findViewById(R.id.confirmTV);
        Button submit1 = (Button)findViewById(R.id.submit1);
        final Button submit2 = (Button)findViewById(R.id.submit2);
        final EditText CN = (EditText)findViewById(R.id.CN);

        setUseableEditText(CN, false);
        submit2.setEnabled(false);

        TextView Time = (TextView)findViewById(R.id.time);

        submit1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                email = emailET.getText().toString();
                pw = pwET.getText().toString();
                confirmpw = confirmpwET.getText().toString();

                if(pw.equals(confirmpw)){
                    Map userInfo = new HashMap();
                    userInfo.put("url","/send");
                    userInfo.put("id",email);
                    userInfo.put("pw",pw);
                    userInfo.put("what","signup");
                    String result = null;
                    try {
                        result = serverConnect.returnReply(userInfo);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(result.equals("1")){
                        Toast.makeText(getApplicationContext(),"Certification Number on your email. Check it.",Toast.LENGTH_SHORT).show();
                        setUseableEditText(CN, true);
                        submit2.setEnabled(true);
                        timer();
                    }
                    else if(result.equals("-1")){
                        Toast.makeText(getApplicationContext(),"Exist email. Change it.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"failed. try again",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Confirm password is wrong. Please try again.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cn = CN.getText().toString();
                Map info = new HashMap();
                info.put("url","/signup");
                info.put("id",email);
                info.put("pw",pw);
                info.put("cn",cn);
                String result = null;
                try {
                    result = serverConnect.returnReply(info);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(result.equals("1")){
                    timer.cancel();
                    Toast.makeText(getApplicationContext(),"good good",Toast.LENGTH_SHORT).show();
                    Intent intentLoginActivity = new Intent(mContext, Login_Activity.class);
                    intentLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentLoginActivity);
                }else{
                    Toast.makeText(getApplicationContext(),"cn different",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed(){
        if(check==1) timer.cancel();
        super.onBackPressed();
    }

    private void setUseableEditText(EditText et, boolean useable){
        et.setClickable(useable);
        et.setEnabled(useable);
        et.setFocusable(useable);
        et.setFocusableInTouchMode(useable);
    }
    public void timer(){
        check=1;
        timer_text = (TextView)findViewById(R.id.time);
        timer_sec = 0;
        count=300;
        second = new TimerTask() {
            @Override
            public void run() {
                Update();
                if(count==0){
                    timer.cancel();
                    Toast.makeText(getApplicationContext(),"time out",Toast.LENGTH_SHORT).show();
                    Intent intentLoginActivity = new Intent(mContext, Login_Activity.class);
                    intentLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentLoginActivity);
                }
                count--;

            }
        };
        timer = new Timer();
        timer.schedule(second,0,1000);
    }
    protected void Update(){
        Runnable updater = new Runnable(){
            @Override
            public void run() {
                timer_text.setText(count/60+":"+count%60);
            }
        };
        handler.post(updater);
    }
}
