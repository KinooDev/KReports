package com.kino.kreports.models.reports;

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

    public Report (UUID reported, UUID reporter, String reason) {
        this.priority = ReportPriority.MEDIUM;
        this.state = ReportState.PAUSED;

        this.reported = reported;
        this.reporter = reporter;

        this.reason = reason;

        this.comments = new ArrayList<>();
        this.staffInspection = new ArrayList<>();

        this.date = new Date(System.currentTimeMillis());
    }


    public Report (Map<String, Object> map) {
        this.priority = ReportPriority.valueOf((String) map.get("priority"));
        this.state = ReportState.valueOf((String) map.get("state"));

        this.reported = UUID.fromString((String) map.get("reported"));
        this.reporter = UUID.fromString((String) map.get("reporter"));

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

        reportMap.put("priority", priority.name());
        reportMap.put("state", state.name());
        reportMap.put("reported", reported.toString());
        reportMap.put("reporter", reporter.toString());
        reportMap.put("reason", reason);
        reportMap.put("comments", comments);
        reportMap.put("staffInspection", staffInspection);
        reportMap.put("date", new SimpleDateFormat("MMM dd,yyyy HH:mm").format(date));

        return reportMap;
    }


}
