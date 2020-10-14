package com.kino.kreports.utils.user;

import com.kino.kore.utils.files.YMLFile;
import com.kino.kore.utils.storage.Storage;
import com.kino.kreports.models.reports.Report;
import com.kino.kreports.models.user.Staff;
import com.kino.kreports.models.user.User;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.name.Named;

import java.util.UUID;

@InjectAll
public class UserUtils {

    private Storage<UUID, User> playerStorage;

    @Named("user_data")
    private YMLFile playerData;

    public User fromUUID (UUID uuid) {

        return playerStorage.find(uuid).orElse(playerStorage.findFromData(uuid).orElse(null));
    }

    public UUID fromReport (User user) {

        for (String sUserUUID : playerData.getConfigurationSection("users").getKeys(false)) {
            UUID userUUID = UUID.fromString(sUserUUID);
            if (playerStorage.findFromData(userUUID).isPresent()) {
                User user1 = playerStorage.findFromData(userUUID).get();
                if (user.equals(user1)) {
                    return userUUID;
                }
            }
        }

        return null;
    }

    public boolean isStaff (User user) {

        return user instanceof Staff;

    }
}
