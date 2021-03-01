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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents config loaders.
 */
@RequiredArgsConstructor
public final class Configs {

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
   * the class to save.
   */
  @Nullable
  private final Config toSave;

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
   */
  public void load() {
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
     * the file path.
     */
    @Nullable
    private Path filePath;

    /**
     * the class to save.
     */
    @Nullable
    private Config toSave;

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
    public Configs build() {
      Validate.checkNull(this.configType, "Use #setConfigType(Class<ConfigType>) method to set config type.");
      Validate.checkNull(this.fileName, "Use #setFileName(String) method to set file name.");
      Validate.checkNull(this.filePath, "Use #setFilePath(Path) method to set file path.");
      return new Configs(this.configType, this.fileName, this.filePath, this.serializers, this.toSave);
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
     * sets the file path.
     *
     * @param file the file to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setFilePath(@NotNull final File file) {
      return this.setFilePath(file.toPath());
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
     * obtains the to save.
     *
     * @return to save.
     */
    @Nullable
    public Config getToSave() {
      return this.toSave;
    }

    /**
     * sets the to save.
     *
     * @param toSave the to save to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder setToSave(@NotNull final Config toSave) {
      this.toSave = toSave;
      return this;
    }
  }
}
