package io.github.portlek.configs.file;

import io.github.portlek.configs.SendableTitle;
import io.github.portlek.configs.annotations.BasicFile;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.values.BasicReplaceable;
import io.github.portlek.configs.values.BasicSendableTitle;
import javafx.beans.binding.When;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@BasicFile(
    fileName = "config",
    header = "Test for header\n" +
        "Test header for new line",
    configVersion = "1.2"
)
public final class Config {

    @Instance
    public test_1 test_1 = new test_1();

    @Value(
        comment = {
            "Test for comment",
            "Test comment for new line"
        }
    )
    @NotNull
    public String plugin_prefix = "&6[&eExamplePlugin&6]";

    @Value(comment = "Plugin language can be 'en' and 'tr'")
    @NotNull
    public String plugin_language = "en";

    @Value(comment = "Test for list values")
    @NotNull
    public String[] test_list = {"test-1", "test-2"};

    @Value(comment = "Test for long values")
    public long test_long = Long.MAX_VALUE;

    @Value(comment = "Test for int values")
    public int test_integer = Integer.MAX_VALUE;

    @Value(comment = "Test for sendable title values")
    public SendableTitle test_title = new BasicSendableTitle(
        new BasicReplaceable("test-title"),
        new BasicReplaceable("test-sub-title"),
        20,
        20,
        20
    );

    @Section(header = "Test for configuration section")
    public static final class test_1 {

        @Instance
        public test_2 test_2 = new test_2();

        @Value(comment = "Test for sendable title values")
        public SendableTitle test_title = new BasicSendableTitle(
            new BasicReplaceable("test-title"),
            new BasicReplaceable("test-sub-title"),
            20,
            20,
            20
        );

        @Section(header = "Test for configuration section")
        public static final class test_2 {

            @Instance
            public test_3 test_3 = new test_3();

            @Value(comment = "Test for sendable title values")
            public SendableTitle test_title = new BasicSendableTitle(
                new BasicReplaceable("test-title"),
                new BasicReplaceable("test-sub-title"),
                20,
                20,
                20
            );

            @Section(header = "Test for configuration section")
            public static final class test_3 {

                @Value(comment = "Test for sendable title values")
                public SendableTitle test_title = new BasicSendableTitle(
                    new BasicReplaceable("test-title"),
                    new BasicReplaceable("test-sub-title"),
                    20,
                    20,
                    20
                );

            }

        }
    }

}
