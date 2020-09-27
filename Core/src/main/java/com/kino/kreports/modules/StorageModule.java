package com.kino.kreports.modules;

import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.models.reports.Report;
import com.kino.kreports.storage.reports.ReportStorageManager;
import com.kino.kreports.models.user.User;
import com.kino.kreports.storage.user.UserStorageManager;
import team.unnamed.inject.bind.AbstractModule;
import team.unnamed.inject.identity.Key;

import java.util.UUID;

public class StorageModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(new Key<Storage<UUID, User>>() {}).to(UserStorageManager.class).singleton();
        bind(new Key<Storage<UUID, Report>>() {}).to(ReportStorageManager.class).singleton();
    }
}
