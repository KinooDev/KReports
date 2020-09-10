package com.kino.kreports.stats;

public interface Statistic {

    Bans getBans();

    Reports getReports();

    Warns getWarns();

    Mutes getMutes();

    Kicks getKicks();
}
