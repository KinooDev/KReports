package com.kino.kreports.stats;

public interface Statistic {

    IntegerStat getBans();

    IntegerStat getReports();

    IntegerStat getWarns();

    IntegerStat getMutes();

    IntegerStat getKicks();
}
