package io.github.portlek.configs.files.yaml.eoyaml;

/**
 * The class is for defining the type of {@link YamlNode}.
 */
public enum Node {

    /**
     * Type of {@link Scalar}.
     */
    SCALAR,
    /**
     * Type of {@link YamlMapping}.
     */
    MAPPING,
    /**
     * Type of {@link YamlStream}.
     */
    STREAM,
    /**
     * Type of {@link YamlSequence}.
     */
    SEQUENCE

}
