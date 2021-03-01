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

package io.github.portlek.configs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents config loaders.
 */
public final class ConfigLoader {

  /**
   * the class to save.
   */
  @Nullable
  private final Class<?> classToSave;

  /**
   * the config type class.
   */
  @NotNull
  private final Class<ConfigType> configTypeClass;

  /**
   * the file name.
   */
  @NotNull
  private final String fileName;

  /**
   * the file path.
   */
  @NotNull
  private final Path filePath;

  /**
   * the serializers.
   */
  @NotNull
  private final List<Serializer> serializers;

  /**
   * ctor.
   *
   * @param configTypeClass the config type class.
   * @param fileName the file name.
   * @param filePath the file path.
   */
  private ConfigLoader(@NotNull final Class<ConfigType> configTypeClass, @NotNull final String fileName,
                       @NotNull final Path filePath, @Nullable final Class<?> classToSave,
                       @NotNull final List<Serializer> serializers) {
    this.configTypeClass = configTypeClass;
    this.fileName = fileName;
    this.filePath = filePath;
    this.classToSave = classToSave;
    this.serializers = serializers;
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
   * loads the config.
   *
   * @return loaded config instance.
   */
  @NotNull
  public Config load() {
    return new Config() {
    };
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
     * the config type class.
     */
    @Nullable
    private Class<ConfigType> configType;

    /**
     * the file name.
     */
    @Nullable
    private String fileName;

    /**
     * the file path.
     */
    @Nullable
    private Path filePath;

    /**
     * the class to save.
     */
    @Nullable
    private Class<?> toSave;

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
      Validate.checkNull(this.configType, "Use #setConfigClassType(Class<ConfigType>) method to set config type class");
      Validate.checkNull(this.fileName, "Use #setFileName(String) method to set file name.");
      Validate.checkNull(this.filePath, "Use #setFilePath(Path) method to set file path.");
      return new ConfigLoader(this.configType, this.fileName, this.filePath, this.toSave, this.serializers);
    }

    /**
     * obtains the config type class.
     *
     * @return config type class.
     */
    @Nullable
    public Class<ConfigType> getConfigType() {
      return this.configType;
    }

    /**
     * sets the config type class.
     *
     * @param configType the config type class to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setConfigType(@NotNull final Class<ConfigType> configType) {
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
     * obtains the file path.
     *
     * @return file path.
     */
    @Nullable
    public Path getFilePath() {
      return this.filePath;
    }

    /**
     * sets the file path.
     *
     * @param filePath the file path to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setFilePath(@NotNull final Path filePath) {
      this.filePath = filePath;
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

    /**
     * obtains the class to save.
     *
     * @return class to save.
     */
    @Nullable
    public Class<?> getToSave() {
      return this.toSave;
    }

    /**
     * sets the class to save.
     *
     * @param toSave the class to save to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setToSave(@NotNull final Class<?> toSave) {
      this.toSave = toSave;
      return this;
    }
  }
}
