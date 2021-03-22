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
import io.github.portlek.configs.loaders.FieldLoader;
import io.github.portlek.configs.loaders.FlConfigHolder;
import io.github.portlek.configs.loaders.FlConfigLoader;
import io.github.portlek.configs.loaders.FlConfiguration;
import io.github.portlek.configs.loaders.FlConfigurationSection;
import io.github.portlek.configs.loaders.FlFile;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents config loaders.
 */
@RequiredArgsConstructor
@Getter
public final class ConfigLoader {

  /**
   * the async executor.
   */
  @NotNull
  public final Executor asyncExecutor;

  /**
   * the class config holder.
   */
  @Nullable
  private final Class<? extends ConfigHolder> configHolder;

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
   * obtains the configuration.
   *
   * @return configuration.
   */
  @NotNull
  public FileConfiguration getConfiguration() {
    return Objects.requireNonNull(this.configuration, "Use #load() method before save the config!");
  }

  /**
   * obtains the file.
   *
   * @return file.
   */
  @NotNull
  public File getFile() {
    return Objects.requireNonNull(this.file, "Use #load() method before save the config!");
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
  public CompletableFuture<FileConfiguration> load(final boolean save, final boolean async) {
    final var filePath = this.folderPath.resolve(this.fileName + this.configType.getSuffix());
    this.file = filePath.toFile();
    if (Files.notExists(filePath)) {
      try {
        Files.createFile(filePath);
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }
    final var future = new CompletableFuture<FileConfiguration>();
    if (async) {
      future.completeAsync(() -> {
        try {
          return this.configType.load(filePath.toFile());
        } catch (final IOException | InvalidConfigurationException e) {
          throw new RuntimeException(e);
        }
      }, this.asyncExecutor);
    } else {
      try {
        future.complete(this.configType.load(filePath.toFile()));
      } catch (final IOException | InvalidConfigurationException e) {
        throw new RuntimeException(e);
      }
    }
    future.whenComplete((configuration, t) -> {
      if (t != null) {
        t.printStackTrace();
        return;
      }
      this.configuration = configuration;
      this.load0();
      if (save) {
        try {
          this.save();
        } catch (final IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
    return future;
  }

  /**
   * loads the config.
   *
   * @return loaded config.
   */
  @NotNull
  public FileConfiguration load() {
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
  public FileConfiguration load(final boolean save) {
    return this.load(save, false).join();
  }

  /**
   * runs when config is saving.
   *
   * @throws IOException if something goes wrong when saving the file.
   */
  public void save() throws IOException {
    this.configType.save(this.getFile(), this.getConfiguration());
  }

  /**
   * loads fields in the {@link #configHolder} class.
   */
  private void load0() {
    if (this.configHolder != null) {
      FieldLoader.load(this, this.configHolder, this.loaders);
    }
  }

  /**
   * a class that represents class loader builders.
   */
  @Getter
  public static final class Builder {

    /**
     * the loaders.
     */
    @NotNull
    private final List<Supplier<? extends FieldLoader>> loaders = new ArrayList<>() {{
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
     * the async executor.
     */
    @NotNull
    private Executor asyncExecutor = Executors.newSingleThreadExecutor();

    /**
     * the config holder.
     */
    @Nullable
    private Class<? extends ConfigHolder> configHolder;

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
     * ctor.
     */
    private Builder() {
    }

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
    public Builder setConfigHolder(@NotNull final Class<? extends ConfigHolder> configHolder) {
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
     * sets the file path.
     *
     * @param file the file to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setFolderPath(@NotNull final File file) {
      return this.setFolderPath(file.toPath());
    }

    /**
     * sets the folder path.
     *
     * @param folderPath the folder path to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setFolderPath(@NotNull final Path folderPath) {
      this.folderPath = folderPath;
      return this;
    }
  }
}
