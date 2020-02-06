package io.github.portlek.configs;

import io.github.portlek.configs.annotations.*;
import io.github.portlek.configs.util.ColorUtil;
import io.github.portlek.configs.util.Replaceable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@LinkedConfig(configs = {
    @Config(
        name = "en",
        location = "%basedir%/AGUI/languages"
    )
})
public final class TestBukkitLinkedConfig extends BukkitLinkedManaged {

    private final Map<String, Supplier<String>> prefix = new HashMap<>();

    public TestBukkitLinkedConfig(@NotNull TestBukkitConfig configFile) {
        super(configFile.plugin_language);

        prefix.put("%prefix%", configFile.plugin_prefix::build);
    }

    @Instance
    public final Errors errors = new Errors();

    @Section(path = "error")
    public class Errors {

        @Value
        public Replaceable<String> already_exist_in_item_slot = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &7This command cannot be used because an some item already exists in slot 9.")
                    .map(ColorUtil::colored)
                    .replace(prefix)
            )
        );

        @Value
        public Replaceable<String> no_permission = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &7No perm for this command!")
                    .map(ColorUtil::colored)
                    .replace(prefix)
            )
        );

        @Value
        public Replaceable<String> blacklist_command = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &7The command cannot be used with the cursor in slot 9!")
                    .map(ColorUtil::colored)
                    .replace(prefix)
            )
        );

    }

    @Instance
    public final Generals generals = new Generals();

    @Section(path = "general")
    public class Generals {

        @Value
        public Replaceable<String> reload_complete = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &aReload complete! &7Took (%ms%)")
                    .map(ColorUtil::colored)
                    .replaces("%ms%")
                    .replace(prefix)
            )
        );

        @Value
        public Replaceable<String> success = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &7Display GUI items.")
                    .map(ColorUtil::colored)
                    .replace(prefix)
            )
        );

        @Value
        public Replaceable<String> off_success = match(s ->
            Optional.of(
                Replaceable.of("%prefix% &7Hide GUI items.")
                    .map(ColorUtil::colored)
                    .replace(prefix)
            )
        );

    }

}

