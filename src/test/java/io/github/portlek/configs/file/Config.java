package io.github.portlek.configs.file;

import io.github.portlek.configs.annotations.BasicFile;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;

@BasicFile(fileName = "config")
public final class Config {

    @Instance
    public test_1 test_1;

    @Instance
    public test_2 test_2;

    @Value(stringValue = "&6[&eExamplePlugin&6]")
    public String plugin_prefix;

    @Value(stringValue = "en")
    public String plugin_language;

    @Value(stringArrayValue = {"test-1", "test-2"})
    public String[] test_array;

    @Section
    public static final class test_1 {

    }

    @Section
    private static final class test_2 {

    }

}
