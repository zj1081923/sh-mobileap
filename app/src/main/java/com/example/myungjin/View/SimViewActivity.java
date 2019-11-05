package com.example.myungjin.withusplanet.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myungjin.withusplanet.DataConnect.ServerConnect;
import com.example.myungjin.withusplanet.DataConnect.SimData;
import com.example.myungjin.withusplanet.DataConnect.exData;
import com.example.myungjin.withusplanet.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
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

public class SimViewActivity extends AppCompatActivity {
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
    private String selectedPort;
    private Timer timer;
    private ArrayList<Float> cpuinfo;
    private ArrayList<Float> memoryinfo;
    private Float[] cpuusage, memusage;
    //private SimData simData;
    private Handler mHandler;
    int value = 0;
    int DATA_RANGE = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_view);
        getSupportActionBar().hide();
        final TextView idtx = (TextView)findViewById(R.id.textView);
        final String userID = getIntent().getStringExtra("id");
        selectedIP = getIntent().getStringExtra("ip");
        selectedPort = getIntent().getStringExtra("port");

        idtx.setText("ID | " + userID);

        cpuinfo = new ArrayList<>();
        memoryinfo = new ArrayList<>();

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


        mHandler = new Handler(){
            public void handleMessage(Message msg){
                getData();
                System.out.println(System.currentTimeMillis()/1000);
                mHandler.sendEmptyMessageDelayed(0,5000);
            }
        };
        mHandler.sendEmptyMessage(0);


    }
    public LineChart DrawChart(ArrayList info,LineChart linechart, String type){
        ArrayList<String> xlabels = new ArrayList<String>();
        List<Entry> Entries = new ArrayList<>();
        int count=0;
        for(Object i : info){
            Entries.add(new Entry((Float)i,count));
            xlabels.add(String.valueOf(count));
            count++;
        }
        /*while(count< DATA_RANGE){
            Entries.add(new Entry(0, count));
            xlabels.add(String.valueOf(count));
            count++;
        }*/
        //System.out.println("Entries : "+Entries);
        /*int size = info.size();
        if(size<DATA_RANGE){
            while(Entries.size()<DATA_RANGE-size)
            Entries.add(new Entry(0, count));
            xlabels.add("");
            count++;
        }
        for(Object i : info){
            Entries.add(new Entry((Float)i, count));
            xlabels.add("");
            count++;
        }*/

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
        DataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineData linedata = new LineData(xlabels, DataSet);
        linechart.setData(linedata);

        //chart animation 추가
        //linechart.animateXY(3000,3000);
        //linechart.notifyDataSetChanged();
        linechart.invalidate();

        //기타 필요없는거
        linechart.setDescription("");
        linechart.setBackgroundColor(Color.TRANSPARENT);
        linechart.setDrawGridBackground(false);
        //linechart.getAxisLeft().setAxisMaxValue(100f);
        //linechart.getAxisLeft().setAxisMinValue(0f);
        linechart.getAxisLeft().setDrawLabels(false);
        linechart.getLegend().setEnabled(false);
        linechart.getXAxis().setDrawGridLines(false);

        return linechart;
    }// end of DrawChart

    //hostname, ip, mac주소 받아오는 메소드
    public void onPassTvInfo(String url) {
        Map urlinfo = new HashMap();
        urlinfo.put("url",url);
        urlinfo.put("ip",selectedIP);
        urlinfo.put("port",selectedPort);
        String log  = null;
        try {
            log = serverConnect.returnReply(urlinfo);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        exData data = gson.fromJson(log, exData.class);
        macad = data.getMac();
        ipad = data.getIp();
        hostname = data.getHostName();
    }// end of onPassTvInfo

    //cpu, memory 받아오는 메소드 (class 반환)
    public SimData onPassLineInfo(String url){
        Map urlinfo = new HashMap();
        urlinfo.put("url", url);
        urlinfo.put("ip",selectedIP);
        urlinfo.put("port",selectedPort);
        String log = null;
        try {
            log = serverConnect.returnReply(urlinfo);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();

        SimData data = gson.fromJson(log, SimData.class);
        return data;
    }

    public void getData(){
        SimData simCpu, simMem;
        onPassTvInfo("/siminfo");
        simCpu = onPassLineInfo("/simCPU");
        simMem = onPassLineInfo("/simRAM");
        hostnametv.setText(hostname);
        iptv.setText(ipad);
        mactv.setText(macad);

        cpuusage = simCpu.getCpu();
        memusage = simMem.getRam();
        //System.out.println("cpu : "+cpuusage);
        //System.out.println("mem : "+memusage);

        for(int i=0;i<5;i++) {
            if (cpuinfo.size() >= DATA_RANGE) {
                cpuinfo.remove(0);
                cpuinfo.add(cpuusage[i]);
            } else {
                cpuinfo.add(cpuusage[i]);
            }
            if (memoryinfo.size() >= DATA_RANGE) {
                memoryinfo.remove(0);
                memoryinfo.add(memusage[i]);
            } else {
                memoryinfo.add(memusage[i]);
            }
            cpulineChart = DrawChart(cpuinfo, cpulineChart, "cpu");
            memorylineChart = DrawChart(memoryinfo, memorylineChart, "memory");
        }

    }//getData end

}
