/*
 * MIT License
 *
 * Copyright (c) 2021 Hasan Demirtaş
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

package io.github.portlek.configs.lang;

import io.github.portlek.configs.ConfigHolder;
import io.github.portlek.configs.ConfigLoader;
import io.github.portlek.configs.ConfigType;
import io.github.portlek.configs.FieldLoader;
import io.github.portlek.configs.Loader;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents language file loaders.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class LangLoader implements Loader {

  /**
   * the config builders.
   */
  @NotNull
  private final Map<String, ConfigLoader.Builder> builders;

  /**
   * the config holder.
   */
  @Nullable
  private final ConfigHolder configHolder;

  /**
   * the keys.
   */
  @Nullable
  private List<String> keys;

  /**
   * the values.
   */
  @Nullable
  private List<ConfigLoader.Builder> values;

  /**
   * creates a new {@link Builder} instance.
   *
   * @return a newly created builder instance.
   */
  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  /**
   * obtains the default language key.
   *
   * @return default language key.
   */
  @NotNull
  public Optional<String> getDefaultLanguage() {
    return this.getKeys().stream().findFirst();
  }

  /**
   * obtains the lang keys.
   *
   * @return lang keys.
   */
  @NotNull
  public List<String> getKeys() {
    if (this.keys == null) {
      this.keys = List.copyOf(this.builders.keySet());
    }
    return Collections.unmodifiableList(this.keys);
  }

  /**
   * obtains the lang values.
   *
   * @return lang values.
   */
  @NotNull
  public List<ConfigLoader.Builder> getValues() {
    if (this.values == null) {
      this.values = List.copyOf(this.builders.values());
    }
    return Collections.unmodifiableList(this.values);
  }

  /**
   * loads the config.
   *
   * @return loaded config.
   */
  @NotNull
  public LangLoader load() {
    return this.load(false);
  }

  /**
   * loads the config.
   *
   * @param save the save to load.
   *
   * @return loaded config.
   */
  @NotNull
  public LangLoader load(final boolean save) {
    return this.load(save, false).join();
  }

  /**
   * loads the config.
   *
   * @param save the save to load.
   * @param async the async to load.
   *
   * @return loaded config.
   */
  @NotNull
  public CompletableFuture<LangLoader> load(final boolean save, final boolean async) {
    final var future = new CompletableFuture<LangLoader>();
    final var values = this.getValues();
    final var job = (Function<ConfigLoader, Runnable>) loader -> () -> {
      loader.createFolderAndFile();
      loader.loadFile();
      this.loadFieldsAndSave(loader, save);
    };
    if (async) {
      for (final var value : values) {
        final var loader = value.build();
        future.thenRunAsync(job.apply(loader), loader.getAsyncExecutor());
      }
    } else {
      for (final var value : values) {
        final var build = value.build();
        final var runnable = job.apply(build);
        future.thenRun(runnable);
      }
    }
    return future.thenApply(langLoader -> langLoader);
  }

  /**
   * loads fields in the {@link #configHolder} then saves if {@code save} is true.
   *
   * @param loader the loader to load.
   * @param save the save to load.
   */
  public void loadFieldsAndSave(@NotNull final ConfigLoader loader, final boolean save) {
    if (this.configHolder != null) {
      FieldLoader.load(this, this.configHolder, loader.getLoaders());
    }
    if (save) {
      loader.save();
    }
  }

  /**
   * a class that represents class loader builders.
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @Getter
  public static final class Builder {

    /**
     * the config builders.
     */
    @NotNull
    private Map<String, ConfigLoader.Builder> builders = new HashMap<>();

    /**
     * the holder.
     */
    @Nullable
    private ConfigHolder holder;

    /**
     * adds the key with builder.
     *
     * @param builders the builders to add.
     *
     * @return {@code this} for builder chain.
     */
    @SafeVarargs
    @NotNull
    public final Builder addBuilder(@NotNull final Map.Entry<String, ConfigLoader.Builder>... builders) {
      Arrays.stream(builders)
        .forEach(builder -> this.builders.put(builder.getKey(), builder.getValue()));
      return this;
    }

    /**
     * sets the builder.
     *
     * @param builders the builders to set.
     *
     * @return {@code this} for builder chain.
     */
    @SafeVarargs
    @NotNull
    public final Builder setBuilders(@NotNull final Map.Entry<String, ConfigLoader.Builder>... builders) {
      this.builders = Arrays.stream(builders)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, HashMap::new));
      return this;
    }

    /**
     * adds a builder from the given file name as key and with other parameters.
     *
     * @param fileName the file name to add.
     * @param folder the folder to add.
     * @param configType the config type to add.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder addBuilder(@NotNull final String fileName, @NotNull final Path folder,
                              @NotNull final ConfigType configType) {
      this.builders.put(fileName, ConfigLoader.builderForLang(fileName, folder, configType));
      return this;
    }

    /**
     * adds a builder from the given key and with other parameters.
     *
     * @param key the key to add.
     * @param fileName the file name to add.
     * @param folder the folder to add.
     * @param configType the config type to add.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder addBuilder(@NotNull final String key, @NotNull final String fileName, @NotNull final Path folder,
                              @NotNull final ConfigType configType) {
      this.builders.put(key, ConfigLoader.builderForLang(fileName, folder, configType));
      return this;
    }

    /**
     * adds a builder from the given file name as key and with other parameters.
     *
     * @param fileName the file name to add.
     * @param folder the folder to add.
     * @param configType the config type to add.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder addBuilder(@NotNull final String fileName, @NotNull final File folder,
                              @NotNull final ConfigType configType) {
      this.builders.put(fileName, ConfigLoader.builderForLang(fileName, folder, configType));
      return this;
    }

    /**
     * adds a builder from the given key and with other parameters.
     *
     * @param key the key to add.
     * @param fileName the file name to add.
     * @param folder the folder to add.
     * @param configType the config type to add.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder addBuilder(@NotNull final String key, @NotNull final String fileName, @NotNull final File folder,
                              @NotNull final ConfigType configType) {
      this.builders.put(key, ConfigLoader.builderForLang(fileName, folder, configType));
      return this;
    }

    /**
     * adds the builder with its file name as key.
     *
     * @param builder the builder to add.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder addBuilder(@NotNull final ConfigLoader.Builder builder) {
      final var key = builder.getFileName();
      if (key == null) {
        return this;
      }
      this.builders.put(key, builder);
      return this;
    }

    /**
     * adds the key with builder.
     *
     * @param key the key to add.
     * @param builder the builder to add.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder addBuilder(@NotNull final String key, @NotNull final ConfigLoader.Builder builder) {
      this.builders.put(key, builder);
      return this;
    }

    /**
     * adds the key with builder.
     *
     * @param builders the builders to add.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder addBuilder(@NotNull final Map<String, ConfigLoader.Builder> builders) {
      this.builders.putAll(builders);
      return this;
    }

    /**
     * builds and create a new {@link LangLoader} instance.
     *
     * @return a newly created lang loader instance.
     */
    @NotNull
    public LangLoader build() {
      if (this.builders.isEmpty()) {
        throw new IllegalStateException("#builders is empty");
      }
      return new LangLoader(this.builders, this.holder);
    }

    /**
     * sets the builder.
     *
     * @param builders the builders to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setBuilders(@NotNull final Map<String, ConfigLoader.Builder> builders) {
      this.builders = builders;
      return this;
    }

    /**
     * sets the config holder.
     *
     * @param holder the holder to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setHolder(@NotNull final ConfigHolder holder) {
      this.holder = holder;
      return this;
    }
  }
}
