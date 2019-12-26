package io.github.portlek.configs;

import io.github.portlek.configs.annotations.*;
import io.github.portlek.configs.util.Copied;
import io.github.portlek.configs.util.CreateStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.cactoos.io.InputOf;
import org.cactoos.io.InputStreamOf;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
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
            final String directoryPath = plugin.getDataFolder().getAbsolutePath() + File.separator + resourcePath;
            final String fileName;

            if (basicFile.fileName().endsWith(basicFile.fileType().getSuffix())) {
                fileName = basicFile.fileName();
            } else {
                fileName = basicFile.fileName() + basicFile.fileType().getSuffix();
            }

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
                // TODO: 26/12/2019
                default:
                    fileConfiguration = YamlConfiguration.loadConfiguration(file);
                    break;
            }

            for (Field field : tClass.getDeclaredFields()) {
                final boolean accessible = field.isAccessible();

                field.setAccessible(true);

                final Value value = field.getDeclaredAnnotation(Value.class);
                final Instance instance = field.getDeclaredAnnotation(Instance.class);

                if (value != null) {
                    final boolean isPrimitive = isPrimitive(field.getType());
                    final String path;

                    if (value.path().isEmpty()) {
                        path = field.getName().replace("_", value.separator());
                    } else {
                        path = value.path();
                    }

                    final Object defaultValue;

                    try {
                        defaultValue = field.get(t);
                    } catch (Exception exception) {
                        continue;
                    }

                    if (defaultValue == null) {
                        continue;
                    }

                    final Object tempValue = fileConfiguration.get(path);

                    if (isPrimitive) {
                        try {
                            if (tempValue == null) {
                                fileConfiguration.set(path, defaultValue);
                                fileConfiguration.save(file);
                            } else {
                                field.set(t, tempValue);
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {

                    }
                } else if (instance != null) {
                    for (Constructor<?> constructor : field.getType().getDeclaredConstructors()) {
                        final boolean accessibleCtor = constructor.isAccessible();

                        constructor.setAccessible(true);

                        if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].equals(tClass)) {
                            try {
                                field.set(t, load(constructor.newInstance(t)));
                            } catch (Exception ignored) {
                                // ignored
                            }
                            break;
                        } else if (constructor.getParameterCount() == 0) {
                            try {
                                final Object object = load(constructor.newInstance());

                                field.set(t, object);
                            } catch (Exception ignored) {
                                // ignored
                            }
                            break;
                        }

                        constructor.setAccessible(accessibleCtor);
                    }
                }

                field.setAccessible(accessible);
            }

            for (Class<?> innerClass : tClass.getDeclaredClasses()) {
                final Section section = innerClass.getAnnotation(Section.class);

                if (section != null) {
                    final String path;

                    if (section.path().isEmpty()) {
                        path = innerClass.getSimpleName().replace("_", section.separator());
                    } else {
                        path = section.path();
                    }


                }
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

    private boolean isPrimitive(@NotNull Class<?> clazz) {
        return new ListOf<>(
            String.class,
            List.class,
            Number.class
        ).stream().anyMatch(aClass -> aClass.isAssignableFrom(clazz));
    }

}
