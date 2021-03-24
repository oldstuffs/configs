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

package io.github.portlek.configs.lang;

import io.github.portlek.configs.ConfigLoader;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that contains language keys with its values.
 *
 * @param <T> type of the values.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Log
public final class LangValue<T> {

  /**
   * the current lang.
   */
  @NotNull
  private final AtomicReference<String> currentLang = new AtomicReference<>();

  /**
   * the t class.
   */
  @NotNull
  private final Class<T> type;

  /**
   * keys and its value as {@code T}.
   */
  @NotNull
  private final Map<String, T> values;

  /**
   * the loader.
   */
  @Nullable
  private LangLoader loader;

  /**
   * creates a new {@link Builder} instance.
   *
   * @param type the type to create.
   * @param <T> type of the values.
   *
   * @return a builder instance.
   */
  @NotNull
  public static <T> Builder<T> builder(@NotNull final Class<T> type) {
    return new Builder<>(type);
  }

  /**
   * creates a simple {@code this} from the given {@code values}.
   *
   * @param type the type to create.
   * @param key1 the key 1 to create.
   * @param value1 the value 1 to create.
   * @param <T> type of the values.
   *
   * @return a newly created {@code this} instance.
   */
  @NotNull
  public static <T> LangValue<T> create(@NotNull final Class<T> type,
                                        @NotNull final String key1, @NotNull final T value1) {
    return LangValue.create(type,
      new AbstractMap.SimpleEntry<>(key1, value1));
  }

  /**
   * creates a simple {@code this} from the given {@code values}.
   *
   * @param type the type to create.
   * @param key1 the key 1 to create.
   * @param value1 the value 1 to create.
   * @param key2 the key 2 to create.
   * @param value2 the value 2 to create.
   * @param <T> type of the values.
   *
   * @return a newly created {@code this} instance.
   */
  @NotNull
  public static <T> LangValue<T> create(@NotNull final Class<T> type,
                                        @NotNull final String key1, @NotNull final T value1,
                                        @NotNull final String key2, @NotNull final T value2) {
    return LangValue.create(type,
      new AbstractMap.SimpleEntry<>(key1, value1),
      new AbstractMap.SimpleEntry<>(key2, value2));
  }

  /**
   * creates a simple {@code this} from the given {@code values}.
   *
   * @param type the type to create.
   * @param key1 the key 1 to create.
   * @param value1 the value 1 to create.
   * @param key2 the key 2 to create.
   * @param value2 the value 2 to create.
   * @param key3 the key 3 to create.
   * @param value3 the value 3 to create.
   * @param <T> type of the values.
   *
   * @return a newly created {@code this} instance.
   */
  @NotNull
  public static <T> LangValue<T> create(@NotNull final Class<T> type,
                                        @NotNull final String key1, @NotNull final T value1,
                                        @NotNull final String key2, @NotNull final T value2,
                                        @NotNull final String key3, @NotNull final T value3) {
    return LangValue.create(type,
      new AbstractMap.SimpleEntry<>(key1, value1),
      new AbstractMap.SimpleEntry<>(key2, value2),
      new AbstractMap.SimpleEntry<>(key3, value3));
  }

  /**
   * creates a simple {@code this} from the given {@code values}.
   *
   * @param type the type to create.
   * @param key1 the key 1 to create.
   * @param value1 the value 1 to create.
   * @param key2 the key 2 to create.
   * @param value2 the value 2 to create.
   * @param key3 the key 3 to create.
   * @param value3 the value 3 to create.
   * @param key4 the key 4 to create.
   * @param value4 the value 4 to create.
   * @param <T> type of the values.
   *
   * @return a newly created {@code this} instance.
   */
  @NotNull
  public static <T> LangValue<T> create(@NotNull final Class<T> type,
                                        @NotNull final String key1, @NotNull final T value1,
                                        @NotNull final String key2, @NotNull final T value2,
                                        @NotNull final String key3, @NotNull final T value3,
                                        @NotNull final String key4, @NotNull final T value4) {
    return LangValue.create(type,
      new AbstractMap.SimpleEntry<>(key1, value1),
      new AbstractMap.SimpleEntry<>(key2, value2),
      new AbstractMap.SimpleEntry<>(key3, value3),
      new AbstractMap.SimpleEntry<>(key4, value4));
  }

  /**
   * creates a simple {@code this} from the given {@code values}.
   *
   * @param type the type to create.
   * @param key1 the key 1 to create.
   * @param value1 the value 1 to create.
   * @param key2 the key 2 to create.
   * @param value2 the value 2 to create.
   * @param key3 the key 3 to create.
   * @param value3 the value 3 to create.
   * @param key4 the key 4 to create.
   * @param value4 the value 4 to create.
   * @param key5 the key 5 to create.
   * @param value5 the value 5 to create.
   * @param <T> type of the values.
   *
   * @return a newly created {@code this} instance.
   */
  @NotNull
  public static <T> LangValue<T> create(@NotNull final Class<T> type,
                                        @NotNull final String key1, @NotNull final T value1,
                                        @NotNull final String key2, @NotNull final T value2,
                                        @NotNull final String key3, @NotNull final T value3,
                                        @NotNull final String key4, @NotNull final T value4,
                                        @NotNull final String key5, @NotNull final T value5) {
    return LangValue.create(type,
      new AbstractMap.SimpleEntry<>(key1, value1),
      new AbstractMap.SimpleEntry<>(key2, value2),
      new AbstractMap.SimpleEntry<>(key3, value3),
      new AbstractMap.SimpleEntry<>(key4, value4),
      new AbstractMap.SimpleEntry<>(key5, value5));
  }

  /**
   * creates a simple {@code this} from the given {@code values}.
   *
   * @param type the type to create.
   * @param values the values to create.
   * @param <T> type of the values.
   *
   * @return a newly created {@code this} instance.
   */
  @SafeVarargs
  @NotNull
  public static <T> LangValue<T> create(@NotNull final Class<T> type, @NotNull final Map.Entry<String, T>... values) {
    return LangValue.create(type, Arrays.stream(values)
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, HashMap::new)));
  }

  /**
   * creates a simple {@code this} from the given {@code values}.
   *
   * @param type the type to create.
   * @param values the values to create.
   * @param <T> type of the values.
   *
   * @return a newly created {@code this} instance.
   */
  @NotNull
  public static <T> LangValue<T> create(@NotNull final Class<T> type, @NotNull final Map<String, T> values) {
    return LangValue.builder(type)
      .setDefaults(values)
      .build();
  }

  /**
   * gets the value at {@link LangLoader#getDefaultLanguage()}.
   *
   * @param lang the lang to set {@link #currentLang}.
   *
   * @return value at the default language of {@link #loader}.
   */
  @NotNull
  public Optional<T> get(@NotNull final String lang) {
    return this.lang(lang).get();
  }

  /**
   * gets the value at {@link LangLoader#getDefaultLanguage()}.
   *
   * @return value at the default language of {@link #loader}.
   */
  @NotNull
  public Optional<T> get() {
    return Optional.ofNullable(this.currentLang.get()).map(this.values::get);
  }

  /**
   * obtains the {@link #loader}.
   *
   * @return holder.
   */
  @NotNull
  public LangLoader getLoader() {
    return Objects.requireNonNull(this.loader, "Load the fields before use #getLoader() method!");
  }

  /**
   * sets {@link #currentLang} if the holder is not null and {@link LangLoader#getKeys()} contains the
   * given {@code lang}.
   *
   * @param lang the lang to set.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public LangValue<T> lang(@NotNull final String lang) {
    if (this.loader == null) {
      return this;
    }
    final var control = this.loader.getValues().stream()
      .map(ConfigLoader.Builder::getFileName)
      .filter(Objects::nonNull)
      .anyMatch(lang::equalsIgnoreCase);
    if (control) {
      this.currentLang.set(lang);
    } else {
      LangValue.log.log(Level.SEVERE, lang + " not found!");
    }
    return this;
  }

  /**
   * puts the given key and key into {@link #values}.
   *
   * @param key the key to put.
   * @param value the value to put.
   */
  public void put(@NotNull final String key, @NotNull final T value) {
    this.values.put(key, value);
  }

  /**
   * sets hte {@link #loader} and {@link #currentLang}.
   *
   * @param loader the loader to set.
   */
  public void setHolder(@NotNull final LangLoader loader) {
    this.loader = loader;
    loader.getDefaultLanguage().ifPresent(this.currentLang::set);
  }

  /**
   * a class that helps to create {@link LangValue} instances.
   *
   * @param <T> type of the values.
   */
  @RequiredArgsConstructor
  public static final class Builder<T> {

    /**
     * the type.
     */
    @NotNull
    private final Class<T> type;

    /**
     * keys and its value as {@code T}.
     */
    @NotNull
    private Map<String, T> defaults = new HashMap<>();

    /**
     * adds the given key and its value into {@link #defaults}.
     *
     * @param key the key to add.
     * @param value the value to add.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder<T> addDefault(@NotNull final String key, @NotNull final T value) {
      this.defaults.put(key, value);
      return this;
    }

    /**
     * builds ands creates a {@link LangValue} instance.
     *
     * @return lang value instance.
     */
    @NotNull
    public LangValue<T> build() {
      return new LangValue<>(this.type, new HashMap<>(this.defaults));
    }

    /**
     * sets {@link #defaults} to the given {@code values}.
     *
     * @param defaults the defaults to set.
     *
     * @return {@code this} for builder chain.
     */
    @NotNull
    public Builder<T> setDefaults(@NotNull final Map<String, T> defaults) {
      this.defaults = Objects.requireNonNull(defaults, "values");
      return this;
    }
  }
}
