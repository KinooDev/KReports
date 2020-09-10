package com.kino.kreports.stats.staff;

import com.kino.kreports.stats.Stat;

public class MutesStaff implements Stat {

    private int mutes;

    public MutesStaff() {
        this(0);
    }

    public MutesStaff(int mutes) {
        this.mutes = mutes;
    }


    @Override
    public int get() {
        return mutes;
    }

    @Override
    public void add(int t) {
        mutes += t;
    }

    @Override
    public void remove(int t) {
        mutes -= t;
    }

    @Override
    public void set(int t) {
        mutes = t;
    }
}
