package io.github.portlek.configs.values;

import io.github.portlek.configs.Replaceable;
import io.github.portlek.configs.SendableTitle;
import io.github.portlek.title.base.TitlePlayerOf;
import org.bukkit.entity.Player;
import org.cactoos.collection.CollectionOf;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class BasicTitleValue implements SendableTitle {

    @NotNull
    private final Replaceable replaceableTitle;

    @NotNull
    private final Replaceable replaceableSubTitle;

    private final int fadeIn;

    private final int showTime;

    private final int fadeOut;

    public BasicTitleValue(@NotNull Replaceable replaceableTitle, @NotNull Replaceable replaceableSubTitle, int fadeIn,
                           int showTime, int fadeOut) {
        this.replaceableTitle = replaceableTitle;
        this.replaceableSubTitle = replaceableSubTitle;
        this.fadeIn = fadeIn;
        this.showTime = showTime;
        this.fadeOut = fadeOut;
    }

    @NotNull
    @Override
    public SendableTitle replaceTile(@NotNull String regex, @NotNull Object replace) {
        replaceableTitle.replace(regex, replace);

        return this;
    }

    @NotNull
    @Override
    public SendableTitle replaceSubTile(@NotNull String regex, @NotNull Object replace) {
        replaceableSubTitle.replace(regex, replace);

        return this;
    }

    @NotNull
    @Override
    public Replaceable title() {
        return replaceableTitle;
    }

    @NotNull
    @Override
    public Replaceable subTitle() {
        return replaceableSubTitle;
    }

    @Override
    public int fadeIn() {
        return fadeIn;
    }

    @Override
    public int showTime() {
        return showTime;
    }

    @Override
    public int fadeOut() {
        return fadeOut;
    }

    @Override
    public void sendTitle(@NotNull Player... players) {
        sendTitle(
            new CollectionOf<>(players)
        );
    }

    @Override
    public void sendTitle(@NotNull Collection<Player> players) {
        players.forEach(player ->
            new TitlePlayerOf(player).sendTitle(
                replaceableTitle.build(),
                replaceableSubTitle.build(),
                fadeIn,
                showTime,
                fadeOut
            )
        );
    }

    @NotNull
    @Override
    public SendableTitle replace(@NotNull String regex, @NotNull Object replace) {
        replaceableTitle.replace(regex, replace);
        replaceableSubTitle.replace(regex, replace);

        return this;
    }

    @NotNull
    @Override
    public SendableTitle replace(@NotNull List<Map.Entry<@NotNull String, @NotNull Object>> replaces) {
        replaceableTitle.replace(replaces);
        replaceableSubTitle.replace(replaces);

        return this;
    }

    @NotNull
    @Override
    public SendableTitle replace(@NotNull Map<@NotNull String, @NotNull Object> replaces) {
        replaceableTitle.replace(replaces);
        replaceableSubTitle.replace(replaces);

        return this;
    }

    @NotNull
    @Override
    public SendableTitle clearReplaces() {
        replaceableTitle.clearReplaces();
        replaceableSubTitle.clearReplaces();

        return this;
    }

    @NotNull
    @Override
    public String build() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendMessage(@NotNull Player... players) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendMessage(@NotNull Collection<Player> players) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendActionbar(@NotNull Player... players) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendActionbar(@NotNull Collection<Player> players) {
        throw new UnsupportedOperationException();
    }

}
