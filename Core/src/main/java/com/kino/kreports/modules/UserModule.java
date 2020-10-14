package com.kino.kreports.modules;

import com.kino.kreports.utils.report.ReportUtils;
import com.kino.kreports.utils.user.UserUtils;
import team.unnamed.inject.bind.AbstractModule;

public class UserModule  extends AbstractModule {


    @Override
    protected void configure() {
        bind(UserUtils.class).singleton();
    }
}
