package com.example.myungjin.withusplanet.DataConnect;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ServerConnect {
    private static String ip_adr="203.252.34.237";
    private int sim_port;
    private String sim_ip;

    public String returnReply(Map<String, String>inputMap) throws ExecutionException, InterruptedException {
        String result = null;
        result = (String)new NetworkTask2().execute(inputMap).get();
        return result;
    }

    public void setIp_adr(String ip_adr){
        this.ip_adr = ip_adr;
    }
    public void setSim_port(int sim_port){
        this.sim_ip = sim_ip;
    }
    public void setSim_ip(String sim_ip){
        this.sim_ip = sim_ip;
    }


    public class NetworkTask2 extends AsyncTask<Map<String, String>, Integer, String> {
        protected String doInBackground(Map... maps) {
            String url="http://"+ip_adr+":8080"+maps[0].get("url");
            Log.d("URL address",url);
            System.out.println(maps);

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", url);
            // Parameter 를 전송한다.
            http.addAllParameters(maps[0]);
            //Http 요청 전송
            HttpClient post = http.create();
            post.request();
            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();
            // 응답 본문 가져오기
            String body = post.getBody();
            return body;
        }


        @Override
        protected void onPostExecute(String s) {
            Log.d("JSON_RESULT", s);
        }
    }
}
