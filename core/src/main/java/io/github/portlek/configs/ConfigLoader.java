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

import io.github.portlek.configs.paths.Pth;
import io.github.portlek.reflection.clazz.ClassOf;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
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
   * the cache.
   */
  private final AtomicReference<Map<String, Object>> cache = new AtomicReference<>(new HashMap<>());

  /**
   * the config.
   */
  private final Config config = new Config(this);

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
   * the class path holder.
   */
  @NotNull
  private final PathHolder pathHolder;

  /**
   * the serializers.
   */
  @NotNull
  private final List<Serializer> serializers;

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
   * loads the config.
   *
   * @return loaded config.
   */
  @NotNull
  public Config load() {
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
  public Config load(final boolean save) {
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
  public CompletableFuture<Config> load(final boolean save, final boolean async) {
    final var filePath = this.folderPath.resolve(this.fileName + this.configType.getSuffix());
    this.file = filePath.toFile();
    if (Files.notExists(filePath)) {
      try {
        Files.createFile(filePath);
        this.configType.writeDefault(filePath.toFile());
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }
    if (Files.exists(filePath)) {
      try {
        if (Files.size(filePath) == 0) {
          this.configType.writeDefault(filePath.toFile());
        }
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }
    final var future = new CompletableFuture<Map<String, Object>>();
    if (async) {
      future.completeAsync(() -> {
        this.preLoad();
        return this.configType.load(filePath.toFile());
      });
    } else {
      this.preLoad();
      future.complete(this.configType.load(filePath.toFile()));
    }
    final var config = new CompletableFuture<Config>();
    future.whenComplete((map, t) -> {
      if (t != null) {
        t.printStackTrace();
        return;
      }
      this.postLoad(map);
      this.cache.set(map);
      if (save) {
        this.save();
      }
      config.complete(this.config);
    });
    return config;
  }

  /**
   * runs {@link PathHolder#postLoad()} method.
   *
   * @param cache the cache to load.
   */
  private void postLoad(@NotNull final Map<String, Object> cache) {
    this.config.getDefaults().forEach((s, o) -> {
      if (!cache.containsKey(s)) {
        cache.put(s, o);
      }
    });
    this.pathHolder.postLoad();
  }

  /**
   * loads fields in the {@link #pathHolder} class.
   */
  private void preLoad() {
    new ClassOf<>(this.pathHolder).getDeclaredFields().stream()
      .filter(refField -> Pth.class.isAssignableFrom(refField.getType()))
      .map(refField -> refField.of(this.pathHolder).getValue())
      .filter(Optional::isPresent)
      .map(Optional::get)
      .map(Pth.class::cast)
      .forEach(pth -> pth.setConfig(this.config));
    this.pathHolder.preLoad();
  }

  /**
   * runs when config is saving.
   */
  private void save() {
    Validate.checkNull(this.file, "Use #load() method before save the config!");
    this.pathHolder.preSave();
    this.configType.save(this.file, this.cache.get());
    this.pathHolder.postSave();
  }

  /**
   * a class that represents class loader builders.
   */
  public static final class Builder {

    /**
     * the serializers.
     */
    @NotNull
    private final List<Serializer> serializers = new ArrayList<>();

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
     * the path holder.
     */
    @NotNull
    private PathHolder pathHolder = PathHolder.empty();

    /**
     * ctor.
     */
    private Builder() {
    }

    /**
     * adds serializers.
     *
     * @param serializers the serializers to add.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder addSerializers(@NotNull final Serializer... serializers) {
      this.serializers.addAll(Arrays.asList(serializers));
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
      return new ConfigLoader(this.configType, this.fileName, this.folderPath, this.pathHolder, this.serializers);
    }

    /**
     * obtains the config type.
     *
     * @return config type.
     */
    @Nullable
    public ConfigType getConfigType() {
      return this.configType;
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
     * obtains the file name.
     *
     * @return file name.
     */
    @Nullable
    public String getFileName() {
      return this.fileName;
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
     * obtains the folder path.
     *
     * @return folder path.
     */
    @Nullable
    public Path getFolderPath() {
      return this.folderPath;
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
     * obtains the path holder.
     *
     * @return path holder.
     */
    @Nullable
    public PathHolder getPathHolder() {
      return this.pathHolder;
    }

    /**
     * sets the path holder.
     *
     * @param pathHolder the path holder to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setPathHolder(@NotNull final PathHolder pathHolder) {
      this.pathHolder = pathHolder;
      return this;
    }

    /**
     * obtains the serializers.
     *
     * @return serializers.
     */
    @NotNull
    public List<Serializer> getSerializers() {
      return Collections.unmodifiableList(this.serializers);
    }
  }
}
