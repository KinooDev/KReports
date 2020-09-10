package com.kino.kreports.storage.user;

import com.kino.kreports.stats.*;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
public class SimpleUser implements User{


    protected Kicks kicks;
    protected Mutes mutes;
    protected Warns warns;
    protected Bans bans;
    protected Reports reports;


    public SimpleUser() {
        kicks = new Kicks();
        mutes = new Mutes();
        warns = new Warns();
        bans = new Bans();
        reports = new Reports();
    }

    public SimpleUser(Map<String, Object> userMap) {
        kicks = new Kicks((Integer) userMap.get("kicks"));
        mutes = new Mutes((Integer) userMap.get("mutes"));
        warns = new Warns((Integer) userMap.get("warns"));
        bans = new Bans((Integer) userMap.get("bans"));
        reports = new Reports((Integer) userMap.get("reports"));
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
}
