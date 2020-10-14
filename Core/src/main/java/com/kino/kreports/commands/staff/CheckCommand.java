package com.kino.kreports.commands.staff;

import com.kino.kore.utils.messages.MessageUtils;
import com.kino.kreports.utils.ReportUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import me.fixeddev.ebcm.parametric.CommandClass;
import me.fixeddev.ebcm.parametric.annotation.ACommand;
import me.fixeddev.ebcm.parametric.annotation.Injected;
import me.fixeddev.ebcm.parametric.annotation.Optional;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;

import java.util.UUID;

@InjectAll
@ACommand(names = "check", desc = "All the functions to check bans, warns, reports, etc", permission = "kreports.commands.staff.check")
public class CheckCommand implements CommandClass {

    private ReportUtils reportUtils;

    @ACommand(names = {"reports", "report"}, desc = "Check player's reports", permission = "kreports.commands.staff.check.reports")
    public boolean executeCheckReports (@Injected(true) CommandSender sender, @Optional OfflinePlayer target, @Optional Boolean comments) {

        if (comments == null) {
            comments = false;
        }
        if (target == null) {
            if (sender instanceof Player) {

                Player p = (Player) sender;

                reportUtils.sendReportsOfPlayer(p, p, comments);

            } else {
                MessageUtils.sendMessage(sender, "&cIf you use the console, add a name argument to the command");
            }

        } else {

            reportUtils.sendReportsOfPlayer(target, sender, comments);
        }
        return true;

    }

    @ACommand(names = {"comments", "comment"}, desc = "Check the report's comment", permission = "kreports.commands.staff.check.comments")
    public boolean executeCheckComments (@Injected(true) CommandSender sender, UUID target) {

        reportUtils.sendCommentsOfReport(target, sender);

        return true;
    }
}
