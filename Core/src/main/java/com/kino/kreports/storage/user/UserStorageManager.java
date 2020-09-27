package com.kino.kreports.storage.user;


import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.models.user.SimpleUser;
import com.kino.kreports.models.user.Staff;
import com.kino.kreports.models.user.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import team.unnamed.inject.Inject;
import team.unnamed.inject.name.Named;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class UserStorageManager implements Storage<UUID, User> {

    @Inject
    @Named("user_data")
    private YMLFile playerData;

    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    @Override
    public Map<UUID, User> get() {
        return users;
    }

    @Override
    public Optional<User> find(UUID uuid) {
        return Optional.ofNullable(users.get(uuid));
    }

    @Override
    public Optional<User> findFromData(UUID uuid) {
        if (!playerData.contains("users." + uuid.toString())) {
            return Optional.empty();
        }

        Object o = playerData.get("users." + uuid.toString());

        if (o instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) o;
            if (map.containsKey("staff")) {
                return Optional.of(new Staff(map));
            } else {
                return Optional.of(new SimpleUser(map));
            }
        } else if (o instanceof ConfigurationSection) {
            if(((ConfigurationSection) o).contains("staff")) {
                return Optional.of(
                        new Staff(
                                playerData.getConfigurationSection("users." + uuid.toString())
                        )
                );
            } else {
                return Optional.of(
                        new SimpleUser(
                                playerData.getConfigurationSection("users." + uuid.toString()).getValues(false)
                        )
                );
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void save(UUID uuid) {
        find(uuid).ifPresent(user -> {
            playerData.set("users." + uuid.toString(), user.serialize());
            playerData.save();

            remove(uuid);
        });
    }

    @Override
    public void saveObject(UUID key, User value) {
        playerData.set("users." + key.toString(), value.serialize());
        playerData.save();

        remove(key);
    }

    @Override
    public void remove(UUID uuid) {
        users.remove(uuid);
    }

    @Override
    public void add(UUID uuid, User user) {
        users.put(uuid, user);
    }

    @Override
    public void saveAll() {
        users.keySet().forEach(this::save);
    }

    @Override
    public void loadAll() {
        if (!playerData.contains("users")) {
            return;
        }

        Bukkit.getOnlinePlayers().forEach(player -> findFromData(player.getUniqueId()).ifPresent(user -> add(player.getUniqueId(), user)));
    }


}
