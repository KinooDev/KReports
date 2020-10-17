package com.kino.kreports.utils.report;

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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.name.Named;

import java.text.SimpleDateFormat;
import java.util.*;

@InjectAll
public class ReportUtils {


    private Storage<UUID, User> playerStorage;

    private Storage<UUID, Report> reportStorage;

    @Named("config")
    private YMLFile config;

    @Named("messages")
    private YMLFile messages;

    @Named("reports_data")
    private YMLFile reportsData;

    private List<String> format (Report report, boolean comments) {
        List<String> formatter = messages.getStringList("report.format.info");

        for (String sReportUUID : reportsData.getConfigurationSection("reports").getKeys(false)) {
            UUID uuid = UUID.fromString(sReportUUID);
            if (reportStorage.findFromData(uuid).isPresent()) {
                if (reportStorage.findFromData(uuid).get().equals(report)) {
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
                        } else if (i <= report.getComments().size() - 1) {
                            builder.append(". ");
                        } else {
                            builder.append("; ");
                        }
                    }

                    if (comments && !report.getComments().isEmpty()) {
                        formatter.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s).replace(
                                "<comments>", builder.toString()));
                    } else {
                        List<String> list = new ArrayList<>();

                        for (String s : formatter) {
                            if (ChatColor.stripColor(s).equals("<comments>")) {
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
        for (Report report : sortReports(p)) {
            for (String s : format(report, comments)) {
                if (receiver instanceof Player) {
                    Player player = (Player) receiver;
                    TextComponent text = new TextComponent();
                    text.setText(ChatColor.translateAlternateColorCodes('&', s));
                    text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messages.getString("report.format.hoverText")).create()));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, fromReport(report).toString()));
                    player.spigot().sendMessage(text);
                } else {
                    MessageUtils.sendMessage(receiver, s);
                }
            }
        }
    }

    public List<Report> sortReports (OfflinePlayer p) {
        List<Report> list = new ArrayList<>();

        for (String sReportUUID : reportsData.getConfigurationSection("reports").getKeys(false)) {
            UUID reportUUID = UUID.fromString(sReportUUID);
            if (reportStorage.findFromData(reportUUID).isPresent()) {
                Report report = reportStorage.findFromData(reportUUID).get();
                if (report.getReported().equals(p.getUniqueId())) {
                    list.add(report);
                }
            }
        }

        list.sort(new ReportComparator());

        return list;
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

    public void sendCommentsOfReport (UUID uuid, CommandSender receiver) {
        sendCommentsListHeader(uuid, receiver);
        MessageUtils.sendMessage(receiver, formatComments(fromUUID(uuid)));
    }

    private void sendCommentsListHeader(UUID uuid, CommandSender receiver) {
        String comments;
        Report report = fromUUID(uuid);
        if (report !=null) {
            comments = report.getComments().size() + "";
        } else {
            comments = "error";
        }

        MessageUtils.sendMessage(receiver, messages.getString("report.format.comments.header").replace(
                "<report>", uuid.toString()).replace(
                "<comments>", comments));
    }

    private String formatComments (Report report) {
        if (report !=null) {
            StringBuilder builder = new StringBuilder();

            //TODO: PAGE SYSTEM

            String commentFormatter = messages.getString("report.format.comments.base");
            for (int i = 0; i < report.getComments().size(); i++) {
                builder.append(commentFormatter.replace("<id>", (i + 1) + "").replace("<comment>", report.getComments().get(i)));

                if (i == report.getComments().size() - 1) {
                    builder.append(".");
                } else {
                    builder.append(", ");
                }
            }

            return builder.toString();
        } else {
            return "error";
        }
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

    public void accept (Player p, Report report, UUID uuid) {
        report.accept();
        report.setAccepter(p.getUniqueId());
        MessageUtils.sendMessage(p, messages.getString("report.accept").replace("<uuid>", uuid.toString()));
    }

    public void unaccept (Player p, Report report, UUID uuid) {
        report.unaccept();
        report.setAccepter(null);
        MessageUtils.sendMessage(p, messages.getString("report.unaccept").replace("<uuid>", uuid.toString()));
    }

    public void addInspector (Player p, Report report, UUID uuid) {
        for (UUID uuid1 : getAllReports().keySet()) {
            if (getAllReports().get(uuid1).getStaffInspection().contains(p.getUniqueId())) {
                MessageUtils.sendMessage(p, messages.getString("report.inspection.alreadyInspecting").replace("<uuid>", uuid1.toString()));
                return;
            }
        }

        if (config.getInt("reports.maxInspectors") !=-1) {
            if (report.getStaffInspection().size() >= config.getInt("reports.maxInspectors")) {
                MessageUtils.sendMessage(p, messages.getString("report.inspection.maxInspectorsReached").replace("<uuid>", uuid.toString()));
                return;
            }
        }

        report.addInspector(p.getUniqueId());
        if ((Bukkit.getPlayer(report.getReported()) !=null) && (Bukkit.getPlayer(report.getReported()).isOnline())) {
            p.teleport(Bukkit.getPlayer(report.getReported()).getLocation());
        }
        MessageUtils.sendMessage(p, messages.getString("report.inspection.nowInspecting").replace("<uuid>", uuid.toString()));
    }

    public void removeInspector (Player p, Report report, UUID uuid) {

        if (report.getStaffInspection().contains(p.getUniqueId())) {
            report.removeInspector(p.getUniqueId());
            MessageUtils.sendMessage(p, messages.getString("report.inspection.stopInspecting").replace("<uuid>", uuid.toString()));
        } else {
            MessageUtils.sendMessage(p, messages.getString("report.inspection.notInspecting").replace("<uuid>", uuid.toString()));
        }

    }

    public Report fromUUID (UUID uuid) {

        return reportStorage.find(uuid).orElse(reportStorage.findFromData(uuid).orElse(null));
    }

    public UUID fromReport (Report report) {
        for (String sReportUUID : reportsData.getConfigurationSection("reports").getKeys(false)) {
            UUID reportUUID = UUID.fromString(sReportUUID);
            if (reportStorage.findFromData(reportUUID).isPresent()) {
                Report report1 = reportStorage.findFromData(reportUUID).get();
                if (report.equals(report1)) {
                    return reportUUID;
                }
            }
        }

        return null;
    }

    public Map<UUID, Report> getAllReports () {
        Map<UUID, Report> reports = new HashMap<>();
        for (String sReportUUID : reportsData.getConfigurationSection("reports").getKeys(false)) {
            UUID uuid = UUID.fromString(sReportUUID);
            if (reportStorage.findFromData(uuid).isPresent()) {
                reports.put(uuid, reportStorage.findFromData(uuid).get());
            }
        }
        for (UUID uuid1 : reportStorage.get().keySet()) {
            if (reportStorage.find(uuid1).isPresent()) {
                reports.put(uuid1, reportStorage.find(uuid1).get());
            }
        }
        return reports;
    }
}
