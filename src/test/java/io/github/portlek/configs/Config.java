package io.github.portlek.configs;

import io.github.portlek.configs.annotations.File;
import io.github.portlek.configs.annotations.values.Value;

@File()
public final class Config {

    @Value(stringValue = "&6[&eExamplePlugin&6]")
    public String plugin_prefix;

    @Value(stringValue = "en")
    public String plugin_language;

}
