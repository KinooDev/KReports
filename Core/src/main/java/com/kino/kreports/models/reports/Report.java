package com.kino.kreports.models.reports;

import com.kino.kreports.utils.report.ReportPriority;
import com.kino.kreports.utils.report.ReportState;
import com.eatthepath.uuid.FastUUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
public class Report implements ConfigurationSerializable {

    @Getter @Setter private int id;
    @Getter private final UUID uuid;

    @Getter @Setter private ReportPriority priority;
    @Getter @Setter private ReportState state;

    @Getter private final UUID reported;
    @Getter private final UUID reporter;

    @Getter private final String reason;

    @Getter @Setter private boolean accepted;
    @Getter @Setter private UUID accepter;

    @Getter @Setter private List<String> comments;
    @Getter @Setter private List<UUID> staffInspection;

    @Getter private final Date date;

    public Report (UUID reported, UUID reporter, String reason) {
        this(reported, reporter, reason, false, null);
    }

    public Report (UUID reported, UUID reporter, String reason, boolean accepted, UUID accepter) {
        this.id = -1;
        this.uuid = UUID.randomUUID();

        this.priority = ReportPriority.MEDIUM;
        this.state = ReportState.PAUSED;

        this.reported = reported;
        this.reporter = reporter;

        this.reason = reason;

        this.accepted = accepted;
        this.accepter = accepter;

        this.comments = new ArrayList<>();
        this.staffInspection = new ArrayList<>();

        this.date = new Date(System.currentTimeMillis());
    }


    public Report (Map<String, Object> map) {
        //this.id = (int) map.get("id");
        this.uuid = FastUUID.parseUUID((String) map.get("uuid"));

        this.priority = ReportPriority.valueOf((String) map.get("priority"));
        this.state = ReportState.valueOf((String) map.get("state"));

        this.reported = FastUUID.parseUUID((String) map.get("reported"));
        this.reporter = FastUUID.parseUUID((String) map.get("reporter"));

        this.reason = (String) map.get("reason");

        this.accepted = (Boolean) map.get("accepted");
        this.accepter = map.get("accepter") == null ? null : UUID.fromString((String) map.get("accepter"));

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
        setState(ReportState.INSPECTION);
    }

    public void removeInspector (UUID uuid) {
        staffInspection.remove(uuid);
        if (staffInspection.isEmpty()) {
            setState(ReportState.PAUSED);
        }
    }

    public void accept () {
        for (UUID uuid : staffInspection) {
            removeInspector(uuid);
        }
        this.accepted = true;
        this.state = ReportState.ARCHIVED;
    }

    public void unaccept () {
        this.accepted = false;
        this.state = ReportState.PAUSED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> reportMap = new LinkedHashMap<>();

        //reportMap.put("id", id);
        reportMap.put("uuid", FastUUID.toString(uuid));
        reportMap.put("priority", priority.name());
        reportMap.put("state", state.name());
        reportMap.put("reported", FastUUID.toString(reported));
        reportMap.put("reporter", FastUUID.toString(reporter));
        reportMap.put("reason", reason);
        reportMap.put("accepted", accepted);
        reportMap.put("accepter", accepter == null ? null : FastUUID.toString(accepter));
        reportMap.put("comments", comments);
        reportMap.put("staffInspection", staffInspection);
        reportMap.put("date", new SimpleDateFormat("MMM dd,yyyy HH:mm").format(date));

        return reportMap;
    }

    @Override
    public String toString() {
        return "Report{" +
                "priority=" + priority +
                ", state=" + state +
                ", reported=" + reported +
                ", reporter=" + reporter +
                ", reason='" + reason + '\'' +
                ", accepted=" + accepted +
                ", comments=" + comments +
                ", staffInspection=" + staffInspection +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return accepted == report.accepted &&
                priority == report.priority &&
                state == report.state &&
                Objects.equals(reported, report.reported) &&
                Objects.equals(reporter, report.reporter) &&
                Objects.equals(reason, report.reason) &&
                Objects.equals(comments, report.comments) &&
                Objects.equals(staffInspection, report.staffInspection) &&
                Objects.equals(date, report.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority, state, reported, reporter, reason, accepted, comments, staffInspection, date);
    }
}
