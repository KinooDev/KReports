package com.kino.kreports;

import com.kino.kore.utils.PluginUtils;
import com.kino.kore.utils.service.Service;
import com.kino.kreports.modules.MainModule;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Injector;
import team.unnamed.inject.InjectorFactory;
import team.unnamed.inject.name.Named;

public class KReports extends JavaPlugin {

    @Inject
    @Named("kreports-service")
    private Service kreportsService;


    @Override
    public void onEnable() {
        Injector injector = InjectorFactory.create(new MainModule(this));
        injector.injectMembers(this);

        kreportsService.start();

        getLogger().info("KReports " + PluginUtils.getVersion(this) + " enabled");
    }

    @Override
    public void onDisable() {
        kreportsService.stop();
    }


}
