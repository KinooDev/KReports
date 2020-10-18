package com.kino.kreports.models.user;

import com.eatthepath.uuid.FastUUID;
import com.kino.kreports.stats.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
public class SimpleUser implements User {

    protected int id;
    protected final UUID uuid;

    protected IntegerStat kicks;
    protected IntegerStat mutes;
    protected IntegerStat warns;
    protected IntegerStat bans;
    protected IntegerStat reports;


    public SimpleUser() {
        id = -1;
        uuid = UUID.randomUUID();

        kicks = new IntegerStat();
        mutes = new IntegerStat();
        warns = new IntegerStat();
        bans = new IntegerStat();
        reports = new IntegerStat();
    }

    public SimpleUser(User user) {
        id = user.getID();
        uuid = user.getUUID();

        kicks = user.getKicks();
        mutes = user.getMutes();
        warns = user.getWarns();
        bans = user.getBans();
        reports = user.getReports();
    }

    public SimpleUser(Map<String, Object> userMap) {
        //id = (int) userMap.get("id");
        uuid = FastUUID.parseUUID((String) userMap.get("uuid"));
        kicks = new IntegerStat((Integer) userMap.get("kicks"));
        mutes = new IntegerStat((Integer) userMap.get("mutes"));
        warns = new IntegerStat((Integer) userMap.get("warns"));
        bans = new IntegerStat((Integer) userMap.get("bans"));
        reports = new IntegerStat((Integer) userMap.get("reports"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleUser that = (SimpleUser) o;
        return Objects.equals(kicks, that.kicks) &&
                Objects.equals(mutes, that.mutes) &&
                Objects.equals(warns, that.warns) &&
                Objects.equals(bans, that.bans) &&
                Objects.equals(reports, that.reports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kicks, mutes, warns, bans, reports);
    }


    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }
}
