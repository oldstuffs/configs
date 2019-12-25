package io.github.portlek.configs;

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

}
