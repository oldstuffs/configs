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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an implementation to serialize raw fields.
 */
@SuppressWarnings("rawtypes")
public final class FlRawField extends BaseFieldLoader {

  /**
   * the instance.
   */
  public static final Supplier<FlRawField> INSTANCE = FlRawField::new;

  /**
   * the generic classes.
   */
  private static final List<Class> GENERICS = List.of(List.class, Map.class);

  /**
   * the raw classes.
   */
  private static final List<Class> RAWS = List.of(String.class, Integer.class, int.class, Boolean.class,
    boolean.class, long.class, Long.class, double.class, Double.class, char.class, Character.class, byte.class,
    Byte.class, float.class, Float.class, short.class, Short.class);

  /**
   * converts the given value at path into the field's type.
   *
   * @param field the field to convert.
   * @param valueAtPath the value at path to convert.
   *
   * @return converted value, null if the type of field and value at path not match.
   */
  @Nullable
  private static Object convertFieldType(@NotNull final RefField field, @NotNull final Object valueAtPath) {
    final var type = field.getType();
    if (type.isAssignableFrom(valueAtPath.getClass())) {
      return valueAtPath;
    }
    if (type == String.class) {
      return valueAtPath.toString();
    }
    return null;
  }

  /**
   * loads list.
   *
   * @param valueAtPath the value at path.
   * @param field the field.
   * @param section the section to load.
   * @param path the path to load.
   */
  private static void loadList(@NotNull final Object valueAtPath, @NotNull final RefField field,
                               @NotNull final ConfigurationSection section, @NotNull final String path) {
    FlRawField.loadList(valueAtPath, field, null, section, path);
  }

  /**
   * loads list.
   *
   * @param valueAtPath the value at path.
   * @param field the field.
   * @param fieldValue the field value to load.
   * @param section the section to load.
   * @param path the path to load.
   */
  private static void loadList(@NotNull final Object valueAtPath, @NotNull final RefField field,
                               @Nullable final Object fieldValue, @NotNull final ConfigurationSection section,
                               @NotNull final String path) {
    if (!(valueAtPath instanceof List<?>)) {
      return;
    }
    final var list = (List<?>) valueAtPath;
    if (list.isEmpty()) {
      field.setValue(new ArrayList<>());
      return;
    }
    final var listObject = list.get(0);
    final var listObjectClass = listObject.getClass();
    if (!FlRawField.RAWS.contains(listObjectClass) && !FlRawField.GENERICS.contains(listObjectClass)) {
      return;
    }
    final var genericType = (ParameterizedType) field.getRealField().getGenericType();
    final var genericClass = (Class<?>) genericType.getActualTypeArguments()[0];
    if (genericClass.isAssignableFrom(listObjectClass)) {
      field.setValue(list);
    } else if (fieldValue != null) {
      section.set(path, fieldValue);
    }
  }

  /**
   * loads map.
   *
   * @param valueAtPath the value at path.
   * @param field the field.
   * @param section the section to load.
   * @param path the path to load.
   */
  private static void loadMap(@NotNull final Object valueAtPath, @NotNull final RefField field,
                              @NotNull final ConfigurationSection section, @NotNull final String path) {
    FlRawField.loadMap(valueAtPath, field, null, section, path);
  }

  /**
   * loads map.
   *
   * @param valueAtPath the value at path.
   * @param field the field.
   * @param fieldValue the field value to load.
   * @param section the section to load.
   * @param path the path to load.
   */
  private static void loadMap(@NotNull final Object valueAtPath, @NotNull final RefField field,
                              @Nullable final Object fieldValue, @NotNull final ConfigurationSection section,
                              @NotNull final String path) {
    final Object finalValueAtPath;
    if (valueAtPath instanceof ConfigurationSection) {
      finalValueAtPath = ((ConfigurationSection) valueAtPath).getMapValues(false);
    } else {
      finalValueAtPath = valueAtPath;
    }
    if (!(finalValueAtPath instanceof Map<?, ?>)) {
      return;
    }
    final var map = (Map<?, ?>) finalValueAtPath;
    if (map.isEmpty()) {
      field.setValue(new HashMap<>());
      return;
    }
    final var mapEntry = map.entrySet().toArray(Map.Entry[]::new)[0];
    final var mapEntryKey = mapEntry.getKey();
    final var mapEntryValue = mapEntry.getValue();
    final var mapEntryKeyClass = mapEntryKey.getClass();
    final var mapEntryValueClass = mapEntryValue.getClass();
    if (!FlRawField.RAWS.contains(mapEntryKeyClass) && !FlRawField.GENERICS.contains(mapEntryKeyClass) ||
      !FlRawField.RAWS.contains(mapEntryValueClass) && !FlRawField.GENERICS.contains(mapEntryValueClass)) {
      return;
    }
    final var genericType = (ParameterizedType) field.getRealField().getGenericType();
    final var keyClass = (Class<?>) genericType.getActualTypeArguments()[0];
    final var valueClass = (Class<?>) genericType.getActualTypeArguments()[1];
    if (keyClass.isAssignableFrom(mapEntryKeyClass) &&
      valueClass.isAssignableFrom(mapEntryValueClass)) {
      field.setValue(map);
    } else if (fieldValue != null) {
      section.set(path, fieldValue);
    }
  }

  @Override
  public boolean canLoad(@NotNull final Loader loader, @NotNull final RefField field) {
    return FlRawField.RAWS.contains(field.getType()) ||
      FlRawField.GENERICS.contains(field.getType());
  }

  @Override
  public void onLoad(@NotNull final Loader loader, @NotNull final RefField field) {
    final var path = field.getAnnotation(Route.class)
      .map(Route::value)
      .orElse(field.getName());
    final var fieldValueOptional = field.of(loader.getConfigHolder()).getValue();
    final var section = this.getSection(loader);
    final var valueAtPath = section.get(path);
    final var fieldType = field.getType();
    if (fieldValueOptional.isPresent()) {
      final var fieldValue = fieldValueOptional.get();
      if (valueAtPath != null) {
        if (FlRawField.GENERICS.contains(fieldType)) {
          FlRawField.loadList(valueAtPath, field, fieldValue, section, path);
          FlRawField.loadMap(valueAtPath, field, fieldValue, section, path);
        } else {
          final var converted = FlRawField.convertFieldType(field, valueAtPath);
          if (converted == null) {
            section.set(path, fieldValue);
          } else {
            field.setValue(fieldType.cast(converted));
          }
        }
      } else {
        section.set(path, fieldValue);
      }
    } else if (valueAtPath != null) {
      if (FlRawField.GENERICS.contains(fieldType)) {
        FlRawField.loadList(valueAtPath, field, section, path);
        FlRawField.loadMap(valueAtPath, field, section, path);
      } else {
        field.setValue(fieldType.cast(valueAtPath));
      }
    }
  }
}
