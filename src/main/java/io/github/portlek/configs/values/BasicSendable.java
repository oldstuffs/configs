package io.github.portlek.configs.values;

import io.github.portlek.actionbar.base.ActionBarPlayerOf;
import io.github.portlek.configs.Replaceable;
import io.github.portlek.configs.Sendable;
import org.bukkit.entity.Player;
import org.cactoos.collection.CollectionOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class BasicSendable implements Sendable {

    @NotNull
    private final Replaceable replaceable;

    public BasicSendable(@NotNull Replaceable replaceable) {
        this.replaceable = replaceable;
    }

    @Override
    public void sendMessage(@NotNull Player... players) {
        sendMessage(
            new CollectionOf<>(players)
        );
    }

    @Override
    public void sendMessage(@NotNull Iterable<Player> players) {
        players.forEach(player ->
            player.sendMessage(replaceable.build())
        );
    }

    @Override
    public void sendActionbar(@NotNull Player... players) {
        sendActionbar(
            new CollectionOf<>(players)
        );
    }

    @Override
    public void sendActionbar(@NotNull Iterable<Player> players) {
        players.forEach(player ->
            new ActionBarPlayerOf(player).sendActionBar(
                replaceable.build()
            )
        );
    }

    @NotNull
    @Override
    public Sendable replace(@NotNull String regex, @NotNull Object replace) {
        replaceable.replace(regex, replace);

        return this;
    }

    @NotNull
    @Override
    public Sendable replace(@NotNull List<Map.Entry<@NotNull String, @NotNull Object>> replaces) {
        replaceable.replace(replaces);

        return this;
    }

    @NotNull
    @Override
    public Sendable replace(@NotNull Map<@NotNull String, @NotNull Object> replaces) {
        replaceable.replace(replaces);

        return this;
    }

    @NotNull
    @Override
    public Sendable clearReplaces() {
        replaceable.clearReplaces();

        return this;
    }

    @NotNull
    @Override
    public String build() {
        return replaceable.build();
    }

    @Override
    @Deprecated
    public void sendTitle(@NotNull Player... players) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void sendTitle(@NotNull Iterable<Player> players) {
        throw new UnsupportedOperationException();
    }

}
