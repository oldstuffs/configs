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

package io.github.portlek.configs.util;

import io.github.portlek.configs.FieldLoader;
import io.github.portlek.configs.Loader;
import io.github.portlek.reflection.RefField;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * a class that contains utility methods to cover file version operations.
 */
@UtilityClass
public class FileVersions {

  /**
   * runs {@link Loader#getFileVersionOperations()}.
   *
   * @param loader the loader to run.
   */
  public void onLoad(@NotNull final Loader loader) {
    final var actualVersion = Math.min(
      loader.getFileVersion(),
      Math.max(1, loader.getFileConfiguration().getInt("file-version", 1)));
    Optional.ofNullable(loader.getFileVersionOperations().get(actualVersion))
      .ifPresent(Runnable::run);
  }

  /**
   * loads the field.
   *
   * @param loader the loader to load.
   * @param field the field to load.
   * @param fieldLoader the field loader to load.
   */
  public void onLoadField(@NotNull final Loader loader, @NotNull final RefField field,
                          @NotNull final FieldLoader fieldLoader) {
    fieldLoader.onLoad(loader, field);
  }

  /**
   * increases the file version of the loader.
   *
   * @param loader the loader to increase.
   */
  public void onUpdate(@NotNull final Loader loader) {
    final var configuration = loader.getFileConfiguration();
    final var fileVersion = configuration.getInt("file-version", 1);
    final var actualVersion = Math.min(loader.getFileVersion(), Math.max(1, fileVersion));
    final var newVersion = actualVersion != fileVersion && loader.getFileVersion() > actualVersion
      ? actualVersion + 1
      : actualVersion;
    configuration.set("file-version", newVersion);
  }
}
