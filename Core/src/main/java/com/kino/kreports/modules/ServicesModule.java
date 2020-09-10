package com.kino.kreports.modules;

import com.kino.kore.utils.service.Service;
import com.kino.kreports.services.KReportsService;
import com.kino.kreports.services.ReportsService;
import com.kino.kreports.services.UserService;
import team.unnamed.inject.bind.AbstractModule;

public class ServicesModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(Service.class).named("kreports-service").to(KReportsService.class).singleton();
        bind(Service.class).named("users-service").to(UserService.class).singleton();
        bind(Service.class).named("reports-service").to(ReportsService.class).singleton();
    }
}
