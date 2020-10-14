package com.kino.kreports.commands.staff.manage.subcommands;

import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.messages.MessageUtils;
import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.models.reports.Report;
import com.kino.kreports.models.user.Staff;
import com.kino.kreports.utils.report.ReportPriority;
import com.kino.kreports.utils.report.ReportState;
import com.kino.kreports.utils.report.ReportUtils;
import com.kino.kreports.utils.user.UserUtils;
import me.fixeddev.ebcm.parametric.CommandClass;
import me.fixeddev.ebcm.parametric.annotation.ACommand;
import me.fixeddev.ebcm.parametric.annotation.ConsumedArgs;
import me.fixeddev.ebcm.parametric.annotation.Injected;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.name.Named;

import java.util.UUID;

@InjectAll
@ACommand(names = {"reports", "report"}, desc = "Manage reports command", permission = "kreports.commands.staff.manage.reports")
public class ManageReportSubCommand implements CommandClass {

    private ReportUtils reportUtils;
    private UserUtils userUtils;

    private Storage<UUID, Report> reportStorage;

    @Named("messages")
    private YMLFile messages;

    @ACommand(names = {"addcomment"}, desc = "Add a comment to a report", permission = "kreports.commands.staff.manage.reports.addcomment")
    public boolean executeAddComment (@Injected(true) CommandSender sender, UUID uuid, @ConsumedArgs(-1) String comment) {

        if (reportStorage.findFromData(uuid).isPresent()) {
            Report report = reportStorage.findFromData(uuid).get();
            if (!report.isAccepted()) {
                reportUtils.addComment(sender, comment, report);
            } else {
                MessageUtils.sendMessage(sender, messages.getString("report.cantEditWhenAccepted"));
            }

        } else {
            MessageUtils.sendMessage(sender, messages.getString("invalidUUID"));

        }

        return true;

    }

    @ACommand(names = {"setpriority", "changepriority"}, desc = "Change the priority of a report", permission = "kreports.commands.staff.manage.reports.set.priority")
    public boolean executeChangeReportPriority (@Injected(true) CommandSender sender, UUID uuid, ReportPriority priority) {

        if (reportStorage.findFromData(uuid).isPresent()) {
            Report report = reportStorage.findFromData(uuid).get();
            if (!report.isAccepted()) {
                reportUtils.changePriority(sender, report, priority, uuid);
            } else {
                MessageUtils.sendMessage(sender, messages.getString("report.cantEditWhenAccepted"));
            }

        } else {
            MessageUtils.sendMessage(sender, messages.getString("invalidPriority"));

        }

        return true;

    }

    @ACommand(names = {"setstate", "changestate"}, desc = "Change the state of a report", permission = "kreports.commands.staff.manage.reports.set.state")
    public boolean executeChangeReportState (@Injected(true) CommandSender sender, UUID uuid, ReportState state) {

        if (reportStorage.findFromData(uuid).isPresent()) {
            Report report = reportStorage.findFromData(uuid).get();
            if (!report.isAccepted()) {
                reportUtils.changeState(sender, report, state, uuid);
            } else {
                MessageUtils.sendMessage(sender, messages.getString("report.cantEditWhenAccepted"));
            }
        } else {
            MessageUtils.sendMessage(sender, messages.getString("invalidState"));

        }

        return true;

    }

    @ACommand(names = {"accept"}, desc = "Accept a report", permission = "kreports.commands.staff.manage.reports.accept")
    public boolean executeAcceptReport (@Injected(true) CommandSender sender, UUID uuid) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (reportStorage.findFromData(uuid).isPresent()) {
                Report report = reportStorage.findFromData(uuid).get();
                if (!report.isAccepted()) {
                    reportUtils.accept(p, report, uuid);
                    if (userUtils.isStaff(userUtils.fromUUID(p.getUniqueId()))) {
                        Staff staff = (Staff) userUtils.fromUUID(p.getUniqueId());
                        staff.getReportsStaff().add(1);
                    }
                } else {
                    MessageUtils.sendMessage(sender, messages.getString("report.cantEditWhenAccepted"));
                }
            } else {
                MessageUtils.sendMessage(sender, messages.getString("invalidState"));
            }
        } else {
            MessageUtils.sendMessage(sender, "&cThis command is only for players!");
        }

        return true;

    }


}
