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

package io.github.portlek.configs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents configs.
 */
@RequiredArgsConstructor
public final class Config {

  /**
   * the defaults.
   */
  private final Map<String, Object> defaults = new HashMap<>();

  /**
   * the loader.
   */
  @NotNull
  @Getter
  private final ConfigLoader loader;

  /**
   * adds the path and def to {@link #defaults}.
   *
   * @param path the path to add.
   * @param def the def to add.
   */
  public void addDefault(@NotNull final String path, @Nullable final Object def) {
    if (def == null) {
      this.defaults.remove(path);
    } else {
      this.defaults.put(path, def);
    }
  }

  /**
   * gets the default at the path.
   *
   * @param path the path to get.
   *
   * @return default value at the path.
   */
  @Nullable
  public Object getDefault(@NotNull final String path) {
    return this.defaults.get(path);
  }

  /**
   * obtains the defaults.
   *
   * @return defaults.
   */
  @NotNull
  public Map<String, Object> getDefaults() {
    return Collections.unmodifiableMap(this.defaults);
  }
}
