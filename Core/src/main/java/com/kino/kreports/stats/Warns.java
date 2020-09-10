package com.kino.kreports.stats;

public class Warns implements Stat{

    private int warns;

    public Warns() {
        this(0);
    }

    public Warns(int warns) {
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
