package com.kino.kreports.stats;

public class IntegerStat implements Stat<Integer> {

    private int stat;

    public IntegerStat() {
        this(0);
    }

    public IntegerStat(int stat) {
        this.stat = stat;
    }

    @Override
    public int get() {
        return stat;
    }

    @Override
    public void add(int t) {
        stat += t;
    }

    @Override
    public void remove(int t) {
        stat -= t;
    }
}
