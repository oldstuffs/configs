package io.github.portlek.configs.file;

import io.github.portlek.configs.annotations.BasicFile;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    public List<String> test_list = new ListOf<>("test-1", "test-2");

    @Value
    public int test_integer = 0;

    @Section
    public static final class test_1 {

    }

    @Section
    private static final class test_2 {

    }

}
