package com.kino.kreports.loader;

import com.kino.kore.utils.loaders.Loader;
import com.kino.kreports.KReports;
import com.kino.kreports.commands.main.KReportsCommand;
import com.kino.kreports.listener.JoinListener;
import com.kino.kreports.listener.QuitListener;
import me.fixeddev.ebcm.bukkit.BukkitCommandManager;
import me.fixeddev.ebcm.parametric.CommandClass;
import me.fixeddev.ebcm.parametric.ParametricCommandBuilder;
import me.fixeddev.ebcm.parametric.ReflectionParametricCommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.InjectIgnore;

@InjectAll
public class CommandsLoader implements Loader {

    private KReportsCommand kReportsCommand;
    private KReports plugin;

    @InjectIgnore
    private final ParametricCommandBuilder builder = new ReflectionParametricCommandBuilder();

    @InjectIgnore
    private final BukkitCommandManager commandManager = new BukkitCommandManager("KReports");

    private void registerCommands(CommandClass... commandClasses) {
        for (CommandClass commandClass : commandClasses) {
            commandManager.registerCommands(builder.fromClass(commandClass));
        }
    }

    @Override
    public void load() {
        registerCommands(kReportsCommand);
    }
}
