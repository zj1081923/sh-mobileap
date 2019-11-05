package com.example.myungjin.withusplanet.DataConnect;

public class SimData {
    private Float[] cpu;
    private Float[] ram;

    public Float[] getCpu(){
        return cpu;
    }
    public Float[] getRam(){
        return ram;
    }
    public void setCpu(Float[] cpu){
        this.cpu = cpu;
    }
    public void setRam(Float[] ram){
        this.ram = ram;
    }
}