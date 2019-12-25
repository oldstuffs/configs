package io.github.portlek.configs;

import io.github.portlek.configs.annotations.BasicFile;
import io.github.portlek.configs.annotations.Languages;
import io.github.portlek.configs.util.Copied;
import io.github.portlek.configs.util.CreateStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.cactoos.io.InputOf;
import org.cactoos.io.InputStreamOf;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class AnnotationProcessor {

    private final Map<String, Object> CONSTANTS = new HashMap<>();

    @NotNull
    private final Plugin plugin;

    public AnnotationProcessor(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public <T> T load(@NotNull T t) {
        final Class<?> tClass = t.getClass();
        final BasicFile basicFile = tClass.getAnnotation(BasicFile.class);
        final Languages languages = tClass.getAnnotation(Languages.class);
        /*final*/ FileConfiguration fileConfiguration = null;

        if (basicFile != null) {
            if (basicFile.fileName().isEmpty()) {
                throw new IllegalStateException("File name of " + t.getClass().getSimpleName() + " cannot be null!");
            }

            final StringBuilder resourcePathBuilder = new StringBuilder(
                basicFile.resourcePath().replace("/", File.separator)
            );

            if (!basicFile.resourcePath().endsWith(File.separator)) {
                resourcePathBuilder.append(File.separator);
            }

            final String resourcePath = resourcePathBuilder.toString();
            final String directoryPath = plugin.getDataFolder().getAbsolutePath() +
                File.separator +
                resourcePath;
            final String fileName = basicFile.fileName().endsWith(basicFile.fileType().getSuffix())
                ? basicFile.fileName()
                : basicFile.fileName() + basicFile.fileType().getSuffix();

            final File file;

            if (basicFile.copyDefault()) {
                file = new Copied(
                    new CreateStorage(
                        directoryPath,
                        fileName
                    ),
                    new InputStreamOf(
                        new InputOf(
                            plugin.getResource(
                                resourcePath + fileName
                            )
                        )
                    )
                ).value();
            } else {
                file = new CreateStorage(
                    directoryPath,
                    fileName
                ).value();
            }

            switch (basicFile.fileType()) {
                case YAML:
                    fileConfiguration = YamlConfiguration.loadConfiguration(file);
                    break;
                case XML:
                    // TODO: 26/12/2019
                    break;
                case JSON:
                    // TODO: 26/12/2019
                    break;
            }
        } else if (languages != null) {
            final File directory = new File(plugin.getDataFolder(), languages.path());

            directory.mkdirs();


        }

        return t;
    }

    public void define(@NotNull String regex, @NotNull Object object) {
        CONSTANTS.put(regex, object);
    }

}
