package io.github.portlek.configs;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.ColorUtil;
import io.github.portlek.configs.util.MapEntry;
import io.github.portlek.configs.util.Replaceable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@LinkedConfig(configs = {
    @Config(
        name = "en",
        location = "%basedir%/TestBukkitPlugin/languages"
    )
})
public final class TestBukkitLinkedConfig extends BukkitLinkedManaged {

    @Instance
    public final Errors errors = new Errors();

    @Instance
    public final Generals generals = new Generals();

    public TestBukkitLinkedConfig(@NotNull TestBukkitConfig configFile) {
        super(configFile.plugin_language, MapEntry.from("config", configFile));
    }

    @NotNull
    public Map<String, Supplier<String>> getPrefix() {
        final Map<String, Supplier<String>> prefix = new HashMap<>();
        pull("config").ifPresent(o ->
            prefix.put("%prefix%", () -> ((TestBukkitConfig) o).plugin_prefix.build())
        );

        return prefix;
    }

    @CfgSection(path = "error")
    public class Errors {

        @Value
        public Replaceable<String> already_exist_in_item_slot = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &7This command cannot be used because an some item already exists in slot 9.")
                    .map(ColorUtil::colored)
                    .replace(getPrefix())
            )
        );

        @Value
        public Replaceable<String> no_permission = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &7No perm for this command!")
                    .map(ColorUtil::colored)
                    .replace(getPrefix())
            )
        );

        @Value
        public Replaceable<String> blacklist_command = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &7The command cannot be used with the cursor in slot 9!")
                    .map(ColorUtil::colored)
                    .replace(getPrefix())
            )
        );

    }

    @CfgSection(path = "general")
    public class Generals {

        @Value
        public Replaceable<String> reload_complete = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &aReload complete! &7Took (%ms%)")
                    .map(ColorUtil::colored)
                    .replaces("%ms%")
                    .replace(getPrefix())
            )
        );

        @Value
        public Replaceable<String> success = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &7Display GUI items.")
                    .map(ColorUtil::colored)
                    .replace(getPrefix())
            )
        );

        @Value
        public Replaceable<String> off_success = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &7Hide GUI items.")
                    .map(ColorUtil::colored)
                    .replace(getPrefix())
            )
        );

    }

}

