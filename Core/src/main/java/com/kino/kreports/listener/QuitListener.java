package com.kino.kreports.listener;

import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.models.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import team.unnamed.inject.InjectAll;

import java.util.UUID;

@InjectAll
public class QuitListener implements Listener {


    private Storage<UUID, User> userStorage;


    @EventHandler
    public void onQuit (PlayerQuitEvent e) {
        Player p = e.getPlayer();

        userStorage.save(p.getUniqueId());
    }
}
