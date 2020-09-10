package com.kino.kreports.services;

import com.kino.kore.utils.service.Service;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.name.Named;

@InjectAll
public class KReportsService implements Service {


    @Named("users-service")
    private Service usersService;

    @Named("reports-service")
    private Service reportsService;

    @Override
    public void start() {
        usersService.start();
        reportsService.start();
    }

    @Override
    public void stop() {
        usersService.stop();
        reportsService.stop();
    }
}
