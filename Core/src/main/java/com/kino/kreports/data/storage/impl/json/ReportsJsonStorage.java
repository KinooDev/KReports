package com.kino.kreports.data.storage.impl.json;

import com.eatthepath.uuid.FastUUID;
import com.google.gson.reflect.TypeToken;
import com.kino.kreports.KReports;
import com.kino.kreports.data.KReportStorage;
import com.kino.kreports.models.reports.Report;
import com.kino.kreports.models.user.Staff;
import com.kino.kreports.models.user.User;
import com.kino.kreports.utils.report.ReportPriority;
import com.kino.kreports.utils.report.ReportState;
import com.kino.kreports.utils.storage.Document;
import io.netty.util.internal.ConcurrentSet;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ReportsJsonStorage implements KReportStorage {

    private AtomicInteger nextReportID;
    private AtomicInteger nextUserID;

    private File usersFile;
    private File reportsFile;

    private Document data;

    private ConcurrentSet<User> users;
    private ConcurrentSet<Report> reports;

    public ReportsJsonStorage(KReports kReports) {
        this.usersFile = new File(kReports.getDataFolder(), "users.json");
        this.reportsFile = new File(kReports.getDataFolder(), "reports.json");

        if(reportsFile.exists() && reportsFile.isFile()) this.data = Document.loadData(reportsFile);
        else if(usersFile.exists() && usersFile.isFile()) this.data = Document.loadData(usersFile);
        else this.data = new Document();

        this.data.appendDefault("users", new ConcurrentSet<>());
        this.data.appendDefault("reports", new ConcurrentSet<>());

        this.users = this.data.getObject("users", new TypeToken<ConcurrentSet<User>>(){}.getType());
        this.reports = this.data.getObject("reports", new TypeToken<ConcurrentSet<Report>>(){}.getType());

        nextReportID = new AtomicInteger(this.reports.size() + 1);
        nextUserID = new AtomicInteger(this.users.size() + 1);
    }

    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public Report getReport(UUID id) {
        for (Report report : reports) {
            if (report.getUuid().equals(id)) {
                return report;
            }
        }

        return null;
    }

    @Override
    public Report getReport(int id) {
        for (Report report : reports) {
            if (report.getId() == id) {
                return report;
            }
        }

        return null;
    }

    @Override
    public Report getReport(String id) {
        for (Report report : reports) {
            if (report.getUuid().equals(FastUUID.parseUUID(id))) {
                return report;
            }
        }

        return null;
    }

    @Override
    public List<Report> getReports() {
        return new LinkedList<>(this.reports);
    }

    @Override
    public Report createReport(Report report) {
        this.reports.add(report);
        report.setId(nextReportID.getAndIncrement());
        save();
        return report;
    }

    public void saveReport(Report report) {
        for (Report r : reports) {
            if (r.getUuid().equals(report.getUuid())) {
                reports.remove(r);
                reports.add(report);
                save();
                return;
            }
        }
    }

    @Override
    public void updateState(UUID uuid, ReportState state) {
        Report report = getReport(uuid);
        report.setState(state);
        saveReport(getReport(uuid));
    }

    @Override
    public void updatePriority(UUID uuid, ReportPriority priority) {
        Report report = getReport(uuid);
        report.setPriority(priority);
        saveReport(getReport(uuid));
    }

    @Override
    public void updateComments(UUID uuid, String comment) {
        Report report = getReport(uuid);
        report.getComments().add(comment);
        saveReport(getReport(uuid));
    }

    @Override
    public void removeComment(UUID uuid, int id) {
        Report report = getReport(uuid);
        report.getComments().remove(id - 1);
        saveReport(getReport(uuid));
    }

    @Override
    public void updateStaffInspect(UUID uuid, UUID staff) {
        Report report = getReport(uuid);
        report.getStaffInspection().add(staff);
        updateState(uuid, ReportState.INSPECTION);
        saveReport(getReport(uuid));
    }

    @Override
    public void removeStaffInspect(UUID uuid, UUID staff) {
        Report report = getReport(uuid);
        report.getStaffInspection().remove(staff);
        if (report.getStaffInspection().isEmpty()) {
            updateState(uuid, ReportState.PAUSED);
        }
        saveReport(getReport(uuid));
    }

    @Override
    public void updateAccept(UUID uuid, boolean accept, Player p) {
        Report report = getReport(uuid);
        report.setAccepted(accept);
        if (accept) {
            updateAccepter(uuid, p.getUniqueId());
        } else {
            updateAccepter(uuid, null);
        }
        saveReport(getReport(uuid));
    }

    @Override
    public void updateAccepter(UUID uuid, UUID accepter) {
        Report report = getReport(uuid);
        report.setAccepter(accepter);
        saveReport(getReport(uuid));
    }

    @Override
    public User getUser(UUID id) {
        for (User user : users) {
            if (user.getUUID().equals(id)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public User getUser(int id) {
        for (User user : users) {
            if (user.getID() == id) {
                return user;
            }
        }

        return null;
    }

    @Override
    public User getUser(String id) {
        for (User user : users) {
            if (user.getUUID().equals(FastUUID.parseUUID(id))) {
                return user;
            }
        }

        return null;
    }

    @Override
    public List<User> getUsers() {
        return new LinkedList<>(this.users);
    }

    @Override
    public User createUser(User user) {
        this.users.add(user);
        user.setID(nextUserID.getAndIncrement());
        save();
        return user;
    }

    public void saveUser(User user) {
        for (User u : users) {
            if (u.getUUID().equals(user.getUUID())) {
                users.remove(u);
                users.add(user);
                save();
                return;
            }
        }
    }

    @Override
    public void updateBans(UUID uuid, int amount) {
        User user = getUser(uuid);
        if (isPositive(amount)) {
            user.getBans().add(amount);
        } else {
            user.getBans().remove(Math.abs(amount));
        }
        saveUser(user);
    }

    @Override
    public void updateKicks(UUID uuid, int amount) {
        User user = getUser(uuid);
        if (isPositive(amount)) {
            user.getKicks().add(amount);
        } else {
            user.getKicks().remove(Math.abs(amount));
        }
        saveUser(user);
    }

    @Override
    public void updateMutes(UUID uuid, int amount) {
        User user = getUser(uuid);
        if (isPositive(amount)) {
            user.getMutes().add(amount);
        } else {
            user.getMutes().remove(Math.abs(amount));
        }
        saveUser(user);
    }

    @Override
    public void updateReports(UUID uuid, int amount) {
        User user = getUser(uuid);
        if (isPositive(amount)) {
            user.getReports().add(amount);
        } else {
            user.getReports().remove(Math.abs(amount));
        }
        saveUser(user);
    }

    @Override
    public void updateWarns(UUID uuid, int amount) {
        User user = getUser(uuid);
        if (isPositive(amount)) {
            user.getWarns().add(amount);
        } else {
            user.getWarns().remove(Math.abs(amount));
        }
        saveUser(user);
    }

    @Override
    public void updateBansStaff(UUID uuid, int amount) {
        User user = getUser(uuid);
        if (user instanceof Staff) {
            Staff s = (Staff) user;
            if (isPositive(amount)) {
                s.getBansStaff().add(amount);
            } else {
                s.getBansStaff().remove(Math.abs(amount));
            }
            saveUser(user);
        }
    }

    @Override
    public void updateKicksStaff(UUID uuid, int amount) {
        User user = getUser(uuid);
        if (user instanceof Staff) {
            Staff s = (Staff) user;
            if (isPositive(amount)) {
                s.getKicksStaff().add(amount);
            } else {
                s.getKicksStaff().remove(Math.abs(amount));
            }
            saveUser(user);
        }
    }

    @Override
    public void updateMutesStaff(UUID uuid, int amount) {
        User user = getUser(uuid);
        if (user instanceof Staff) {
            Staff s = (Staff) user;
            if (isPositive(amount)) {
                s.getMutesStaff().add(amount);
            } else {
                s.getMutesStaff().remove(Math.abs(amount));
            }
            saveUser(user);
        }
    }

    @Override
    public void updateReportsStaff(UUID uuid, int amount) {
        User user = getUser(uuid);
        if (user instanceof Staff) {
            Staff s = (Staff) user;
            if (isPositive(amount)) {
                s.getReportsStaff().add(amount);
            } else {
                s.getReportsStaff().remove(Math.abs(amount));
            }
            saveUser(user);
        }
    }

    @Override
    public void updateWarnsStaff(UUID uuid, int amount) {
        User user = getUser(uuid);
        if (user instanceof Staff) {
            Staff s = (Staff) user;
            if (isPositive(amount)) {
                s.getWarnsStaff().add(amount);
            } else {
                s.getWarnsStaff().remove(Math.abs(amount));
            }
            saveUser(user);
        }
    }

    private boolean isPositive (int i) {
        return i >= 0;
    }

    public void save(){
        this.data.append("reports", this.reports);
        this.data.saveData(this.reportsFile);

        this.data.append("users", this.users);
        this.data.saveData(this.usersFile);
    }
}
