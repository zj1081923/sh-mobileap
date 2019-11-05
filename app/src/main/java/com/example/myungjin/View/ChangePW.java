package com.example.myungjin.withusplanet.View;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myungjin.withusplanet.DataConnect.ServerConnect;
import com.example.myungjin.withusplanet.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ChangePW extends AppCompatActivity {
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        final ServerConnect serverConnect = new ServerConnect();

        getSupportActionBar().hide();
        final String userID = getIntent().getStringExtra("id");

        final EditText currentTv = (EditText)findViewById(R.id.currentTV);
        final EditText newTv = (EditText)findViewById(R.id.newTV);
        final EditText confirmTv = (EditText)findViewById(R.id.confirmTV);

        Button submitBtn = (Button)findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String current = currentTv.getText().toString();
                String new_ = newTv.getText().toString();
                String confirm = confirmTv.getText().toString();

                if(new_.equals(confirm)) {
                    Map userInfo = new HashMap();
                    userInfo.put("url","/changePassword");
                    userInfo.put("id", userID);
                    userInfo.put("cur_pw",current);
                    userInfo.put("new_pw",new_);
                    String result = null;
                    try {
                        result = serverConnect.returnReply(userInfo);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    if(result.equals("1")){
                        Toast.makeText(getApplicationContext(),"Successfully changed.",Toast.LENGTH_SHORT).show();
                        Intent intentViewActivity = new Intent(mContext,ViewActivity.class);
                        intentViewActivity.putExtra("id",userID);
                        intentViewActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentViewActivity);
                    }else{
                        Toast.makeText(getApplicationContext(),"Change failed. Please try again.",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Confirm password is different.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
