package io.github.portlek.configs;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import org.jetbrains.annotations.NotNull;

@Config(
    name = "config",
    type = FileType.JSON,
    version = "1.1"
)
public final class TestConfig extends ManagedBase {

    @Value
    public String plugin_prefix = "&6[&eExamplePlugin&6]";

    @Value
    public String plugin_language = "en";

    @Value
    public boolean check_for_update = true;

    @Instance
    public final Hooks hooks = new Hooks();

    @Section(path = "hooks")
    public static class Hooks {

        @Value
        public boolean auto_detect = true;

        @Value
        private boolean PlaceholderAPI = false;

        @Value
        private boolean GroupManager = false;

        @Value
        private boolean LuckPerms = false;

        @Value
        private boolean PermissionsEX = false;

        @Value
        private boolean Vault = false;

    }

    @Instance
    public final Saving saving = new Saving();

    @Section(path = "saving")
    public static class Saving {

        @Value
        public boolean save_when_plugin_disable = true;

        @Value
        public boolean auto_save = true;

        @Value
        public int auto_save_time = 60;

        @NotNull
        @Value
        private String storage_type = "sqlite";

        @Instance
        public final MySQL mysql = new MySQL();

        @Section(path = "mysql")
        public static class MySQL {

            @Value
            private String host = "localhost";

            @Value
            private int port = 3306;

            @Value
            private String database = "database";

            @Value
            private String username = "username";

            @Value
            private String password = "password";

        }

    }

}
