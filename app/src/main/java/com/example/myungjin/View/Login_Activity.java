package com.example.myungjin.withusplanet.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myungjin.withusplanet.DataConnect.ServerConnect;
import com.example.myungjin.withusplanet.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Login_Activity extends AppCompatActivity {

    private Context mContext = this;
    String loginId, loginPwd, status;
    //EditText ip;/////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        final ServerConnect serverConnect = new ServerConnect();

        final CheckBox autologin_Check = (CheckBox)findViewById(R.id.auto_check);
        final CheckBox savingid_Check = (CheckBox)findViewById(R.id.savingid_check);
        final EditText IDText = (EditText)findViewById(R.id.idText);
        final EditText PWText = (EditText) findViewById(R.id.pwText);
        //ip = (EditText)findViewById(R.id.ip);////////////////////////////////////////////////////////////////////////
        //ip.setText("203.252.34.237");


        getSupportActionBar().hide();
        final SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId",null);
        loginPwd = auto.getString("inputPwd",null);
        status = auto.getString("status",null);

        System.out.println("저장된아이디 : "+loginId);
        System.out.println("저장된비밀번호 : "+loginPwd);
        System.out.println("저장된상태 : "+status);

        //if(loginId != null && loginPwd != null && status != null){
        if(status!=null){
            System.out.println("---1-----");
            if(status.equals("auto")){
                Map userInfo = new HashMap();
                userInfo.put("url","/login");
                userInfo.put("id",loginId);
                userInfo.put("pw",loginPwd);
                String result = null;
                System.out.println("---2-----");
                try {
                    result=serverConnect.returnReply(userInfo);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(result.equals("1")){

                    System.out.println("---3-----");
                    Intent intentViewActivity = new Intent(mContext, ViewActivity.class);
                    intentViewActivity.putExtra("id",loginId);
                    intentViewActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentViewActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentViewActivity);

                    Toast.makeText(getApplicationContext(),"autologin",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"login failed",Toast.LENGTH_SHORT).show();
                }
            }
            else if(status.equals("id")){
                IDText.setText(loginId);
                savingid_Check.setChecked(true);
            }
        }

        Button LoginBtn = (Button) findViewById(R.id.loginBtn);
        Button SigninBtn = (Button)findViewById(R.id.signinBtn);
        TextView forgetPW = (TextView)findViewById(R.id.forgetPW);


        forgetPW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //String ipadr = ip.getText().toString();////////////////////////////////////////////////////////////////////////
                //serverConnect.setIp_adr(ipadr);
                Intent intentPWActivity = new Intent(mContext, FindingPW.class);
                startActivity(intentPWActivity);
            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {//login 버튼 클릭했을때

                //String ipadr = ip.getText().toString();////////////////////////////////////////////////////////////////////////
                //serverConnect.setIp_adr(ipadr);

                String id = IDText.getText().toString();
                String pw = PWText.getText().toString();

                if(savingid_Check.isChecked()==true){
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("inputId",id);
                    autoLogin.putString("status","id");
                    autoLogin.commit();
                }
                if(autologin_Check.isChecked()==true){
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("inputId",id);
                    autoLogin.putString("inputPwd",pw);
                    autoLogin.putString("status","auto");
                    autoLogin.commit();
                }

                Map UserInfo = new HashMap();
                UserInfo.put("url","/login");
                UserInfo.put("id",id);
                UserInfo.put("pw",pw);
                String result = null;
                try {
                    result=serverConnect.returnReply(UserInfo);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(result.equals("1")){
                    //Intent intentViewActivity = new Intent(mContext, ViewActivity.class);
                    Intent intentViewActivity = new Intent(mContext, SelectSimAP.class);
                    intentViewActivity.putExtra("id",id);
                    intentViewActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentViewActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentViewActivity); //나중에삭제할부분
                }
                else{
                    Toast.makeText(getApplicationContext(),"login failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        SigninBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //String ipadr = ip.getText().toString();////////////////////////////////////////////////////////////////////////
                //serverConnect.setIp_adr(ipadr);
                Intent intentSignUpActivity = new Intent(mContext, SignupActivity.class);
                startActivity(intentSignUpActivity);
            }
        });
    }
}//end of class