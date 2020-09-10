package com.kino.kreports.stats;

public interface Stat {

    int get();

    void add(int t);

    void remove(int t);

    void set(int t);
}
