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

package io.github.portlek.configs;

import java.io.File;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents language file loaders.
 */
@Getter
public final class LangLoader implements Loader {

  /**
   * the async executor.
   */
  @NotNull
  public final Executor asyncExecutor;

  /**
   * the config builders.
   */
  @NotNull
  private final Map<String, ConfigLoader.Builder> builders;

  /**
   * the built config loaders.
   */
  private final StringLoaderEntry[] built;

  /**
   * the config holder.
   */
  @Nullable
  private final ConfigHolder configHolder;

  /**
   * the current statement.
   */
  private final AtomicInteger statement = new AtomicInteger();

  /**
   * the default language.
   */
  @Nullable
  private String defaultLanguage;

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
   * ctor.
   *
   * @param asyncExecutor the async executor.
   * @param builders the builders.
   * @param configHolder the config holder.
   */
  private LangLoader(@NotNull final Executor asyncExecutor, @NotNull final Map<String, ConfigLoader.Builder> builders,
                     @Nullable final ConfigHolder configHolder) {
    this.asyncExecutor = asyncExecutor;
    this.builders = builders;
    this.configHolder = configHolder;
    this.built = new StringLoaderEntry[this.builders.size()];
  }

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
   * obtains the {@link #built}.
   *
   * @return built.
   */
  @NotNull
  public StringLoaderEntry[] getBuilt() {
    synchronized (this.built) {
      if (this.built.length == 0) {
        throw new IllegalStateException("Couldn't find any built config loader instance.");
      }
      if (this.built[0] == null) {
        final var list = this.builders.entrySet().stream()
          .map(entry -> new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), entry.getValue().build()))
          .collect(Collectors.toList());
        IntStream.range(0, list.size())
          .forEach(index -> this.built[index] = new StringLoaderEntry(list.get(index)));
      }
      return this.built.clone();
    }
  }

  /**
   * obtains the default language key.
   *
   * @return default language key.
   */
  @NotNull
  public Optional<String> getDefaultLanguage() {
    if (this.defaultLanguage == null) {
      this.defaultLanguage = this.getKeys().stream().findFirst().orElse(null);
    }
    return Optional.ofNullable(this.defaultLanguage);
  }

  @Override
  public int getFileVersion() {
    return this.pollConfigLoader().getValue().getFileVersion();
  }

  @NotNull
  @Override
  public Map<Integer, Runnable> getFileVersionOperations() {
    return this.pollConfigLoader().getValue().getFileVersionOperations();
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
    final var job = (Function<ConfigLoader, Runnable>) loader -> () -> {
      loader.createFolderAndFile();
      loader.loadFile();
      this.loadFieldsAndSave(loader, save);
    };
    Arrays.stream(this.getBuilt())
      .map(StringLoaderEntry::getValue)
      .forEach(built -> {
        if (async) {
          future.thenRunAsync(job.apply(built), built.getAsyncExecutor());
        } else {
          future.thenRun(job.apply(built));
        }
      });
    if (async) {
      future.completeAsync(() -> this, this.asyncExecutor);
    } else {
      future.complete(this);
    }
    return future;
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
   * polls the current config loader.
   *
   * @return config loader.
   */
  @NotNull
  public Map.Entry<String, ConfigLoader> pollConfigLoader() {
    final var built = this.getBuilt();
    if (built.length == 0) {
      throw new IllegalStateException("Couldn't find any built config loader.");
    }
    return built[this.pollStatement()];
  }

  /**
   * loads fields in the {@link #configHolder} then saves if {@code save} is true.
   *
   * @param configLoader the config loader to load.
   * @param save the save to load.
   */
  private void loadFieldsAndSave(@NotNull final ConfigLoader configLoader, final boolean save) {
    if (this.configHolder != null) {
      FieldLoader.load(this, this.configHolder, configLoader.getLoaders());
    }
    if (save) {
      configLoader.save();
    }
  }

  /**
   * polls and increase {@link #statement} by 1.
   *
   * @return the current statement.
   */
  private int pollStatement() {
    if (this.statement.get() > this.getKeys().size()) {
      this.statement.set(0);
    }
    return this.statement.getAndIncrement();
  }

  /**
   * a class that represents class loader builders.
   */
  @Getter
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Builder {

    /**
     * the async executor.
     */
    @NotNull
    private Executor asyncExecutor = Executors.newWorkStealingPool();

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
      if (this.holder != null) {
        this.builders.values().forEach(builder ->
          builder.setConfigHolder(this.holder));
      }
      return new LangLoader(this.asyncExecutor, this.builders, this.holder);
    }

    /**
     * sets the async executor type.
     *
     * @param asyncExecutor the async executor to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setAsyncExecutor(@NotNull final Executor asyncExecutor) {
      this.asyncExecutor = asyncExecutor;
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

  /**
   * a string and config loader map entry to avoid generic types.
   */
  @RequiredArgsConstructor
  public static final class StringLoaderEntry implements Map.Entry<String, ConfigLoader> {

    /**
     * the original entry to delegate.
     */
    @NotNull
    @Delegate
    private final Map.Entry<String, ConfigLoader> original;
  }
}
