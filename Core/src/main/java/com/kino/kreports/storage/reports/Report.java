package com.kino.kreports.storage.reports;

import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.storage.user.Staff;
import com.kino.kreports.storage.user.User;
import com.kino.kreports.utils.ReportPriority;
import com.kino.kreports.utils.ReportState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;
import team.unnamed.inject.name.Named;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
public class Report implements ConfigurationSerializable {

    @Inject
    @Named("config")
    private YMLFile config;

    @Inject
    @Named("messages")
    private YMLFile messages;

    @Inject
    private Storage<UUID, User> playerStorage;

    @Getter @Setter private ReportPriority priority;
    @Getter @Setter private ReportState state;

    @Getter private final User reported;
    @Getter private final User reporter;

    @Getter @Setter private String reason;

    @Getter @Setter private boolean accepted = false;

    @Getter @Setter private List<String> comments;
    @Getter @Setter private List<Staff> staffInspection;

    @Getter private final Date date;

    Report (User reported, User reporter, String reason) {
        this.priority = ReportPriority.MEDIUM;
        this.state = ReportState.PAUSED;

        this.reported = reported;
        this.reporter = reporter;

        this.reason = reason;

        this.comments = new ArrayList<>();
        this.staffInspection = new ArrayList<>();

        this.date = new Date(System.currentTimeMillis());
    }

    Report (User reported, User reporter) {
        this (reported, reporter, "");
        this.reason = config.getString("reports.defaultReason");
    }

    Report (Map<String, Object> map) {
        this.priority = (ReportPriority) map.get("priority");
        this.state = (ReportState) map.get("state");

        this.reported = playerStorage.find((UUID) map.get("reported")).isPresent() ? playerStorage.find((UUID) map.get("reported")).get()
                : playerStorage.findFromData((UUID) map.get("reported")).isPresent() ? playerStorage.findFromData((UUID) map.get("reported")).get() : null;
        this.reporter = playerStorage.find((UUID) map.get("reporter")).isPresent() ? playerStorage.find((UUID) map.get("reporter")).get()
                : playerStorage.findFromData((UUID) map.get("reporter")).isPresent() ? playerStorage.findFromData((UUID) map.get("reporter")).get() : null;

        this.reason = (String) map.get("reason");

        this.comments = (List<String>) map.get("comments");

        this.staffInspection = new ArrayList<>();
        List<String> uuids = (List<String>) map.get("staffInspection");
        for (String s : uuids) {
            this.staffInspection.add(playerStorage.find(UUID.fromString(s)).isPresent() ? (Staff) playerStorage.find(UUID.fromString(s)).get() :
                    playerStorage.findFromData(UUID.fromString(s)).isPresent() ? (Staff) playerStorage.findFromData(UUID.fromString(s)).get() : null);
        }

        Date date1;

        try {
            date1 = new SimpleDateFormat("MMM dd,yyyy HH:mm").parse((String) map.get("date"));
        } catch (ParseException e) {
            e.printStackTrace();
            date1 = new Date(System.currentTimeMillis());
        }
        this.date = date1;
    }

    public void send (Player p) {
        if (p.hasPermission("kreports.staff.info")) {
            for (String s : messages.getStringList("report.info")) {
                p.sendMessage(s.replace("<reported>", playerStorage.getKey(reported).isPresent() ?
                        Bukkit.getPlayer(playerStorage.getKey(reported).get()).getName() : playerStorage.getKeyFromData(reported).isPresent() ?
                        Bukkit.getPlayer(playerStorage.getKeyFromData(reported).get()).getName() :
                        "invalid-user")
                .replace("<reporter>", playerStorage.getKey(reporter).isPresent() ?
                        Bukkit.getPlayer(playerStorage.getKey(reporter).get()).getName() : playerStorage.getKeyFromData(reporter).isPresent() ?
                        Bukkit.getPlayer(playerStorage.getKeyFromData(reporter).get()).getName() :
                        "invalid-user")
                .replace("<reports>", reported.getReports() + "")
                .replace("<reason>", reason));
            }
        }
    }

    public void addInspector (Player p) {
        if (p.hasPermission("kreports.staff.inspect")) {
            Staff staff = (Staff) playerStorage.find(p.getUniqueId()).orElse(playerStorage.findFromData(p.getUniqueId()).isPresent() ? playerStorage.findFromData(p.getUniqueId()).get() : null);
            if (staff != null) {
                staffInspection.add(staff);
            }
        }
    }

    public void accept () {
        this.accepted = true;
        this.state = ReportState.ARCHIVED;
    }

    public void addComment (CommandSender sender, String s) {
        this.comments.add(messages.getString("report.comments").replace("<name>", sender instanceof Player ? sender.getName() : "Console").replace("<comment>", s));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> reportMap = new LinkedHashMap<>();

        reportMap.put("priority", priority);
        reportMap.put("state", state);
        reportMap.put("reported", playerStorage.getKey(reported));
        reportMap.put("reporter", playerStorage.getKey(reporter));
        reportMap.put("reason", reason);
        reportMap.put("comments", comments);

        List<String> uuids = new ArrayList<>();
        for (Staff s : staffInspection) {
            uuids.add(playerStorage.getKey(s) + "");
        }

        reportMap.put("staffInspection", uuids);
        reportMap.put("date", new SimpleDateFormat("MMM dd,yyyy HH:mm").format(date));

        return reportMap;
    }


}
