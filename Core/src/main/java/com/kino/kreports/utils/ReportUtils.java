package com.kino.kreports.utils;

import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.storage.reports.Report;
import com.kino.kreports.storage.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;
import team.unnamed.inject.name.Named;

import java.util.UUID;

public class ReportUtils {

    @Inject
    private Storage<UUID, User> playerStorage;


    @Inject
    @Named("messages")
    private YMLFile messages;

    public void send (Player p, Report report) {
        if (p.hasPermission("kreports.staff.info")) {
            for (String s : messages.getStringList("report.info")) {
                p.sendMessage(s.replace("<reported>", Bukkit.getPlayer(report.getReported()).getName() == null ?
                        "invalid-user" : Bukkit.getPlayer(report.getReported()).getName())
                        .replace("<reporter>",
                                Bukkit.getPlayer(report.getReporter()).getName() == null ?
                                "invalid-user" : Bukkit.getPlayer(report.getReporter()).getName())
                        .replace("<reports>", playerStorage.find(report.getReported()).isPresent() ?
                                playerStorage.find(report.getReported()).get().getReports() + "" : playerStorage.findFromData(report.getReported()).isPresent() ?
                                playerStorage.findFromData(report.getReported()).get().getReports() + "" : "<error>")
                        .replace("<reason>", report.getReason()));
            }
        }
    }

    public void addComment (CommandSender sender, String s, Report report) {
        report.getComments().add(messages.getString("report.comments").replace("<name>", sender instanceof Player ? sender.getName() : "Console").replace("<comment>", s));
    }
}
