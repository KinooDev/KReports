package com.kino.kreports.stats.staff;

import com.kino.kreports.stats.Stat;

public class KicksStaff implements Stat {

    private int kicks;

    public KicksStaff() {
        this(0);
    }

    public KicksStaff(int kicks) {
        this.kicks = kicks;
    }


    @Override
    public int get() {
        return kicks;
    }

    @Override
    public void add(int t) {
        kicks += t;
    }

    @Override
    public void remove(int t) {
        kicks -= t;
    }

    @Override
    public void set(int t) {
        kicks = t;
    }
}
