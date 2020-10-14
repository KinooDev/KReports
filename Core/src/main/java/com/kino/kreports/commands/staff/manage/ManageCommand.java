package com.kino.kreports.commands.staff.manage;

import com.kino.kreports.commands.staff.manage.subcommands.ManageReportSubCommand;
import com.kino.kreports.utils.report.ReportUtils;
import me.fixeddev.ebcm.parametric.CommandClass;
import me.fixeddev.ebcm.parametric.annotation.ACommand;
import me.fixeddev.ebcm.parametric.annotation.SubCommandClasses;
import team.unnamed.inject.InjectAll;

@InjectAll
@ACommand(names = "manage", desc = "All the functions to manage bans, warns, reports, etc (add comments, set priority, etc)", permission = "kreports.commands.staff.manage")
@SubCommandClasses(ManageReportSubCommand.class)
public class ManageCommand implements CommandClass {

    private ReportUtils reportUtils;

}
