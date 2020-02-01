package io.github.portlek.configs;

import io.github.portlek.configs.annotations.LinkedConfig;
import org.jetbrains.annotations.NotNull;

@LinkedConfig(
    linkedFiles = {}
)
public final class TestLinkedConfig extends LinkedManagedBase {

    public TestLinkedConfig(@NotNull String fileId) {
        super(fileId);
    }
    
}
