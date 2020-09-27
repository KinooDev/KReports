package com.kino.kreports.utils;

import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.models.reports.Report;
import com.kino.kreports.models.user.Staff;
import com.kino.kreports.models.user.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.name.Named;

import java.util.UUID;

@InjectAll
public class ReportUtils {


    private Storage<UUID, User> playerStorage;



    @Named("messages")
    private YMLFile messages;

    public void broadcast (Report report) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (playerStorage.find(online.getUniqueId()).isPresent() && playerStorage.find(online.getUniqueId()).get() instanceof Staff) {
                send(online, report);
            }
        }
    }

    public void send (Player p, Report report) {
        if (p.hasPermission("kreports.staff.info")) {
            for (String s : messages.getStringList("report.info")) {
                p.sendMessage(s.replace("<reported>", Bukkit.getPlayer(report.getReported()).getName() == null ?
                        "invalid-user" : Bukkit.getPlayer(report.getReported()).getName())
                        .replace("<reporter>",
                                Bukkit.getPlayer(report.getReporter()).getName() == null ?
                                "invalid-user" : Bukkit.getPlayer(report.getReporter()).getName())
                        .replace("<reports>", playerStorage.find(report.getReported()).isPresent() ?
                                playerStorage.find(report.getReported()).get().getReports().get() + "" : playerStorage.findFromData(report.getReported()).isPresent() ?
                                playerStorage.findFromData(report.getReported()).get().getReports().get() + "" : "<error>")
                        .replace("<reason>", report.getReason()));

            }
            Player player = Bukkit.getPlayer(report.getReported());
            TextComponent text = new TextComponent();
            text.setText(ChatColor.translateAlternateColorCodes('&', messages.getString("report.teleportMessage.text").replace("<player>", player.getName())));
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messages.getString("report.teleportMessage.hoverText")
                    .replace("<player>", player.getName())
                    .replace("<world>", player.getWorld().getName())).create()));
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + p.getName() + " " + player.getName()));
            p.spigot().sendMessage(text);
        }
    }

    public void addComment (CommandSender sender, String s, Report report) {
        report.getComments().add(messages.getString("report.comments").replace("<name>", sender instanceof Player ? sender.getName() : "Console").replace("<comment>", s));
    }
}
