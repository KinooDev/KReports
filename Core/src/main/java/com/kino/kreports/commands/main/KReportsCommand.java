package com.kino.kreports.commands.main;

import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.messages.MessageUtils;
import com.kino.kore.utils.service.Service;
import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.storage.user.SimpleUser;
import com.kino.kreports.storage.user.Staff;
import com.kino.kreports.storage.user.User;
import me.fixeddev.ebcm.parametric.CommandClass;
import me.fixeddev.ebcm.parametric.annotation.ACommand;
import me.fixeddev.ebcm.parametric.annotation.Injected;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.name.Named;

import java.util.UUID;

@InjectAll
@ACommand(names = {"kreports"}, desc = "Main command of the plugin", permission = "kreports.commands.main")
public class KReportsCommand implements CommandClass {

    private Storage<UUID, User> userStorage;

    @Inject
    @Named("kreports-service")
    private Service kreportsService;

    @Named("config")
    private YMLFile config;

    @Named("messages")
    private YMLFile messages;

    @Named("user_data")
    private YMLFile playerData;

    @Named("reports_data")
    private YMLFile reportsData;

    @ACommand(names = {"setstaff", "promote", "addstaff"}, desc = "Set a player as a staff, to has staffs' stats", permission = "kreports.commands.main.setstaff")
    public boolean executeSetStaff (@Injected(true) CommandSender sender, String name) {
        Player p = Bukkit.getPlayer(name);

        if (p !=null && p.isOnline() && p.hasPermission("kreports.staff") && userStorage.find(p.getUniqueId()).isPresent()) {

            if (!(userStorage.find(p.getUniqueId()).get() instanceof Staff) && userStorage.find(p.getUniqueId()).get() instanceof SimpleUser) {

                Staff s = new Staff((SimpleUser) userStorage.find(p.getUniqueId()).get());

                userStorage.remove(p.getUniqueId());
                userStorage.get().put(p.getUniqueId(), s);

                MessageUtils.sendMessage(p, messages.getString("staffSet"));

                Bukkit.getOnlinePlayers().forEach(player -> {
                    if(player.hasPermission("kreports.staff")) {
                        MessageUtils.sendMessage(player, messages.getString("staffSetBroadcast").replace("<player>", p.getName()));
                    }});

                return true;
            }
        }
        return false;
    }

    @ACommand(names = {"unsetstaff", "demote", "removestaff"}, desc = "Set a player as a user, to remove him staffs' stats", permission = "kreports.commands.main.unsetstaff")
    public boolean executeUnSetStaff (@Injected(true) CommandSender sender, String name) {
        Player p = Bukkit.getPlayer(name);

        if ((p !=null && p.isOnline() && userStorage.find(p.getUniqueId()).isPresent())) {

            if ((userStorage.find(p.getUniqueId()).get() instanceof Staff)) {

                SimpleUser simpleUser = new SimpleUser(userStorage.find(p.getUniqueId()).get());

                userStorage.remove(p.getUniqueId());
                userStorage.get().put(p.getUniqueId(), simpleUser);

                MessageUtils.sendMessage(p, messages.getString("staffUnSet"));

                Bukkit.getOnlinePlayers().forEach(player -> {
                    if(player.hasPermission("kreports.staff")) {
                        MessageUtils.sendMessage(player, messages.getString("staffUnSetBroadcast").replace("<player>", p.getName()));
                    }});

                return true;
            }
        }
        return false;
    }

    @ACommand(names = {"reload", "rl"}, desc = "Reload the plugin", permission = "kreports.commands.main.reload")
    public boolean executeReload (@Injected(true) CommandSender sender) {
        kreportsService.stop();
        kreportsService.start();
        config.reload();
        messages.reload();
        playerData.reload();
        reportsData.reload();
        return true;
    }
}
