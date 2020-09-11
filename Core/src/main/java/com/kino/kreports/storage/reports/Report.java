package com.kino.kreports.storage.reports;

import com.kino.kreports.utils.ReportPriority;
import com.kino.kreports.utils.ReportState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
public class Report implements ConfigurationSerializable {


    @Getter @Setter private ReportPriority priority;
    @Getter @Setter private ReportState state;

    @Getter private final UUID reported;
    @Getter private final UUID reporter;

    @Getter @Setter private String reason;

    @Getter @Setter private boolean accepted = false;

    @Getter @Setter private List<String> comments;
    @Getter @Setter private List<UUID> staffInspection;

    @Getter private final Date date;

    Report (UUID reported, UUID reporter, String reason) {
        this.priority = ReportPriority.MEDIUM;
        this.state = ReportState.PAUSED;

        this.reported = reported;
        this.reporter = reporter;

        this.reason = reason;

        this.comments = new ArrayList<>();
        this.staffInspection = new ArrayList<>();

        this.date = new Date(System.currentTimeMillis());
    }


    Report (Map<String, Object> map) {
        this.priority = (ReportPriority) map.get("priority");
        this.state = (ReportState) map.get("state");

        this.reported = (UUID) map.get("reported");
        this.reporter = (UUID) map.get("reporter");

        this.reason = (String) map.get("reason");

        this.comments = (List<String>) map.get("comments");

        this.staffInspection = (List<UUID>) map.get("staffInspection");

        Date date1;

        try {
            date1 = new SimpleDateFormat("MMM dd,yyyy HH:mm").parse((String) map.get("date"));
        } catch (ParseException e) {
            e.printStackTrace();
            date1 = new Date(System.currentTimeMillis());
        }
        this.date = date1;
    }

    public void addInspector (UUID uuid) {
        staffInspection.add(uuid);
    }

    public void accept () {
        this.accepted = true;
        this.state = ReportState.ARCHIVED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> reportMap = new LinkedHashMap<>();

        reportMap.put("priority", priority);
        reportMap.put("state", state);
        reportMap.put("reported", reported);
        reportMap.put("reporter", reporter);
        reportMap.put("reason", reason);
        reportMap.put("comments", comments);
        reportMap.put("staffInspection", staffInspection);
        reportMap.put("date", new SimpleDateFormat("MMM dd,yyyy HH:mm").format(date));

        return reportMap;
    }


}
