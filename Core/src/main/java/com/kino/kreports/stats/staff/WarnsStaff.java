package com.kino.kreports.stats.staff;

import com.kino.kreports.stats.Stat;

public class WarnsStaff implements Stat {

    private int warns;

    public WarnsStaff() {
        this(0);
    }

    public WarnsStaff(int warns) {
        this.warns = warns;
    }


    @Override
    public int get() {
        return warns;
    }

    @Override
    public void add(int t) {
        warns += t;
    }

    @Override
    public void remove(int t) {
        warns -= t;
    }

    @Override
    public void set(int t) {
        warns = t;
    }
}
