package io.github.portlek.configs.annotations;

import io.github.portlek.configs.ManagedBase;
import org.jetbrains.annotations.NotNull;

public @interface LinkedConfig {

    @NotNull
    Class<? extends ManagedBase>[] linkedFiles();

}
