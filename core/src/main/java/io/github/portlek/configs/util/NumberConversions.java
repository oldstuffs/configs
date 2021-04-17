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

package io.github.portlek.configs.util;

import lombok.experimental.UtilityClass;

/**
 * Utils for casting number types to other number types.
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/util/NumberConversions.java">Bukkit
 *   Source</a>
 */
@UtilityClass
public class NumberConversions {

  /**
   * converts the given object into {@link Double}.
   *
   * @param object the object to convert.
   *
   * @return converted double number.
   */
  public double toDouble(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).doubleValue();
    }
    try {
      return Double.parseDouble(object.toString());
    } catch (final NumberFormatException | NullPointerException ignored) {
    }
    return 0;
  }

  /**
   * converts the given object into {@link Integer}.
   *
   * @param object the object to convert.
   *
   * @return converted integer number.
   */
  public int toInt(final Object object) {
    return NumberConversions.toInt(object, 0);
  }

  /**
   * converts the given object into {@link Integer}.
   *
   * @param object the object to convert.
   * @param fallback the fallback to convert.
   *
   * @return converted integer number.
   */
  public int toInt(final Object object, final int fallback) {
    if (object instanceof Number) {
      return ((Number) object).intValue();
    }
    try {
      return Integer.parseInt(object.toString());
    } catch (final NumberFormatException | NullPointerException ignored) {
    }
    return fallback;
  }

  /**
   * converts the given object into {@link Long}.
   *
   * @param object the object to convert.
   *
   * @return converted long number.
   */
  public long toLong(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).longValue();
    }
    try {
      return Long.parseLong(object.toString());
    } catch (final NumberFormatException | NullPointerException ignored) {
    }
    return 0;
  }
}
