package com.kino.kreports.stats;

public interface Stat<T extends Number> {

    int get();

    void add(int t);

    void remove(int t);
}
