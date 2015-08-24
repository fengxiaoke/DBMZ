package com.anniemal.model;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2015/8/18.
 */
public class Data extends Application {

    private String[] jokeTitle;
    private String[] JokeId;
    private List<CosImageInfo> cs;
    private int s;

    @Override
    public void onCreate() {
        s = 0;
        jokeTitle = new String[s];
        JokeId = new String[s];
        cs = new ArrayList<>();
        super.onCreate();
    }

    public String[] getJokeId() {
        return JokeId;
    }

    public void setJokeId(String[] jokeId) {
        JokeId = jokeId;
    }

    public String[] getJokeTitle() {
        return jokeTitle;
    }

    public void setJokeTitle(String[] jokeTitle) {
        this.jokeTitle = jokeTitle;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public List<CosImageInfo> getCs() {
        return cs;
    }

    public void setCs(List<CosImageInfo> cs) {
        this.cs = cs;
    }
}
