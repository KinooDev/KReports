package com.kino.kreports.commands.report;

import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.messages.MessageUtils;
import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.models.reports.Report;
import com.kino.kreports.models.user.User;
import com.kino.kreports.utils.report.ReportUtils;
import me.fixeddev.ebcm.parametric.CommandClass;
import me.fixeddev.ebcm.parametric.annotation.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.name.Named;

import java.util.UUID;

@InjectAll
@ACommand(names = {"report"}, desc = "Report and utilities command", permission = "kreports.commands.report")
public class ReportCommand implements CommandClass {

    private ReportUtils reportUtils;

    private Storage<UUID, User> playerStorage;

    private Storage<UUID, Report> reportStorage;

    @Named("messages")
    private YMLFile messages;

    @Named("config")
    private YMLFile config;

    @ACommand(names = "")
    public boolean executeReportCommand (@Injected(true) CommandSender sender, Player p, @ConsumedArgs(-1) String reason) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (p != null && p.isOnline()) {

                if (!p.getName().equals(sender.getName())) {

                    Report report;
                    if (StringUtils.isBlank(reason)) {
                        report = new Report(p.getUniqueId(), player.getUniqueId(), config.getString("reports.defaultReason"));
                    } else {
                        report = new Report(p.getUniqueId(), player.getUniqueId(), reason);
                    }
                    if (playerStorage.find(p.getUniqueId()).isPresent()) {
                        playerStorage.find(p.getUniqueId()).get().getReports().add(1);
                    }

                    UUID uuid = UUID.randomUUID();

                    reportStorage.add(uuid, report);
                    reportStorage.save(uuid);
                    reportUtils.broadcast(report);
                    MessageUtils.sendMessage(player, messages.getString("report.succesfullyReported").replace(
                            "<reason>", reason).replace(
                            "<reported>", p.getName()));
                } else {
                    MessageUtils.sendMessage(player, messages.getString("report.yourself"));
                }

            } else {
                MessageUtils.sendMessage(player, messages.getString("playerNotOnline"));
            }
        } else {
            MessageUtils.sendMessage(sender, "&cThis command is only for players!");
        }
        return true;

    }


}
