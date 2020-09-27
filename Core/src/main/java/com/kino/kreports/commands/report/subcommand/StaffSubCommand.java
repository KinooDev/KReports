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
@ACommand(names = "staff", desc = "All the functions to check reports & staff stuff", permission = "kreports.commands.report.staff")
public class StaffSubCommand implements CommandClass {

    private ReportUtils reportUtils;

    @ACommand(names = "check", desc = "Check player's reports", permission = "kreports.commands.report.staff.check")
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
