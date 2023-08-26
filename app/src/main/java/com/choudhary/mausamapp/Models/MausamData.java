package com.choudhary.mausamapp.Models;

import java.util.List;

public class MausamData {

    public List<Weather> weather;

    public  main main;

    public String name;

    public MausamData(String name) {
        this.name = name;
    }

    public MausamData(List<Weather> weather, com.choudhary.mausamapp.Models.main main) {
        this.weather = weather;
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public com.choudhary.mausamapp.Models.main getMain() {
        return main;
    }

    public void setMain(com.choudhary.mausamapp.Models.main main) {
        this.main = main;
    }
}

