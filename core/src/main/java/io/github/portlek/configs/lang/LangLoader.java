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

import io.github.portlek.configs.ConfigLoader;
import io.github.portlek.configs.ConfigType;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents language file loaders.
 */
@RequiredArgsConstructor
public final class LangLoader {

  /**
   * the config builders.
   */
  @NotNull
  private final Map<String, ConfigLoader.Builder> builders;

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
      this.keys = new ArrayList<>(this.builders.keySet());
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
      this.values = new ArrayList<>(this.builders.values());
    }
    return Collections.unmodifiableList(this.values);
  }

  /**
   * a class that represents class loader builders.
   */
  public static final class Builder {

    /**
     * the config builders.
     */
    @NotNull
    private Map<String, ConfigLoader.Builder> builders = new HashMap<>();

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
      return this.addBuilder(fileName, fileName, folder, configType);
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
      return this.addBuilder(key, ConfigLoader.builder(fileName, folder, configType));
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
      return this.addBuilder(fileName, fileName, folder, configType);
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
      return this.addBuilder(key, fileName, folder.toPath(), configType);
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
      return this.addBuilder(key, builder);
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
  }
}
