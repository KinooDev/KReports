package com.kino.kreports.storage.reports;

import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.models.reports.Report;
import org.bukkit.configuration.ConfigurationSection;
import team.unnamed.inject.Inject;
import team.unnamed.inject.name.Named;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class ReportStorageManager implements Storage<UUID, Report> {

    @Inject
    @Named("reports_data")
    private YMLFile reportsData;

    private final Map<UUID, Report> reports = new ConcurrentHashMap<>();

    @Override
    public Map<UUID, Report> get() {
        return reports;
    }

    @Override
    public Optional<Report> find(UUID uuid) {
        return Optional.ofNullable(reports.get(uuid));
    }

    @Override
    public Optional<Report> findFromData(UUID uuid) {
        if (!reportsData.contains("reports." + uuid.toString())) {
            return Optional.empty();
        }

        Object o = reportsData.get("reports." + uuid.toString());

        if (o instanceof Map) {
            return Optional.of(new Report((Map<String, Object>) o));
        } else if (o instanceof ConfigurationSection) {
            return Optional.of(
                    new Report(
                            reportsData.getConfigurationSection("reports." + uuid.toString()).getValues(false)
                    )
            );
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void save(UUID uuid) {
        find(uuid).ifPresent(report -> {
            reportsData.set("reports." + uuid.toString(), report.serialize());
            reportsData.save();

            remove(uuid);
        });
    }

    @Override
    public void saveObject(UUID key, Report value) {
        reportsData.set("reports." + key.toString(), value.serialize());
        reportsData.save();

        remove(key);
    }

    @Override
    public void remove(UUID uuid) {
        reports.remove(uuid);
    }

    @Override
    public void add(UUID uuid, Report report) {
        reports.put(uuid, report);
    }

    @Override
    public void saveAll() {
        reports.keySet().forEach(this::save);
    }

    @Override
    public void loadAll() {
        if (!reportsData.contains("reports")) {
            return;
        }

        reportsData.getConfigurationSection("reports").getKeys(false).forEach(s -> findFromData(UUID.fromString(s)).ifPresent(report -> add(UUID.fromString(s), report)));

    }

}
