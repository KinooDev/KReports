package com.kino.kreports.storage.user;


import com.kino.kreports.stats.*;
import com.kino.kreports.stats.staff.*;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@SuppressWarnings("unchecked")
public class Staff extends SimpleUser {

    private KicksStaff kicksStaff;
    private MutesStaff mutesStaff;
    private WarnsStaff warnsStaff;
    private BansStaff bansStaff;
    private ReportsStaff reportsStaff;

    public Staff() {
        kicks = new Kicks();
        mutes = new Mutes();
        warns = new Warns();
        bans = new Bans();
        reports = new Reports();

        kicksStaff = new KicksStaff();
        mutesStaff = new MutesStaff();
        warnsStaff = new WarnsStaff();
        bansStaff = new BansStaff();
        reportsStaff = new ReportsStaff();
    }

    public Staff(Map<String, Object> userMap) {
        kicks = new Kicks((Integer) userMap.get("kicks"));
        mutes = new Mutes((Integer) userMap.get("mutes"));
        warns = new Warns((Integer) userMap.get("warns"));
        bans = new Bans((Integer) userMap.get("bans"));
        reports = new Reports((Integer) userMap.get("reports"));

        Map<String, Object> staffMap = (Map<String, Object>) userMap.get("staff");
        kicksStaff = new KicksStaff((Integer) staffMap.get("kicks"));
        mutesStaff = new MutesStaff((Integer) staffMap.get("mutes"));
        warnsStaff = new WarnsStaff((Integer) staffMap.get("warns"));
        bansStaff = new BansStaff((Integer) staffMap.get("bans"));
        reportsStaff = new ReportsStaff((Integer) staffMap.get("reports"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> playerMap = new LinkedHashMap<>();

        playerMap.put("reports", getReports().get());
        playerMap.put("bans", getBans().get());
        playerMap.put("kicks", getKicks().get());
        playerMap.put("warns", getWarns().get());
        playerMap.put("mutes", getMutes().get());

        Map<String, Object> staffMap = new LinkedHashMap<>();
        staffMap.put("kicks", getKicksStaff().get());
        staffMap.put("bans", getBansStaff().get());
        staffMap.put("reports", getReportsStaff().get());
        staffMap.put("mutes", getMutesStaff().get());
        staffMap.put("warns", getWarnsStaff().get());

        playerMap.put("staff", staffMap);

        return playerMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Staff staff = (Staff) o;
        return Objects.equals(kicksStaff, staff.kicksStaff) &&
                Objects.equals(mutesStaff, staff.mutesStaff) &&
                Objects.equals(warnsStaff, staff.warnsStaff) &&
                Objects.equals(bansStaff, staff.bansStaff) &&
                Objects.equals(reportsStaff, staff.reportsStaff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), kicksStaff, mutesStaff, warnsStaff, bansStaff, reportsStaff);
    }
}
