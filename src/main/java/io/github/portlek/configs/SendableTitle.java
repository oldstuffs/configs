package io.github.portlek.configs;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SendableTitle extends Sendable {

    @NotNull
    SendableTitle replaceTile(@NotNull String regex, @NotNull Object replace);

    @NotNull
    SendableTitle replaceSubTile(@NotNull String regex, @NotNull Object replace);

    @NotNull
    Replaceable title();

    @NotNull
    Replaceable subTitle();

    int fadeIn();

    int showTime();

    int fadeOut();

    @Deprecated
    @NotNull
    String build();

    @Deprecated
    @Override
    void sendMessage(@NotNull Player... players);

    @Deprecated
    @Override
    void sendMessage(@NotNull Iterable<Player> players);

    @Deprecated
    @Override
    void sendActionbar(@NotNull Player... players);

    @Deprecated
    @Override
    void sendActionbar(@NotNull Iterable<Player> players);

}
