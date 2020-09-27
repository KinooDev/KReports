package com.kino.kreports.commands.report.subcommand;

import com.kino.kore.utils.messages.MessageUtils;
import com.kino.kreports.utils.ReportUtils;
import me.fixeddev.ebcm.parametric.CommandClass;
import me.fixeddev.ebcm.parametric.annotation.ACommand;
import me.fixeddev.ebcm.parametric.annotation.Injected;
import me.fixeddev.ebcm.parametric.annotation.Optional;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;

@InjectAll
@ACommand(names = "check", desc = "All the functions to check bans, warns, reports, etc", permission = "kreports.commands.staff.check")
public class CheckCommand implements CommandClass {

    private ReportUtils reportUtils;

    @ACommand(names = {"reports", "report"}, desc = "Check player's reports", permission = "kreports.commands.staff.check.reports")
    public boolean executeCheck (@Injected(true) CommandSender sender, @Optional OfflinePlayer target) {



        if (target == null) {
            if (sender instanceof Player) {

                Player p = (Player) sender;

                reportUtils.sendReportsOfPlayer(p, p);

            } else {
                MessageUtils.sendMessage(sender, "&cIf you use the console, add a name argument to the command");
            }

        } else {

            reportUtils.sendReportsOfPlayer(target, sender);
        }
        return true;

    }
}
