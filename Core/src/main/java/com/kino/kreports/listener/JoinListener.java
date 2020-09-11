package com.kino.kreports.listener;

import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.storage.user.SimpleUser;
import com.kino.kreports.storage.user.Staff;
import com.kino.kreports.storage.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import team.unnamed.inject.InjectAll;

import java.util.UUID;

@InjectAll
public class JoinListener implements Listener {


    private Storage<UUID, User> userStorage;

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        User user = userStorage.findFromData(player.getUniqueId()).orElseGet(player.hasPermission("kreports.staff") ? Staff::new : SimpleUser::new);

        userStorage.add(player.getUniqueId(), user);
    }
}
