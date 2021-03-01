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

package io.github.portlek.configs.managed;

import io.github.portlek.configs.FileType;
import io.github.portlek.configs.FlManaged;
import io.github.portlek.configs.section.ConfigSection;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

public class FileManaged extends ConfigSection implements FlManaged {

  private final Map<String, Object> objects = new HashMap<>();

  private boolean autoSave = false;

  @Nullable
  private File file;

  @Nullable
  private FileType fileType;

  @SafeVarargs
  public FileManaged(@NotNull final Map.Entry<String, Object>... objects) {
    Arrays.asList(objects).forEach(entry ->
      this.object(entry.getKey(), entry.getValue()));
  }

  @Override
  public final void autoSave() {
    if (this.autoSave) {
      this.save();
    }
  }

  @NotNull
  @Override
  public File getFile() {
    return Objects.requireNonNull(this.file, "You have to load your class with '#load()' method");
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return Objects.requireNonNull(this.fileType, "You have to load your class with '#load()' method");
  }

  @Override
  public void setFileType(@NotNull final FileType fileType) {
    this.fileType = fileType;
  }

  @Override
  public final boolean isAutoSave() {
    return this.autoSave;
  }

  @Override
  public final void setAutoSave(final boolean autoSave) {
    this.autoSave = autoSave;
  }

  @Override
  public final void object(@NotNull final String key, @NotNull final Object object) {
    this.objects.put(key, object);
  }

  @NotNull
  @Override
  public final Optional<Object> object(@NotNull final String id) {
    return Optional.ofNullable(this.objects.get(id));
  }

  @NotNull
  @Override
  public final Collection<Object> objects() {
    return this.objects.values();
  }

  @Override
  public void setup(@NotNull final File file, final @NotNull FileType fileType) throws Exception {
    this.setup(this, fileType.load(file));
    this.file = file;
    this.fileType = fileType;
  }

  @NotNull
  @Override
  public FileConfiguration getConfigurationSection() {
    return (FileConfiguration) super.getConfigurationSection();
  }
}
