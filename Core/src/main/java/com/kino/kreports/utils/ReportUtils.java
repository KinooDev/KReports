package com.kino.kreports.utils;

import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.messages.MessageUtils;
import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.models.reports.Report;
import com.kino.kreports.models.user.Staff;
import com.kino.kreports.models.user.User;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.name.Named;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@InjectAll
public class ReportUtils {


    private Storage<UUID, User> playerStorage;

    private Storage<UUID, Report> reportStorage;

    @Named("messages")
    private YMLFile messages;

    public List<String> format (Report report, boolean comments) {
        List<String> formatter = messages.getStringList("report.format.info");

        for (UUID uuid : reportStorage.get().keySet()) {
            if (reportStorage.find(uuid).isPresent()){
                if (reportStorage.find(uuid).get().equals(report)) {
                    formatter.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s.replace(
                            "<uuid>", uuid.toString()
                            ).replace(
                                    "<date>", new SimpleDateFormat("MMM dd,yyyy HH:mm").format(report.getDate())
                    ).replace(
                            "<reporter>", Bukkit.getOfflinePlayer(report.getReporter()).getName()
                    ).replace(
                            "<reason>", report.getReason()
                    ).replace(
                            "<state>", report.getState().name().toUpperCase()
                    ).replace(
                            "<priority>", report.getPriority().name().toUpperCase()
                    ).replace(
                            "<player>", Bukkit.getOfflinePlayer(report.getReported()).getName()
                    )));

                    StringBuilder builder = new StringBuilder();

                    String commentFormatter = messages.getString("report.format.comments.base");
                    for (int i = 0; i < report.getComments().size(); i++) {
                        builder.append(commentFormatter.replace("<id>", (i + 1) + "").replace("<comment>", report.getComments().get(i)));

                        if (i == 3) {
                            builder.append(". ").append(messages.getString("report.format.comments.seeMore"));
                        } else {
                            builder.append("; ");
                        }
                    }

                    if(comments && !report.getComments().isEmpty()) {
                        formatter.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s).replace(
                                "<comments>", builder.toString()));
                    } else {
                        List<String> list = new ArrayList<>();

                        for (String s : formatter) {
                            if(ChatColor.stripColor(s).equals("<comments>")) {
                                continue;
                            }

                            list.add(s.replace("<comments>", ""));
                        }
                        formatter = list;
                    }
                }
            }
        }

        return formatter;
    }



    public void sendReportsOfPlayer (OfflinePlayer p, CommandSender receiver, boolean comments) {
        sendReportListHeader(p, receiver);
        for (UUID reportUUID : reportStorage.get().keySet()) {
            if (reportStorage.find(reportUUID).isPresent()){
                Report report = reportStorage.find(reportUUID).get();
                if (report.getReported().equals(p.getUniqueId())) {
                    for (String s : format(report, comments)) {
                        if (receiver instanceof Player) {
                            Player player = (Player) receiver;
                            TextComponent text = new TextComponent();
                            text.setText(ChatColor.translateAlternateColorCodes('&', s));
                            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messages.getString("report.format.hoverText")).create()));
                            text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, reportUUID.toString()));
                            player.spigot().sendMessage(text);
                        } else {
                            MessageUtils.sendMessage(receiver, s);
                        }
                    }
                }
            }
        }
    }

    private void sendReportListHeader (OfflinePlayer p, CommandSender receiver) {
        String reports;
        if (playerStorage.find(p.getUniqueId()).isPresent()) {
            reports = playerStorage.find(p.getUniqueId()).get().getReports().get() + "";
        } else {
            if (playerStorage.findFromData(p.getUniqueId()).isPresent()) {
                reports = playerStorage.findFromData(p.getUniqueId()).get().getReports().get() + "";
            } else {
                reports = "error";
            }
        }

        MessageUtils.sendMessage(receiver, messages.getString("report.format.header").replace(
                "<player>", p.getName()).replace(
                "<reports>", reports));
    }

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
        MessageUtils.sendMessage(sender, messages.getString("report.addComment"));
    }

    public void changePriority (CommandSender sender, Report report, ReportPriority reportPriority, UUID uuid) {
        ReportPriority old = report.getPriority();
        report.setPriority(reportPriority);
        MessageUtils.sendMessage(sender, messages.getString("report.changePriority").replace("<uuid>", uuid.toString()).replace("<priority>",
                old.name()).replace("<newpriority>", reportPriority.name()));
    }

    public void changeState (CommandSender sender, Report report, ReportState state, UUID uuid) {
        ReportPriority old = report.getPriority();
        report.setState(state);
        MessageUtils.sendMessage(sender, messages.getString("report.changeState").replace("<uuid>", uuid.toString()).replace("<state>",
                old.name()).replace("<newstate>", state.name()));
    }
}
