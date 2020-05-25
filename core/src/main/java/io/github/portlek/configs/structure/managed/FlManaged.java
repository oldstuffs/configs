/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.github.portlek.configs.structure.managed;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.ConfigSerializable;
import io.github.portlek.configs.files.configuration.FileConfiguration;
import io.github.portlek.configs.processors.ConfigProceed;
import io.github.portlek.configs.provided.Provided;
import io.github.portlek.configs.provided.SerializableProvider;
import io.github.portlek.configs.structure.managed.section.CfgSection;
import io.github.portlek.configs.util.SpecialFunction;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public interface FlManaged extends CfgSection {

    Map<Class<?>, Provided<?>> PROVIDED = new ConcurrentHashMap<>();

    Map<Class<?>, SpecialFunction<?>> PROVIDED_GET = new ConcurrentHashMap<>();

    Map<Class<?>, Function<?, Object>> PROVIDED_SET = new ConcurrentHashMap<>();

    @NotNull
    static <T> Optional<Provided<T>> getProvidedClass(@NotNull final Class<T> aClass) {
        //noinspection unchecked
        return FlManaged.PROVIDED.keySet().stream()
            .filter(aClass::equals)
            .findFirst()
            .map(clss -> (Provided<T>) FlManaged.PROVIDED.get(clss));
    }

    static <T> void addProvidedGetMethod(@NotNull final Class<T> aClass,
                                         @NotNull final SpecialFunction<T> provide) {
        if (!FlManaged.PROVIDED_GET.containsKey(aClass)) {
            FlManaged.PROVIDED_GET.put(aClass, provide);
        }
    }

    @NotNull
    static <T> Optional<SpecialFunction<T>> getProvidedGetMethod(@NotNull final Class<T> aClass) {
        //noinspection unchecked
        return FlManaged.PROVIDED_GET.keySet().stream()
            .filter(aClass::equals)
            .findFirst()
            .map(clss -> (SpecialFunction<T>) FlManaged.PROVIDED_GET.get(clss));
    }

    static <T> void addProvidedSetMethod(@NotNull final Class<T> aClass, @NotNull final Function<T, Object> provide) {
        if (!FlManaged.PROVIDED_SET.containsKey(aClass)) {
            FlManaged.PROVIDED_SET.put(aClass, provide);
        }
    }

    @NotNull
    static <T> Optional<Function<T, Object>> getProvidedSetMethod(@NotNull final Class<T> aClass) {
        //noinspection unchecked
        return FlManaged.PROVIDED_SET.keySet().stream()
            .filter(aClass::equals)
            .findFirst()
            .map(clss -> (Function<T, Object>) FlManaged.PROVIDED_SET.get(clss));
    }

    static <T> void addSerializableClass(@NotNull final Class<T> aClass) {
        Optional.ofNullable(aClass.getDeclaredAnnotation(ConfigSerializable.class)).orElseThrow(() ->
            new UnsupportedOperationException(aClass.getSimpleName() + " has not `ConfigSerializable` annotation!"));
        final SerializableProvider<T> provided = new SerializableProvider<>(aClass);
        provided.initiate();
        FlManaged.addProvidedClass(aClass, provided);
    }

    static <T> void addProvidedClass(@NotNull final Class<T> aClass, @NotNull final Provided<T> provided) {
        if (!FlManaged.PROVIDED.containsKey(aClass)) {
            FlManaged.PROVIDED.put(aClass, provided);
        }
    }

    @Override
    FileConfiguration getConfigurationSection();

    @NotNull
    Optional<Object> pull(@NotNull String id);

    default void load() {
        this.onCreate();
        new ConfigProceed(
            Optional.ofNullable(this.getClass().getDeclaredAnnotation(Config.class)).orElseThrow(() ->
                new UnsupportedOperationException(this.getClass().getSimpleName() + " has not `Config` annotation!"))
        ).load(this);
        this.onLoad();
    }

    default void onCreate() {
    }

    default void onLoad() {
    }

    void setup(@NotNull File file, @NotNull FileConfiguration fileConfiguration);

    default void save() {
        this.getConfigurationSection().save(this.getFile());
    }

    @NotNull
    File getFile();

    void addObject(@NotNull String key, @NotNull Object object);

    boolean isAutoSave();

    void setAutoSave(boolean autosv);

    void autoSave();

}
