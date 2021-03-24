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

import io.github.portlek.configs.configuration.FileConfiguration;
import io.github.portlek.configs.exceptions.InvalidConfigurationException;
import io.github.portlek.configs.loaders.FlConfigHolder;
import io.github.portlek.configs.loaders.FlConfigLoader;
import io.github.portlek.configs.loaders.FlConfiguration;
import io.github.portlek.configs.loaders.FlConfigurationSection;
import io.github.portlek.configs.loaders.FlFile;
import io.github.portlek.configs.loaders.FlLangLoader;
import io.github.portlek.configs.loaders.FlLangValueLoader;
import io.github.portlek.configs.loaders.FlLocale;
import io.github.portlek.configs.loaders.FlRawField;
import io.github.portlek.configs.loaders.FlUniqueId;
import io.github.portlek.configs.util.Validate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents config loaders.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class ConfigLoader implements Loader {

  /**
   * the async executor.
   */
  @NotNull
  public final Executor asyncExecutor;

  /**
   * the class config holder.
   */
  @Nullable
  private final ConfigHolder configHolder;

  /**
   * the config type.
   */
  @NotNull
  private final ConfigType configType;

  /**
   * the file name.
   */
  @NotNull
  private final String fileName;

  /**
   * the folder path.
   */
  @NotNull
  private final Path folderPath;

  /**
   * the loaders.
   */
  @NotNull
  private final List<Supplier<? extends FieldLoader>> loaders;

  /**
   * the configuration.
   */
  @Nullable
  private FileConfiguration configuration;

  /**
   * the file.
   */
  @Nullable
  private File file;

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
   * creates a new {@link Builder} instance.
   *
   * @param fileName the file name to create.
   * @param folder the folder to create.
   * @param configType the config type to create.
   *
   * @return a newly created builder instance.
   */
  @NotNull
  public static Builder builder(@NotNull final String fileName, @NotNull final File folder,
                                @NotNull final ConfigType configType) {
    return ConfigLoader.builder(fileName, folder.toPath(), configType);
  }

  /**
   * creates a new {@link Builder} instance.
   *
   * @param fileName the file name to create.
   * @param folder the folder to create.
   * @param configType the config type to create.
   *
   * @return a newly created builder instance.
   */
  @NotNull
  public static Builder builder(@NotNull final String fileName, @NotNull final Path folder,
                                @NotNull final ConfigType configType) {
    return ConfigLoader.builder()
      .setFileName(fileName)
      .setFolder(folder)
      .setConfigType(configType);
  }

  /**
   * creates a new {@link Builder} instance.
   *
   * @param fileName the file name to create.
   * @param folder the folder to create.
   * @param configType the config type to create.
   *
   * @return a newly created builder instance.
   */
  @NotNull
  public static Builder builderForLang(@NotNull final String fileName, @NotNull final File folder,
                                       @NotNull final ConfigType configType) {
    return ConfigLoader.builderForLang(fileName, folder.toPath(), configType);
  }

  /**
   * creates a new {@link Builder} instance.
   *
   * @param fileName the file name to create.
   * @param folder the folder to create.
   * @param configType the config type to create.
   *
   * @return a newly created builder instance.
   */
  @NotNull
  public static Builder builderForLang(@NotNull final String fileName, @NotNull final Path folder,
                                       @NotNull final ConfigType configType) {
    return ConfigLoader.builder()
      .setFileName(fileName)
      .setFolder(folder)
      .setConfigType(configType)
      .setLoaders(List.of(
        FlConfigurationSection.INSTANCE,
        FlLangValueLoader.INSTANCE,
        FlConfiguration.INSTANCE,
        FlConfigHolder.INSTANCE,
        FlLangLoader.INSTANCE,
        FlFile.INSTANCE
      ));
  }

  /**
   * creates the folder and file then sets {@link #file}.
   */
  public void createFolderAndFile() {
    final var filePath = this.folderPath.resolve(this.fileName + this.configType.getSuffix());
    this.file = filePath.toFile();
    if (!Files.notExists(filePath)) {
      return;
    }
    try {
      Files.createFile(filePath);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * obtains the file.
   *
   * @return file.
   */
  @NotNull
  @Override
  public File getFile() {
    return Objects.requireNonNull(this.file, "Use #load() method before use #getFile() method!");
  }

  /**
   * obtains the configuration.
   *
   * @return configuration.
   */
  @NotNull
  @Override
  public FileConfiguration getFileConfiguration() {
    return Objects.requireNonNull(this.configuration, "Use #load() method before use #getConfiguration() method!");
  }

  /**
   * loads the config.
   *
   * @return loaded config.
   */
  @NotNull
  public ConfigLoader load() {
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
  public ConfigLoader load(final boolean save) {
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
  public CompletableFuture<ConfigLoader> load(final boolean save, final boolean async) {
    final var future = new CompletableFuture<ConfigLoader>();
    final var job = (Supplier<ConfigLoader>) () -> {
      this.createFolderAndFile();
      this.loadFile();
      this.loadFieldsAndSave(save);
      return this;
    };
    if (async) {
      future.completeAsync(job, this.asyncExecutor);
    } else {
      future.complete(job.get());
    }
    return future;
  }

  /**
   * loads fields in the {@link #configHolder} then saves if {@code save} is true.
   *
   * @param save the save to load.
   */
  public void loadFieldsAndSave(final boolean save) {
    if (this.configHolder != null) {
      FieldLoader.load(this, this.configHolder);
    }
    if (save) {
      this.save();
    }
  }

  /**
   * loads the file configuration from {@link #file}.
   */
  public void loadFile() {
    Validate.checkNull(this.file, "file");
    try {
      this.configuration = this.configType.load(this.file);
    } catch (final IOException | InvalidConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * runs when config is saving.
   */
  public void save() {
    try {
      this.configType.save(this.getFile(), this.getFileConfiguration());
    } catch (final IOException e) {
      e.printStackTrace();
    }
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
    private Executor asyncExecutor = Executors.newSingleThreadExecutor();

    /**
     * the config holder.
     */
    @Nullable
    private ConfigHolder configHolder;

    /**
     * the config type.
     */
    @Nullable
    private ConfigType configType;

    /**
     * the file name.
     */
    @Nullable
    private String fileName;

    /**
     * the folder path.
     */
    @Nullable
    private Path folderPath;

    /**
     * the loaders.
     */
    @NotNull
    private List<Supplier<? extends FieldLoader>> loaders = new ArrayList<>() {{
      this.add(FlConfigurationSection.INSTANCE);
      this.add(FlConfiguration.INSTANCE);
      this.add(FlConfigHolder.INSTANCE);
      this.add(FlConfigLoader.INSTANCE);
      this.add(FlRawField.INSTANCE);
      this.add(FlUniqueId.INSTANCE);
      this.add(FlLocale.INSTANCE);
      this.add(FlFile.INSTANCE);
    }};

    /**
     * adds loaders.
     *
     * @param loaders the loaders to add.
     *
     * @return {@code this} for builder chain.
     */
    @SafeVarargs
    @NotNull
    public final Builder addLoaders(@NotNull final Supplier<? extends FieldLoader>... loaders) {
      this.loaders.addAll(Arrays.asList(loaders));
      return this;
    }

    /**
     * builds the config loader from the builder values.
     *
     * @return a newly built config loader.
     */
    @NotNull
    public ConfigLoader build() {
      Validate.checkNull(this.configType, "Use #setConfigType(ConfigType) method to set config type!");
      Validate.checkNull(this.fileName, "Use #setFileName(String) method to set file name!");
      Validate.checkNull(this.folderPath, "Use #setFolderPath(Path) method to set file path!");
      return new ConfigLoader(this.asyncExecutor, this.configHolder, this.configType, this.fileName, this.folderPath,
        this.loaders);
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
     * sets the config holder.
     *
     * @param configHolder the config holder to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setConfigHolder(@NotNull final ConfigHolder configHolder) {
      this.configHolder = configHolder;
      return this;
    }

    /**
     * sets the config type.
     *
     * @param configType the config type to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setConfigType(@NotNull final ConfigType configType) {
      this.configType = configType;
      return this;
    }

    /**
     * sets the file name.
     *
     * @param fileName file name to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setFileName(@NotNull final String fileName) {
      this.fileName = fileName;
      return this;
    }

    /**
     * sets the folder path.
     *
     * @param folder the folder to set.
     *
     * @return {@code this} for builder chain.
     *
     * @see #setFolder(Path)
     */
    @NotNull
    public Builder setFolder(@NotNull final File folder) {
      this.folderPath = folder.toPath();
      return this;
    }

    /**
     * sets the folder path.
     *
     * @param folderPath the folder path to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setFolder(@NotNull final Path folderPath) {
      this.folderPath = folderPath;
      return this;
    }

    /**
     * sets {@link #loaders}.
     *
     * @param loaders the loaders to sets.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setLoaders(@NotNull final List<Supplier<? extends FieldLoader>> loaders) {
      this.loaders = loaders;
      return this;
    }
  }
}
