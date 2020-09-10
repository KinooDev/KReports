package com.kino.kreports.services;

import com.kino.kore.utils.service.Service;
import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.storage.user.User;
import team.unnamed.inject.Inject;

import java.util.UUID;

public class UserService implements Service {

    @Inject
    private Storage<UUID, User> userStorage;

    @Override
    public void start() {
        userStorage.loadAll();
    }

    @Override
    public void stop() {
        userStorage.saveAll();
    }
}
