package com.kino.kreports.models.user;

import com.kino.kreports.stats.Statistic;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public interface User extends ConfigurationSerializable, Statistic {

    UUID getUUID();

    int getID();

    void setID(int id);

    @Override
    default Map<String, Object> serialize() {
        Map<String, Object> playerMap = new LinkedHashMap<>();

        playerMap.put("reports", getReports().get());
        playerMap.put("bans", getBans().get());
        playerMap.put("kicks", getKicks().get());
        playerMap.put("warns", getWarns().get());
        playerMap.put("mutes", getMutes().get());

        return playerMap;
    }
}
