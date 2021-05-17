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

package io.github.portlek.configs.loaders;

import io.github.portlek.configs.configuration.ConfigurationSection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an interface to determine section serializers.
 *
 * @param <R> type of the raw value.
 * @param <F> type of the final value.
 */
public interface SectionSerializer<R, F> {

  /**
   * obtains the raw config object.
   *
   * @param section the section to obtain.
   * @param path the path to obtain.
   *
   * @return obtained raw value from config.
   */
  @NotNull
  Optional<R> toConfigObject(@NotNull ConfigurationSection section, @NotNull String path);

  /**
   * converts the given raw value into {@link F}.
   *
   * @param rawValue the raw value to convert.
   * @param fieldValue the field value to convert.
   *
   * @return converted final value.
   */
  @NotNull
  default Optional<F> toFinal(@NotNull final R rawValue, @Nullable final F fieldValue) {
    return Optional.empty();
  }

  /**
   * converts the given final value into {@link R}.
   *
   * @param finalValue the final value to convert.
   *
   * @return converted raw value.
   */
  @NotNull
  default Optional<R> toRaw(@NotNull final F finalValue) {
    return Optional.empty();
  }

  /**
   * writes the given final value into the section.
   *
   * @param section the section to write.
   * @param finalValue the final value to write.
   * @param <D> type of the final value.
   */
  default <D extends DataSerializer> void toRaw(@NotNull final ConfigurationSection section,
                                                @NotNull final D finalValue) {
    finalValue.serialize(section);
  }
}
