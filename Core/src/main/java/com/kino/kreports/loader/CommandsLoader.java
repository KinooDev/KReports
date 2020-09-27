package com.kino.kreports.loader;

import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.loaders.Loader;
import com.kino.kreports.KReports;
import com.kino.kreports.commands.main.KReportsCommand;
import com.kino.kreports.commands.report.ReportCommand;
import com.kino.kreports.commands.staff.CheckCommand;
import com.kino.kreports.commands.staff.manage.ManageCommand;
import com.kino.kreports.ebcm.builder.InjectionParametricCommandBuilder;
import com.kino.kreports.ebcm.i18n.CustomI18n;
import com.kino.kreports.ebcm.module.KReportsModule;
import me.fixeddev.ebcm.bukkit.BukkitCommandManager;
import me.fixeddev.ebcm.parametric.CommandClass;
import me.fixeddev.ebcm.parametric.ParametricCommandBuilder;
import me.fixeddev.ebcm.parametric.ReflectionParametricCommandBuilder;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.InjectIgnore;
import team.unnamed.inject.name.Named;

@InjectAll
public class CommandsLoader implements Loader {

    private KReportsCommand kReportsCommand;
    private ReportCommand reportCommand;
    private CheckCommand checkCommand;
    private ManageCommand manageCommand;
    private KReports plugin;

    @Named("messages")
    private YMLFile messages;

    private InjectionParametricCommandBuilder builder;

    @InjectIgnore
    private final BukkitCommandManager commandManager = new BukkitCommandManager("KReports");

    private void registerCommands(CommandClass... commandClasses) {
        for (CommandClass commandClass : commandClasses) {
            commandManager.registerCommands(builder.fromClass(commandClass));
        }
    }

    @Override
    public void load() {
        commandManager.setI18n(new CustomI18n(messages));
        commandManager.getProviderRegistry().installModule(new KReportsModule());
        registerCommands(kReportsCommand, reportCommand, checkCommand, manageCommand);
    }
}
