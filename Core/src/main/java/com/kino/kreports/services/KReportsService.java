package com.kino.kreports.services;

import com.kino.kore.utils.service.Service;
import com.kino.kreports.loader.CommandsLoader;
import com.kino.kreports.loader.ListenerLoader;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.name.Named;

@InjectAll
public class KReportsService implements Service {

    private ListenerLoader listenerLoader;
    private CommandsLoader commandsLoader;

    @Named("users-service")
    private Service usersService;

    @Named("reports-service")
    private Service reportsService;

    @Override
    public void start() {
        listenerLoader.load();
        commandsLoader.load();

        usersService.start();
        reportsService.start();
    }

    @Override
    public void stop() {
        usersService.stop();
        reportsService.stop();
    }
}
