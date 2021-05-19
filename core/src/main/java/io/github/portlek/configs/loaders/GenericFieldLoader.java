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

import io.github.portlek.configs.ConfigHolder;
import io.github.portlek.configs.Loader;
import io.github.portlek.configs.annotation.Route;
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.reflection.RefField;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an abstract class to load fields which have to write in a {@link ConfigurationSection}.
 *
 * @param <R> type of the raw value.
 * @param <F> type of the final value.
 */
public abstract class GenericFieldLoader<R, F> extends BaseFieldLoader implements SectionSerializer<R, F> {

  /**
   * the final class.
   */
  @NotNull
  private final Class<F> finalClass;

  /**
   * ctor.
   *
   * @param holder the holder.
   * @param section the section.
   * @param finalClass final class.
   */
  protected GenericFieldLoader(@NotNull final ConfigHolder holder, @NotNull final ConfigurationSection section,
                               @NotNull final Class<F> finalClass) {
    super(holder, section);
    this.finalClass = finalClass;
  }

  @Override
  public boolean canLoad(@NotNull final Loader loader, @NotNull final RefField field) {
    return this.finalClass.isAssignableFrom(field.getType());
  }

  @Override
  public final void onLoad(@NotNull final Loader loader, @NotNull final RefField field) {
    final var path = field.getAnnotation(Route.class)
      .map(Route::value)
      .orElse(field.getName());
    final var fieldValueOptional = field.getValue()
      .filter(o -> this.finalClass.isAssignableFrom(o.getClass()))
      .map(this.finalClass::cast);
    final var valueAtPath = this.valueAtPath(path, fieldValueOptional.orElse(null));
    if (fieldValueOptional.isPresent()) {
      if (valueAtPath.isPresent()) {
        field.setValue(valueAtPath.get());
      } else {
        final var fieldValue = fieldValueOptional.get();
        if (fieldValue instanceof DataSerializer) {
          this.toRaw(this.getSection().getSectionOrCreate(path), (DataSerializer) fieldValue);
        } else {
          this.toRaw(fieldValue).ifPresent(r -> this.getSection().set(path, r));
        }
      }
    } else {
      valueAtPath.ifPresent(field::setValue);
    }
  }

  /**
   * calculates value at path.
   *
   * @param path the path to calculate.
   * @param fieldValue the field value to calculate.
   *
   * @return value at path.
   */
  @NotNull
  private Optional<F> valueAtPath(@NotNull final String path,
                                  @Nullable final F fieldValue) {
    final var finalValue = this.toFinal(this.getSection(), path, fieldValue);
    final var otherFinalValue = finalValue.isPresent()
      ? finalValue
      : this.toConfigObject(this.getSection(), path).flatMap(r -> this.toFinal(r, fieldValue));
    return this.getSection().contains(path)
      ? otherFinalValue
      : Optional.empty();
  }
}
