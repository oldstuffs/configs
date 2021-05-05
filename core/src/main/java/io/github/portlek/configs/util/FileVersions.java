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
import java.util.stream.IntStream;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * a class that contains utility methods to cover file version operations.
 */
@UtilityClass
public class FileVersions {

  /**
   * loads the field.
   *
   * @param fieldLoader the field loader to check.
   * @param loader the loader to check.
   * @param field the field to check.
   */
  public void onLoad(@NotNull final FieldLoader fieldLoader, @NotNull final Loader loader,
                     @NotNull final RefField field) {
    final var fromOptional = field.getAnnotation(From.class);
    if (fromOptional.isEmpty()) {
      return;
    }
    final var from = fromOptional.get();
    final var changedVersion = from.changedVersion();
    final var version = from.version();
    final var remove = from.remove();
    IntStream.range(1, loader.getFileVersion()).forEach(value -> {
    });
  }
}
