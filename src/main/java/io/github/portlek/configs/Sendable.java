package io.github.portlek.configs;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Sendable extends Replaceable {

    void sendMessage(@NotNull Player... players);

    void sendMessage(@NotNull Iterable<Player> players);

    void sendTitle(@NotNull Player... players);

    void sendTitle(@NotNull Iterable<Player> players);

    void sendActionbar(@NotNull Player... players);

    void sendActionbar(@NotNull Iterable<Player> players);

}
