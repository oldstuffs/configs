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

import java.io.File;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * an interface to determine config types.
 */
public interface ConfigType {

  /**
   * obtains the suffix.
   *
   * @return suffix.
   */
  @NotNull
  String getSuffix();

  /**
   * loads the file.
   *
   * @param file the file to load.
   *
   * @return parsed value.
   */
  @NotNull
  Map<String, Object> load(@NotNull File file);

  /**
   * saves the map into the file.
   *
   * @param file the file to save.
   * @param map the map to save.
   */
  void save(@NotNull File file, @NotNull Map<String, Object> map);

  /**
   * writes default value to the file.
   *
   * @param file the file to write.
   */
  void writeDefault(@NotNull File file);
}
