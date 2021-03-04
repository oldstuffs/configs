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

package io.github.portlek.configs.paths.def;

import io.github.portlek.configs.ConfigPath;
import io.github.portlek.configs.DefaultPath;
import io.github.portlek.configs.paths.BaseDefaultPath;
import io.github.portlek.configs.paths.raw.IntegerPath;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents integer default path.
 */
@RequiredArgsConstructor
public final class IntegerDefaultPath implements DefaultPath<Integer, Integer> {

  /**
   * the original.
   */
  @NotNull
  @Delegate
  private final DefaultPath<Integer, Integer> original;

  /**
   * ctor.
   *
   * @param path the path.
   * @param def the default value.
   */
  public IntegerDefaultPath(@NotNull final ConfigPath<Integer, Integer> path, final int def) {
    this(new BaseDefaultPath<>(def, path));
  }

  /**
   * ctor.
   *
   * @param path the path.
   * @param def the default value.
   */
  public IntegerDefaultPath(@NotNull final String path, final int def) {
    this(new IntegerPath(path), def);
  }
}