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

import io.github.portlek.configs.Loader;
import io.github.portlek.configs.annotation.Route;
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.reflection.RefField;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * an abstract class to load fields which have to write in a {@link ConfigurationSection}.
 *
 * @param <R> type of the raw value.
 * @param <F> type of the final value.
 */
public abstract class GenericFieldLoader<R, F> extends BaseFieldLoader implements SectionSerializer<R, F> {

  /**
   * the persistent class.
   */
  @NotNull
  private final Class<F> persistentClass;

  /**
   * ctor.
   */
  @SuppressWarnings("unchecked")
  protected GenericFieldLoader() {
    try {
      final var x = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
      if (x.length == 1) {
        this.persistentClass = (Class<F>) x[0];
      } else {
        this.persistentClass = (Class<F>) x[1];
      }
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean canLoad(@NotNull final Loader loader, @NotNull final RefField field) {
    return this.persistentClass.isAssignableFrom(field.getType());
  }

  @Override
  public final void onLoad(@NotNull final Loader loader, @NotNull final RefField field) {
    final var path = field.getAnnotation(Route.class)
      .map(Route::value)
      .orElse(field.getName());
    final var fieldValueOptional = field.getValue()
      .filter(o -> this.persistentClass.isAssignableFrom(o.getClass()))
      .map(this.persistentClass::cast);
    final var section = this.prepareSection(this.getSection(loader), path);
    final var finalValue0 = this.toFinal(section, fieldValueOptional.orElse(null));
    final Optional<F> valueAtPath;
    if (finalValue0.isPresent()) {
      valueAtPath = finalValue0;
    } else {
      valueAtPath = this.toConfigObject(section, path)
        .flatMap(r -> this.toFinal(r, fieldValueOptional.orElse(null)));
    }
    if (fieldValueOptional.isPresent()) {
      if (valueAtPath.isPresent()) {
        field.setValue(valueAtPath.get());
      } else {
        final var fieldValue = fieldValueOptional.get();
        if (fieldValue instanceof DataSerializer) {
          this.toRaw(section, (DataSerializer) fieldValue);
        } else {
          this.toRaw(fieldValue).ifPresent(r -> section.set(path, r));
        }
      }
    } else {
      valueAtPath.ifPresent(field::setValue);
    }
  }

  /**
   * prepares the configuration section.
   *
   * @param currentSection the current section.
   * @param path the path to prepare.
   *
   * @return prepared configuration section.
   */
  @NotNull
  protected ConfigurationSection prepareSection(@NotNull final ConfigurationSection currentSection,
                                                @NotNull final String path) {
    return currentSection;
  }
}
