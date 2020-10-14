package com.kino.kreports.models.user;


import com.kino.kreports.stats.*;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@SuppressWarnings("unchecked")
public class Staff extends SimpleUser {

    private IntegerStat kicksStaff;
    private IntegerStat mutesStaff;
    private IntegerStat warnsStaff;
    private IntegerStat bansStaff;
    private IntegerStat reportsStaff;

    public Staff() {
        kicks = new IntegerStat();
        mutes = new IntegerStat();
        warns = new IntegerStat();
        bans = new IntegerStat();
        reports = new IntegerStat();

        kicksStaff = new IntegerStat();
        mutesStaff = new IntegerStat();
        warnsStaff = new IntegerStat();
        bansStaff = new IntegerStat();
        reportsStaff = new IntegerStat();
    }

    public Staff (SimpleUser user) {
        kicks = user.getKicks();
        mutes = user.getMutes();
        warns = user.getWarns();
        bans = user.getBans();
        reports = user.getReports();

        kicksStaff = new IntegerStat();
        mutesStaff = new IntegerStat();
        warnsStaff = new IntegerStat();
        bansStaff = new IntegerStat();
        reportsStaff = new IntegerStat();
    }

    public Staff(Map<String, Object> userMap) {
        kicks = new IntegerStat((Integer) userMap.get("kicks"));
        mutes = new IntegerStat((Integer) userMap.get("mutes"));
        warns = new IntegerStat((Integer) userMap.get("warns"));
        bans = new IntegerStat((Integer) userMap.get("bans"));
        reports = new IntegerStat((Integer) userMap.get("reports"));

        Map<String, Object> staffMap = (Map<String, Object>) userMap.get("staff");
        kicksStaff = new IntegerStat((Integer) staffMap.get("kicks"));
        mutesStaff = new IntegerStat((Integer) staffMap.get("mutes"));
        warnsStaff = new IntegerStat((Integer) staffMap.get("warns"));
        bansStaff = new IntegerStat((Integer) staffMap.get("bans"));
        reportsStaff = new IntegerStat((Integer) staffMap.get("reports"));
    }

    public Staff(ConfigurationSection configurationSection) {
        kicks = new IntegerStat(configurationSection.getInt("kicks"));
        mutes = new IntegerStat(configurationSection.getInt("mutes"));
        warns = new IntegerStat(configurationSection.getInt("warns"));
        bans = new IntegerStat(configurationSection.getInt("bans"));
        reports = new IntegerStat(configurationSection.getInt("reports"));

        Map<String, Object> staffMap = configurationSection.getConfigurationSection("staff").getValues(false);
        kicksStaff = new IntegerStat((Integer) staffMap.get("kicks"));
        mutesStaff = new IntegerStat((Integer) staffMap.get("mutes"));
        warnsStaff = new IntegerStat((Integer) staffMap.get("warns"));
        bansStaff = new IntegerStat((Integer) staffMap.get("bans"));
        reportsStaff = new IntegerStat((Integer) staffMap.get("reports"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> playerMap = new LinkedHashMap<>();

        playerMap.put("reports", getReports().get());
        playerMap.put("bans", getBans().get());
        playerMap.put("kicks", getKicks().get());
        playerMap.put("warns", getWarns().get());
        playerMap.put("mutes", getMutes().get());

        Map<String, Object> staffMap = new HashMap<>();
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
