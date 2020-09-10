package com.kino.kreports.stats;

public class Mutes implements Stat{

    private int mutes;

    public Mutes() {
        this(0);
    }

    public Mutes(int mutes) {
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
