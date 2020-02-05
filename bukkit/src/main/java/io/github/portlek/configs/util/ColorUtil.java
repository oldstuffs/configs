package io.github.portlek.configs.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ColorUtil {

    private ColorUtil() {
    }

    @NotNull
    public static String colored(@NotNull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @NotNull
    public static List<String> colored(@NotNull List<String> list) {
        return list.stream().map(ColorUtil::colored).collect(Collectors.toList());
    }

    @NotNull
    public static List<String> colored(@NotNull String... array) {
        return colored(
            Arrays.asList(array)
        );
    }

}
