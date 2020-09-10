package com.kino.kreports.stats.staff;

import com.kino.kreports.stats.Stat;

public class BansStaff implements Stat {

    private int bans;

    public BansStaff() {
        this(0);
    }

    public BansStaff(int bans) {
        this.bans = bans;
    }


    @Override
    public int get() {
        return bans;
    }

    @Override
    public void add(int t) {
        bans += t;
    }

    @Override
    public void remove(int t) {
        bans -= t;
    }

    @Override
    public void set(int t) {
        bans = t;
    }
}
