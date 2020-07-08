package net.nighthawkempires.races.user.registry;

import com.google.common.collect.ImmutableList;
import net.nighthawkempires.core.datasection.DataSection;
import net.nighthawkempires.core.datasection.Registry;
import net.nighthawkempires.races.user.UserModel;

import java.util.Map;
import java.util.UUID;

public interface UserRegistry extends Registry<UserModel> {

    String NAME = "users";

    default UserModel fromDataSection(String key, DataSection data) {
        return new UserModel(key, data);
    }

    default UserModel getUser(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return fromKey(uuid.toString()).orElseGet(() -> register(new UserModel(uuid)));
    }

    @Deprecated
    Map<String, UserModel> getRegisteredData();

    default Map<String, UserModel> getDatabase() {
        return loadAllFromDb();
    }

    default ImmutableList<UserModel> getUsers() {
        return ImmutableList.copyOf(getDatabase().values());
    }

    default boolean userExists(UUID uuid) {
        return fromKey(uuid.toString()).isPresent();
    }
}