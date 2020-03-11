/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.github.portlek.configs;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.FileType;
import org.jetbrains.annotations.NotNull;

@Config(
    name = "config",
    type = FileType.JSON,
    version = "1.1"
)
public final class TestConfig extends ManagedBase {

    @Instance
    public final Hooks hooks = new Hooks();

    @Instance
    public final Saving saving = new Saving();

    @Value
    public String plugin_prefix = "&6[&eExamplePlugin&6]";

    @Value
    public String plugin_language = "en";

    @Value
    public boolean check_for_update = true;

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

    @Section(path = "saving")
    public static class Saving {

        @Instance
        public final MySQL mysql = new MySQL();

        @Value
        public boolean save_when_plugin_disable = true;

        @Value
        public boolean auto_save = true;

        @Value
        public int auto_save_time = 60;

        @NotNull
        @Value
        private String storage_type = "sqlite";

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
