package com.kino.kreports.stats;

public class Kicks implements Stat{

    private int kicks;

    public Kicks() {
        this(0);
    }

    public Kicks(int kicks) {
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
