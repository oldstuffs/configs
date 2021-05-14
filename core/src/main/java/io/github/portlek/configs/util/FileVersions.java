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
import io.github.portlek.configs.annotation.From;
import io.github.portlek.reflection.RefField;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * a class that contains utility methods to cover file version operations.
 */
@UtilityClass
public class FileVersions {

  /**
   * loads the fields.
   *
   * @param loader the loader to load.
   * @param fields the fields to load.
   */
  public void load(@NotNull final Loader loader,
                   @NotNull final Collection<Map.Entry<RefField, FieldLoader>> fields) {
    IntStream.range(0, loader.getFileVersion()).forEach(index -> {
      FileVersions.onLoad(loader, index);
      fields.forEach(entry -> FileVersions.onLoadField(loader, entry.getKey(), entry.getValue()));
      FileVersions.onUpdate(loader);
    });
  }

  /**
   * runs {@link Loader#getFileVersionOperations()}.
   *
   * @param loader the loader to run.
   * @param fileVersionIndex the file version index to run.
   */
  private void onLoad(@NotNull final Loader loader, final int fileVersionIndex) {
    Optional.ofNullable(loader.getFileVersionOperations().get(fileVersionIndex))
      .ifPresent(Runnable::run);
  }

  /**
   * loads the field.
   *
   * @param loader the loader to load.
   * @param field the field to load.
   * @param fieldLoader the field loader to load.
   */
  private void onLoadField(@NotNull final Loader loader, @NotNull final RefField field,
                           @NotNull final FieldLoader fieldLoader) {
    final var fileVersion = loader.getFileConfiguration().getInt("file-version", 1);
    field.getAnnotation(From.class, from -> {
      final var createdVersion = from.createdVersion();
      if (createdVersion == fileVersion) {
        fieldLoader.onLoad(loader, field);
      }
    });
  }

  /**
   * increases the file version of the loader.
   *
   * @param loader the loader to increase.
   */
  private void onUpdate(@NotNull final Loader loader) {
    final var configuration = loader.getFileConfiguration();
    final var latestVersion = loader.getFileVersion();
    final var fileVersion = configuration.getInt("file-version", 1);
    if (fileVersion < 1) {
      configuration.set("file-version", 1);
    } else if (fileVersion > latestVersion) {
      configuration.set("file-version", latestVersion);
    }
    final var actualVersion = configuration.getInt("file-version", 1);
    if (latestVersion > actualVersion) {
      configuration.set("file-version", actualVersion + 1);
    }
  }
}
