package com.kino.kreports.storage.user;


import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import team.unnamed.inject.Inject;
import team.unnamed.inject.name.Named;

import java.util.HashMap;
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
    public Optional<UUID> getKey(User user) {

        Map<User, UUID> map = new HashMap<>();

        for (UUID uuid : users.keySet()) {
            map.put(users.get(uuid), uuid);
        }

        return Optional.ofNullable(map.get(user));
    }

    @Override
    public Optional<User> findFromData(UUID uuid) {
        if (!playerData.contains("users." + uuid.toString())) {
            return Optional.empty();
        }

        Object o = playerData.get("users." + uuid.toString());

        if (o instanceof Map) {
            if (((Map) o).containsKey("staff")) {
                return Optional.of(new Staff((Map<String, Object>) o));
            } else {
                return Optional.of(new SimpleUser((Map<String, Object>) o));
            }
        } else if (o instanceof ConfigurationSection) {
            if(((ConfigurationSection) o).contains("staff")) {
                return Optional.of(
                        new Staff(
                                playerData.getConfigurationSection("users." + uuid.toString()).getValues(false)
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
    public Optional<UUID> getKeyFromData(User user) {

        for (String key : playerData.getConfigurationSection("users").getKeys(false)) {

            Object o = playerData.get("users." + key);

            if (o instanceof Map) {
                User u;
                if (((Map) o).containsKey("staff")) {
                    u = new Staff((Map<String, Object>) o);
                } else {
                    u = new SimpleUser((Map<String, Object>) o);
                }
                if(u.equals(user)) {
                    return Optional.of(UUID.fromString(key));
                } else {
                    return Optional.empty();
                }
            } else if (o instanceof ConfigurationSection) {
                User u;
                if(((ConfigurationSection) o).contains("staff")) {
                    u = new Staff(playerData.getConfigurationSection("users." + key).getValues(false));
                } else {
                    u = new SimpleUser(playerData.getConfigurationSection("users." + key).getValues(false));
                }
                if(u.equals(user)) {
                    return Optional.of(UUID.fromString(key));
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        }

        return Optional.empty();

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
