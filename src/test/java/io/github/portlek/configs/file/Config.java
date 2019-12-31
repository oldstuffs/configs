package io.github.portlek.configs.file;

import io.github.portlek.configs.SendableTitle;
import io.github.portlek.configs.annotations.BasicFile;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.values.BasicReplaceable;
import io.github.portlek.configs.values.BasicSendableTitle;
import org.jetbrains.annotations.NotNull;

@BasicFile(fileName = "config")
public final class Config {

    @Instance
    public test_1 test_1;

    @Instance
    public test_2 test_2;

    @Value
    @NotNull
    public String plugin_prefix = "&6[&eExamplePlugin&6]";

    @Value
    @NotNull
    public String plugin_language = "en";

    @Value
    @NotNull
    public String[] test_list = {"test-1", "test-2"};

    @Value
    public Integer test_integer = 0;

    @Value
    public SendableTitle test_title = new BasicSendableTitle(
        new BasicReplaceable("test-title"),
        new BasicReplaceable("test-sub-title"),
        20,
        20,
        20
    );

    @Section
    public static final class test_1 {

    }

    @Section
    private static final class test_2 {

    }

}
