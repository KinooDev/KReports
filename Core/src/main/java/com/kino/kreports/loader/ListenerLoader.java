package com.kino.kreports.loader;

import com.kino.kore.utils.loaders.Loader;
import com.kino.kreports.KReports;
import com.kino.kreports.listener.JoinListener;
import com.kino.kreports.listener.QuitListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import team.unnamed.inject.InjectAll;

@InjectAll
public class ListenerLoader implements Loader {

    private JoinListener joinListener;
    private QuitListener quitListener;
    private KReports plugin;

    private void registerListeners (Listener...listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    @Override
    public void load() {
        registerListeners(joinListener, quitListener);
    }
}
