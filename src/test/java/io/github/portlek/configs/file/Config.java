package io.github.portlek.configs.file;

import io.github.portlek.configs.FileType;
import io.github.portlek.configs.annotations.BasicFile;
import io.github.portlek.configs.annotations.values.Value;

@BasicFile(fileName = "config", fileType = FileType.YAML)
public final class Config {

    @Value(prefixValue = "&6[&eExamplePlugin&6]")
    public String plugin_prefix;

    @Value(stringValue = "en")
    public String plugin_language;

}
