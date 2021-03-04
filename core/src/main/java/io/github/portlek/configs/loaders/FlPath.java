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

import io.github.portlek.configs.ConfigLoader;
import io.github.portlek.configs.ConfigPath;
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.reflection.RefField;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an implementation to serialize {@link ConfigPath}.
 */
@SuppressWarnings("rawtypes")
public final class FlPath extends BaseFileLoader {

  /**
   * the instance.
   */
  public static final Supplier<FlConfigHolder> INSTANCE = FlConfigHolder::new;

  /**
   * the path class.
   */
  private static final Class<ConfigPath> PATH_CLASS = ConfigPath.class;

  @Override
  public boolean canLoad(@NotNull final ConfigLoader loader, @NotNull final RefField field) {
    return FlPath.PATH_CLASS.isAssignableFrom(field.getType());
  }

  @Override
  public void onLoad(@NotNull final ConfigLoader loader, @NotNull final RefField field) {
    field.getValue()
      .ifPresent(pth -> ((ConfigPath) pth).setLoader(loader));
  }
}
