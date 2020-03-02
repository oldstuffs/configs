package io.github.portlek.configs;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.util.FileType;
import org.jetbrains.annotations.NotNull;

@LinkedConfig(configs = {
    @Config(
        name = "test",
        type = FileType.JSON,
        location = "%basedir%/QuickShop/languages",
        copyDefault = true
    ),
    @Config(
        name = "test_1",
        type = FileType.JSON,
        location = "%basedir%/QuickShop/languages",
        copyDefault = true
    )
})
public final class TestLinkedResourceConfig extends LinkedManagedBase {

    public TestLinkedResourceConfig(@NotNull final String name) {
        super(name);
    }

}
