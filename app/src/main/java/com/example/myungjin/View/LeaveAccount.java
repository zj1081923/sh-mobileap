package com.example.myungjin.withusplanet.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myungjin.withusplanet.DataConnect.ServerConnect;
import com.example.myungjin.withusplanet.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LeaveAccount extends AppCompatActivity {
    private Context mContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_account);
        final ServerConnect serverConnect = new ServerConnect();

        getSupportActionBar().hide();
        final String userID = getIntent().getStringExtra("id");

        EditText email = (EditText)findViewById(R.id.emailaddress);
        final EditText password = (EditText)findViewById(R.id.pw);
        Button submitBtn = (Button)findViewById(R.id.submitBtn);

        email.setHint(userID);
        setUseableEditText(email, false);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pw = password.getText().toString();
                final Map userInfo = new HashMap();
                userInfo.put("url","/goodBye");
                userInfo.put("id",userID);
                userInfo.put("pw",pw);
                userInfo.put("request",null);
                String result = null;
                try {
                    result = serverConnect.returnReply(userInfo);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(result.equals("1")){//비번 맞음
                    AlertDialog.Builder checkdialog = new AlertDialog.Builder(mContext);
                    checkdialog.setMessage("Are you sure you want to leave?");
                    //yes
                    checkdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //네트워크로 전송 보내기 (구현해야함)
                            Map userInfo1 = new HashMap();
                            userInfo1.put("url","/goodBye");
                            userInfo1.put("id",userID);
                            userInfo1.put("pw", pw);
                            userInfo1.put("request","1");
                            String result1 = null;
                            try {
                                result1 = serverConnect.returnReply(userInfo1);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if(result1.equals("1")){
                                Toast.makeText(getApplicationContext(),"Successfully deleted.",Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), "You can easily join again at any time.", Toast.LENGTH_SHORT).show();
                                Intent intentLoginActivity = new Intent(mContext, Login_Activity.class);
                                intentLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intentLoginActivity);
                            }else{
                                Toast.makeText(getApplicationContext(),"deletion failed. Please try again.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //no
                    checkdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    AlertDialog checkalert = checkdialog.create();
                    checkalert.setCanceledOnTouchOutside(false);
                    checkalert.setTitle("Leave Account");
                    checkalert.show();
                }
                else if(result.equals("0")){//비번 틀렸을때
                    Toast.makeText(getApplicationContext(),"wrong pw.",Toast.LENGTH_SHORT).show();
                }
                else{//기타 실패

                }
            }
        });


    }

    private void setUseableEditText(EditText et, boolean useable){
        et.setClickable(useable);
        et.setEnabled(useable);
        et.setFocusable(useable);
        et.setFocusableInTouchMode(useable);
    }
}
