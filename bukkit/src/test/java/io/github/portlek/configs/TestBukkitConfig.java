package io.github.portlek.configs;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.ColorUtil;
import io.github.portlek.configs.util.Replaceable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Config(
    name = "config",
    location = "%basedir%/TestBukkitPlugin"
)
public final class TestBukkitConfig extends BukkitManaged {

    @Value
    public Replaceable<String> plugin_prefix = Replaceable.of("&6[&eTestBukkitPlugin&6]")
        .map(ColorUtil::colored);

    @Value
    public String plugin_language = "en";

    @Instance
    public final Hooks hooks = new Hooks();

    @Section(path = "hooks")
    public static class Hooks {

        @Value
        public Boolean auto_detect = true;

        @Value
        private Boolean PlaceholderAPI = false;

    }

    @Value
    public double click_cooldown = 0.5;

    @Value
    public boolean first_join_user_on = true;

    @Value
    public List<String> blacklist_commands = Arrays.asList(
        "ah sell", "drop", "auction sell"
    );

    @Value
    public Replaceable<String> actionbar_message = Replaceable.of("/agui [ON|OFF] to turn the item on and off.")
        .map(ColorUtil::colored);

    @Value
    public List<String> right_click_command = Collections.singletonList("test 1'");

    @Value
    public List<String> left_click_command= Collections.singletonList("test 2");

    @Value
    public List<String> shift_left_click_command= Collections.singletonList("test 3");

    @Value
    public List<String> shift_right_click_command= Collections.singletonList("test 4");

}