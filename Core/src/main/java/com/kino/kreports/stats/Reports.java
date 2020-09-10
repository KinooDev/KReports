package com.kino.kreports.stats;

public class Reports implements Stat {

    private int reports;

    public Reports() {
        this(0);
    }

    public Reports(int reports) {
        this.reports = reports;
    }


    @Override
    public int get() {
        return reports;
    }

    @Override
    public void add(int t) {
        reports += t;
    }

    @Override
    public void remove(int t) {
        reports -= t;
    }

    @Override
    public void set(int t) {
        reports = t;
    }
}
