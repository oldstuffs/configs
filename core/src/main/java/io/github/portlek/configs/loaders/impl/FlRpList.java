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

package io.github.portlek.configs.loaders.impl;

import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.loaders.GenericFieldLoader;
import io.github.portlek.replaceable.RpList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an implementation to load {@link RpList}.
 */
public final class FlRpList extends GenericFieldLoader<List<String>, RpList> {

  /**
   * the instance.
   */
  public static final Supplier<FlRpList> INSTANCE = FlRpList::new;

  /**
   * ctor.
   */
  private FlRpList() {
    super(RpList.class);
  }

  @NotNull
  @Override
  public Optional<List<String>> toConfigObject(@NotNull final ConfigurationSection section, @NotNull final String path) {
    return Optional.of(section.getStringList(path));
  }

  @NotNull
  @Override
  public Optional<RpList> toFinal(@NotNull final List<String> rawValue, @Nullable final RpList fieldValue) {
    if (fieldValue == null) {
      return Optional.of(RpList.from(rawValue));
    }
    return Optional.of(fieldValue.value(rawValue));
  }

  @NotNull
  @Override
  public Optional<List<String>> toRaw(@NotNull final RpList finalValue) {
    return Optional.of(finalValue.getValue());
  }
}
