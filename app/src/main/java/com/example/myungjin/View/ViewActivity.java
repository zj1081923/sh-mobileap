package com.example.myungjin.withusplanet.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.myungjin.withusplanet.DataConnect.ServerConnect;
import com.example.myungjin.withusplanet.DataConnect.exData;
import com.example.myungjin.withusplanet.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ViewActivity extends AppCompatActivity {
    private Context mContext = this;

    ServerConnect serverConnect = new ServerConnect();


    private LineChart cpulineChart;
    private LineChart memorylineChart;
    private FloatingActionButton Refresh;
    private String hostname;
    private String ipad;
    private String macad;
    private TextView hostnametv;
    private TextView iptv;
    private TextView mactv;
    private String selectedIP;
    private Float[] cpuinfo;
    public Float[] memoryinfo;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        getSupportActionBar().hide();
        final TextView idtx = (TextView)findViewById(R.id.textView);
        final String userID = getIntent().getStringExtra("id");
        selectedIP = getIntent().getStringExtra("ip");
        idtx.setText("ID | " + userID);

        idtx.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intentUserInfoActivity = new Intent(mContext, UserInfo.class);
                intentUserInfoActivity.putExtra("id",userID);
                startActivity(intentUserInfoActivity);
            }
        });

        hostnametv = (TextView)findViewById(R.id.hostnameTV);
        iptv = (TextView)findViewById(R.id.ipTV);
        mactv = (TextView)findViewById(R.id.macTV);
        cpulineChart = (LineChart)findViewById(R.id.cpuChart);
        memorylineChart = (LineChart)findViewById(R.id.memoryChart);

        getData();

        //onPass 시작(받아오기)
        /*try {
            onPassTvInfo("/basicinfo");
            cpuinfo = onPassLineInfo("/cpugraphinfo", cpuinfo);
            memoryinfo = onPassLineInfo("/memorygraphinfo", memoryinfo);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        hostnametv.setText(hostname);
        iptv.setText(ipad);
        mactv.setText(macad);

        cpulineChart = DrawChart(cpuinfo, cpulineChart,"cpu");
        memorylineChart = DrawChart(memoryinfo, memorylineChart,"memory");*/


        Refresh = (FloatingActionButton)findViewById(R.id.refresh);
        Refresh.setOnClickListener(new View.OnClickListener(){ //새로고침 버튼 눌렀을때
            @Override
            public void onClick(View view) {
                /*try {
                    onPassTvInfo("/basicinfo");
                    cpuinfo = onPassLineInfo("/cpugraphinfo", cpuinfo);
                    memoryinfo = onPassLineInfo("/memorygraphinfo", memoryinfo);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                getData();

                hostnametv.setText(hostname);
                iptv.setText(ipad);
                mactv.setText(macad);

                cpulineChart = DrawChart(cpuinfo, cpulineChart,"cpu");
                memorylineChart = DrawChart(memoryinfo, memorylineChart,"memory");
            }
        });



    }//end of onCreate()

    public LineChart DrawChart (Float[] info, LineChart linechart, String type){

        ArrayList<String> xlabels = new ArrayList<String>();
        List<Entry> Entries = new ArrayList<>();
        int count=0;
        if(type.equals("memory")){
            for(float i : info){
                i = i/1024;
                Entries.add(new Entry(i, count));
                xlabels.add(String.valueOf(count));//xlabels 정의
                count++;
            }
        }
        else{
            for(float i : info){
                Entries.add(new Entry(i, count));// entry 삽입
                xlabels.add(String.valueOf(count));//xlabels 정의
                count++;
            }
        }


        //set the data
        LineDataSet DataSet = new LineDataSet(Entries, "usage");
        DataSet.setDrawFilled(true);
        DataSet.setDrawCubic(true);
        DataSet.setFillAlpha(255);
        int palegreen = ContextCompat.getColor(mContext, R.color.palegreen);
        DataSet.setFillColor(palegreen);
        DataSet.setColor(palegreen);
        DataSet.setDrawCircleHole(false);
        DataSet.setDrawCircles(false);
        DataSet.setDrawValues(false);

        LineData linedata = new LineData(xlabels, DataSet);
        linechart.setData(linedata);

        //chart animation 추가
        linechart.animateXY(3000,3000);
        linechart.invalidate();

        //기타 필요없는거
        linechart.setDescription("");
        linechart.setBackgroundColor(Color.TRANSPARENT);
        linechart.setDrawGridBackground(false);
        linechart.getAxisLeft().setDrawLabels(false);
        linechart.getLegend().setEnabled(false);
        linechart.getXAxis().setDrawGridLines(false);

        return linechart;
    }// end of DrawChart


    public Float[] onPassLineInfo (String url, Float[] info) throws ExecutionException, InterruptedException {

        Map urlinfo = new HashMap();
        urlinfo.put("url",url);
        urlinfo.put("selectedIP",selectedIP);
        String log = serverConnect.returnReply(urlinfo);
        //System.out.println("log = "+log);
        Gson gson = new Gson();
        info = gson.fromJson(log, Float[].class);
        return info;
    } //end of onPassLineInfo
    //onCreate에서 onPass 호출 -> NetworkTask 호출 -> asynctask return 값 받아보기!! 데이터 정형화

    public void onPassTvInfo(String url) throws ExecutionException, InterruptedException {
        Map urlinfo = new HashMap();
        urlinfo.put("url",url);
        urlinfo.put("selectedIP",selectedIP);
        String log  = null;
        log = serverConnect.returnReply(urlinfo);
        Gson gson = new Gson();
        exData data = gson.fromJson(log, exData.class);
        macad = data.getMac();
        ipad = data.getIp();
        hostname = data.getHostName();
    }// end of onPassTvInfo

    //1초에 한번씩 정보 받아오기!
    public void secTimer(){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getData();
            }
        };
        timer.schedule(task, 0, 1000);
    }


    //웹서버랑 연결해서 정보받아오는 메소드!
    public void getData() {
        try {
            onPassTvInfo("/basicinfo");
            cpuinfo = onPassLineInfo("/cpugraphinfo", cpuinfo);
            memoryinfo = onPassLineInfo("/memorygraphinfo",memoryinfo);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hostnametv.setText(hostname);
        iptv.setText(ipad);
        mactv.setText(macad);

        cpulineChart = DrawChart(cpuinfo, cpulineChart,"cpu");
        memorylineChart = DrawChart(memoryinfo, memorylineChart,"memory");
    }

}//end of class
