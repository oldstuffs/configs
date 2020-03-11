package io.github.portlek.configs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * Gets minecraft version from
 * package version of the server.
 */
public final class BukkitVersion {

    /**
     * Pattern of the server text
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
    public BukkitVersion() {
        this(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1));
    }

    /**
     * @param version Minecraft server package name
     */
    public BukkitVersion(@NotNull final String version) {
        this.version = version;
    }

    /**
     * Gets raw string of the version
     *
     * @return raw string
     * output is like that "(major)_(minor)_R(micro)"
     */
    @NotNull
    public String raw() {
        return this.version;
    }

    /**
     * Gets major part of the version
     *
     * @return major part
     */
    public int major() {
        final Matcher matcher = BukkitVersion.PATTERN.matcher(this.version);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group("major"));
        }
        return 0;
    }

    /**
     * Gets minor part of the version
     *
     * @return minor part
     */
    public int minor() {
        final Matcher matcher = BukkitVersion.PATTERN.matcher(this.version);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group("minor"));
        }
        return 0;
    }

    /**
     * Gets micro part of the version
     *
     * @return micro part
     */
    public int micro() {
        final Matcher matcher = BukkitVersion.PATTERN.matcher(this.version);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group("micro"));
        }
        return 0;
    }

}
