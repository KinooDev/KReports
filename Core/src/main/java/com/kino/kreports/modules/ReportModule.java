package com.kino.kreports.modules;

import com.kino.kreports.utils.report.ReportUtils;
import team.unnamed.inject.bind.AbstractModule;

public class ReportModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(ReportUtils.class).singleton();
    }
}
