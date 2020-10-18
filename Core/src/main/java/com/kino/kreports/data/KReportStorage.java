package com.kino.kreports.data;

import com.kino.kreports.models.reports.Report;
import com.kino.kreports.models.user.User;
import com.kino.kreports.stats.IntegerStat;
import com.kino.kreports.utils.report.ReportPriority;
import com.kino.kreports.utils.report.ReportState;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface KReportStorage {

    boolean connect();

    void disconnect();

    boolean isConnected();

    Report getReport(UUID id) throws Exception;

    Report getReport(int id) throws Exception;

    Report getReport(String id) throws Exception;

    List<Report> getReports();

    Report createReport(Report report);

    void updateState(UUID uuid, ReportState state);

    void updatePriority(UUID uuid, ReportPriority priority);

    void updateComments (UUID uuid, String comment);

    void removeComment (UUID uuid, int id);

    void updateStaffInspect (UUID uuid, UUID staff);

    void removeStaffInspect (UUID uuid, UUID staff);

    void updateAccept (UUID uuid, boolean accept, Player p);

    void updateAccepter (UUID uuid, UUID accepter);

    User getUser(UUID id) throws Exception;

    User getUser(int id) throws Exception;

    User getUser(String id) throws Exception;

    List<User> getUsers();

    User createUser(User user);

    void updateBans(UUID uuid, int amount);

    void updateMutes(UUID uuid, int amount);

    void updateKicks(UUID uuid, int amount);

    void updateWarns(UUID uuid, int amount);

    void updateReports(UUID uuid, int amount);

    void updateBansStaff(UUID uuid, int amount);

    void updateReportsStaff(UUID uuid, int amount);

    void updateMutesStaff(UUID uuid, int amount);

    void updateWarnsStaff(UUID uuid, int amount);

    void updateKicksStaff(UUID uuid, int amount);
}
