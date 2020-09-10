package com.kino.kreports.stats;

public class Bans implements Stat{

    private int bans;

    public Bans() {
        this(0);
    }

    public Bans(int bans) {
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
