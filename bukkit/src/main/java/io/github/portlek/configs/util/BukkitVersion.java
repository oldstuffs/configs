package io.github.portlek.configs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * Gets minecraft version from
 * package version from the server.
 */
final class BukkitVersion {

    /**
     * Pattern from the server text
     * <p>
     * The pattern is like that
     * (major)_(minor)_R(micro)
     */
    @NotNull
    private static final Pattern PATTERN = Pattern.compile("v?(?<major>[0-9]+)[._](?<minor>[0-9]+)(?:[._](?<micro>[0-9]+))?(?<sub>.*)");

    /**
     * Server version text
     */
    @NotNull
    private final String version;

    /**
     * Initiates with current running server package name
     */
    BukkitVersion() {
        this(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1));
    }

    /**
     * @param vrsn Minecraft server package name
     */
    BukkitVersion(@NotNull final String vrsn) {
        this.version = vrsn;
    }

    /**
     * Gets minor part from the version
     *
     * @return minor part
     */
    public int minor() {
        final Matcher matcher = BukkitVersion.PATTERN.matcher(this.version);
        final int minor;
        if (matcher.matches()) {
            minor = Integer.parseInt(matcher.group("minor"));
        } else {
            minor = 0;
        }
        return minor;
    }

}
