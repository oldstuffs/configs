package io.github.portlek.configs;

import io.github.portlek.configs.annotations.*;
import io.github.portlek.configs.util.Copied;
import io.github.portlek.configs.util.CreateStorage;
import io.github.portlek.configs.util.ItemBuilder;
import io.github.portlek.configs.values.BasicReplaceable;
import io.github.portlek.configs.values.BasicSendable;
import io.github.portlek.configs.values.BasicSendableTitle;
import io.github.portlek.itemstack.util.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
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
import java.util.Optional;

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
        final FileConfiguration fileConfiguration;

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
                // TODO: 31/12/2019
                case YAML:
                default:
                    fileConfiguration = YamlConfiguration.loadConfiguration(file);
                    break;
            }

            try {
                loadFields(t, tClass, fileConfiguration, "");
            } catch (Exception exception) {
                exception.printStackTrace();
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

                    try {
                        loadFields(t, innerClass, fileConfiguration, path);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            try {
                fileConfiguration.save(file);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else if (languages != null) {
            final File directory = new File(plugin.getDataFolder(), languages.path());

            directory.mkdirs();
        }

        return t;
    }

    private <T> void loadFields(@NotNull T t, Class<?> tClass, @NotNull FileConfiguration fileConfiguration,
                                @NotNull String before) throws Exception {
        for (Field field : tClass.getDeclaredFields()) {
            final boolean accessible = field.isAccessible();

            field.setAccessible(true);

            final Value value = field.getDeclaredAnnotation(Value.class);
            final Instance instance = field.getDeclaredAnnotation(Instance.class);

            if (value != null) {
                final boolean isPrimitive = isPrimitive(field.getType());
                final boolean isAllow = isAllow(field.getType());
                final String path;
                before = before.isEmpty() ? before : before + ".";

                if (value.path().isEmpty()) {
                    path = before + field.getName().replace("_", value.separator());
                } else {
                    path = before + value.path();
                }

                final Object defaultValue = field.get(t);

                if (defaultValue == null) {
                    continue;
                }

                if (isPrimitive) {
                    final Object tempValue = fileConfiguration.get(path);

                    if (tempValue == null) {
                        fileConfiguration.set(path, defaultValue);
                    } else {
                        field.set(t, tempValue);
                    }
                } else if (isAllow) {
                    if (defaultValue instanceof SendableTitle) {
                        final Object section = fileConfiguration.get(path);

                        if (section instanceof ConfigurationSection) {
                            final String title = ((ConfigurationSection) section).getString("title", "");
                            final String subTitle = ((ConfigurationSection) section).getString("sub-title", "");
                            final int fadeIn = ((ConfigurationSection) section).getInt("fade-in", 20);
                            final int showTime = ((ConfigurationSection) section).getInt("show-time", 20);
                            final int fadeOut = ((ConfigurationSection) section).getInt("fade-out", 20);

                            field.set(
                                t,
                                new BasicSendableTitle(
                                    new BasicReplaceable(title),
                                    new BasicReplaceable(subTitle),
                                    fadeIn,
                                    showTime,
                                    fadeOut
                                )
                            );
                        } else {
                            fileConfiguration.set(path + ".title", ((SendableTitle) defaultValue).title().build());
                            fileConfiguration.set(path + ".sub-title", ((SendableTitle) defaultValue).subTitle().build());
                            fileConfiguration.set(path + ".fade-in", ((SendableTitle) defaultValue).fadeIn());
                            fileConfiguration.set(path + ".show-time", ((SendableTitle) defaultValue).showTime());
                            fileConfiguration.set(path + ".fade-out", ((SendableTitle) defaultValue).fadeOut());
                        }
                    } else if (defaultValue instanceof Sendable) {
                        final Object object = fileConfiguration.get(path);

                        if (object instanceof String) {
                            field.set(
                                t,
                                new BasicSendable(
                                    new BasicReplaceable((String) object)
                                )
                            );
                        } else {
                            fileConfiguration.set(path, ((Sendable) defaultValue).build());
                        }
                    } else if (defaultValue instanceof Replaceable) {
                        final Object object = fileConfiguration.get(path);

                        if (object instanceof String) {
                            field.set(
                                t,
                                new BasicReplaceable((String) object)
                            );
                        } else {
                            fileConfiguration.set(path, ((Replaceable) defaultValue).build());
                        }
                    } else if (defaultValue instanceof XMaterial) {
                        final Object object = fileConfiguration.get(path);

                        if (object instanceof String) {
                            final Optional<XMaterial> optional = XMaterial.matchXMaterial((String) object);

                            if (optional.isPresent()) {
                                field.set(
                                    t,
                                    optional.get()
                                );
                            }
                        } else {
                            fileConfiguration.set(path, defaultValue);
                        }
                    } else if (defaultValue instanceof Object[]) {
                        final Object object = fileConfiguration.get(path);

                        if (object instanceof List<?>) {
                            field.set(
                                t,
                                ((List<?>) object).toArray(new Object[0])
                            );
                        } else {
                            fileConfiguration.set(path, new ListOf<>((Object[])defaultValue));
                        }
                    } else if (defaultValue instanceof ItemStack) {
                        final Object section = fileConfiguration.get(path);

                        if (section instanceof ConfigurationSection) {
                            final Optional<XMaterial> material = XMaterial.matchXMaterial(
                                ((ConfigurationSection) section).getString("material", "AIR")
                            );

                            final ItemBuilder itemBuilder = ItemBuilder.of(Material.AIR);

                            material.flatMap(xMaterial ->
                                Optional.ofNullable(xMaterial.parseMaterial())
                            ).ifPresent(itemBuilder::setType);
                            Optional.ofNullable(
                                ((ConfigurationSection) section).getString("display-name")
                            ).ifPresent(itemBuilder::name);

                            field.set(
                                t,
                                itemBuilder
                            );
                        } else {
                            fileConfiguration.set(path + ".material", ((ItemStack) defaultValue).getType().name());
                        }
                    }
                }
            } else if (instance != null) {
                for (Constructor<?> constructor : field.getType().getDeclaredConstructors()) {
                    final boolean accessibleCtor = constructor.isAccessible();

                    constructor.setAccessible(true);

                    if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].equals(tClass)) {
                        field.set(t, load(constructor.newInstance(t)));
                        break;
                    } else if (constructor.getParameterCount() == 0) {
                        final Object object = load(constructor.newInstance());

                        field.set(t, object);

                        break;
                    }

                    constructor.setAccessible(accessibleCtor);
                }
            }

            field.setAccessible(accessible);
        }
    }

    public void define(@NotNull String regex, @NotNull Object object) {
        CONSTANTS.put(regex, object);
    }

    private boolean isPrimitive(@NotNull Class<?> clazz) {
        return clazz.isPrimitive() || new ListOf<>(
            String.class,
            List.class
        ).stream().anyMatch(aClass -> aClass.isAssignableFrom(clazz));
    }

    private boolean isAllow(@NotNull Class<?> clazz) {
        return new ListOf<>(
            Replaceable.class,
            Sendable.class,
            SendableTitle.class,
            XMaterial.class,
            Object[].class,
            ItemStack.class
        ).stream().anyMatch(aClass -> aClass.isAssignableFrom(clazz));
    }

}
