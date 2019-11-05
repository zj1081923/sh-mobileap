package com.example.myungjin.withusplanet.DataConnect;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class DrawChart {
    private int X_RANGE = 50;
    private int DATA_RANGE = 30;
    private ArrayList<Entry> cpuxVal, memxVal;
    private LineDataSet cpusetXcomp, memsetXcomp;
    private ArrayList<String> cpuxVals, memxVals;
    private ArrayList<LineDataSet> cpulineDataSets, memlineDataSets;
    private LineData cpulineData, memlineData;


    public LineChart chartInit(LineChart lineChart, String type){
        if(type.equals("cpu")){
            lineChart.setAutoScaleMinMaxEnabled(true);
            cpuxVal = new ArrayList<Entry>();
            cpusetXcomp.setColor(Color.RED);
            cpusetXcomp.setDrawValues(false);
            cpusetXcomp.setDrawCircles(false);
            cpusetXcomp.setAxisDependency(YAxis.AxisDependency.LEFT);
            cpulineDataSets = new ArrayList<LineDataSet>();
            cpulineDataSets.add(cpusetXcomp);
            cpuxVals = new ArrayList<String>();
            for(int i=0; i<X_RANGE; i++){
                cpuxVals.add("");
            }
            cpulineData = new LineData(cpuxVals, cpulineDataSets);
            lineChart.setData(cpulineData);
            lineChart.invalidate();
        }else if(type.equals("memory")){
            lineChart.setAutoScaleMinMaxEnabled(true);
            memxVal = new ArrayList<Entry>();
            memsetXcomp.setColor(Color.RED);
            memsetXcomp.setDrawValues(false);
            memsetXcomp.setDrawCircles(false);
            memsetXcomp.setAxisDependency(YAxis.AxisDependency.LEFT);
            memlineDataSets = new ArrayList<LineDataSet>();
            memlineDataSets.add(memsetXcomp);
            memxVals = new ArrayList<String>();
            for(int i=0; i<X_RANGE; i++){
                memxVals.add("");
            }
            memlineData = new LineData(memxVals, memlineDataSets);
            lineChart.setData(memlineData);
            lineChart.invalidate();
        }
        return lineChart;
    }

    public LineChart cpuchartUpdate(LineChart lineChart, Float x){
        if(cpuxVal.size()>DATA_RANGE){
            cpuxVal.remove(0);
            for(int i=0; i<DATA_RANGE; i++){
                cpuxVal.get(i).setXIndex(i);
            }
        }
        cpuxVal.add(new Entry(x, cpuxVal.size()));
        cpusetXcomp.notifyDataSetChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();

        return lineChart;
    }
    public LineChart memchartUpdate(LineChart lineChart, Float x){
        if(memxVal.size()>DATA_RANGE){
            memxVal.remove(0);
            for(int i=0; i<DATA_RANGE; i++){
                memxVal.get(i).setXIndex(i);
            }
        }
        memxVal.add(new Entry(x, memxVal.size()));
        memsetXcomp.notifyDataSetChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();

        return lineChart;
    }






}
