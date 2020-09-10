package com.kino.kreports.stats.staff;

import com.kino.kreports.stats.Stat;

public class ReportsStaff implements Stat {

    private int reports;

    public ReportsStaff() {
        this(0);
    }

    public ReportsStaff(int reports) {
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
