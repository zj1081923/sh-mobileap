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

public class FindingPW extends AppCompatActivity {
    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_pw);
        final ServerConnect serverConnect = new ServerConnect();

        getSupportActionBar().hide();


        final EditText IDText = (EditText)findViewById(R.id.emailTV);
        Button submitBtn = (Button)findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = IDText.getText().toString();
                Map userinfo = new HashMap();
                userinfo.put("url","/send");
                userinfo.put("email",email);
                String result = null;
                try {
                    result = serverConnect.returnReply(userinfo);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(result.equals("0")){
                    Toast.makeText(getApplicationContext(),"There's no such e-mail.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Success! Check your mail.", Toast.LENGTH_SHORT).show();

                    Intent intentLoginActivity = new Intent(mContext, Login_Activity.class);
                    intentLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentLoginActivity);
                }
            }
        });


    }
}
