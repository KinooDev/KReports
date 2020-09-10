package com.kino.kreports.modules;

import com.kino.kore.utils.files.YMLFile;
import com.kino.kreports.KReports;
import com.kino.kreports.files.FileBinder;
import lombok.AllArgsConstructor;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.bind.AbstractModule;

@AllArgsConstructor
public class MainModule extends AbstractModule {

    private final KReports kReports;

    @Override
    protected void configure() {
        FileBinder fileBinder = new FileBinder()
                .bind("config", new YMLFile(kReports, "config"))
                .bind("messages", new YMLFile(kReports, "messages"))
                .bind("user_data", new YMLFile(kReports, "user_data"))
                .bind("reports_data", new YMLFile(kReports, "reports_data"));

        install(fileBinder.build());
        install(new ServicesModule());


        bind(KReports.class).toInstance(kReports);
        bind(Plugin.class).to(KReports.class);
    }
}
