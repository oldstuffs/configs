package io.github.portlek.configs;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Sendable extends Replaceable {

    void sendMessage(@NotNull Player... players);

    void sendMessage(@NotNull Collection<Player> players);

    void sendTitle(@NotNull Player... players);

    void sendTitle(@NotNull Collection<Player> players);

    void sendActionbar(@NotNull Player... players);

    void sendActionbar(@NotNull Collection<Player> players);

}
