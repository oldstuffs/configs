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

/**
 * Utils for casting number types to other number types
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/util/NumberConversions.java">Bukkit
 *   Source</a>
 */
public final class NumberConversions {

  private NumberConversions() {
  }

  public static int ceil(final double num) {
    final int floor = (int) num;
    return floor == num ? floor : floor + (int) (~Double.doubleToRawLongBits(num) >>> 63);
  }

  public static int floor(final double num) {
    final int floor = (int) num;
    return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
  }

  public static int round(final double num) {
    return NumberConversions.floor(num + 0.5d);
  }

  public static double square(final double num) {
    return num * num;
  }

  public static byte toByte(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).byteValue();
    }
    try {
      return Byte.valueOf(object.toString());
    } catch (final NumberFormatException | NullPointerException e) {
    }
    return 0;
  }

  public static double toDouble(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).doubleValue();
    }
    try {
      return Double.valueOf(object.toString());
    } catch (final NumberFormatException | NullPointerException e) {
    }
    return 0;
  }

  public static float toFloat(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).floatValue();
    }
    try {
      return Float.valueOf(object.toString());
    } catch (final NumberFormatException | NullPointerException e) {
    }
    return 0;
  }

  public static int toInt(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).intValue();
    }
    try {
      return Integer.valueOf(object.toString());
    } catch (final NumberFormatException | NullPointerException e) {
    }
    return 0;
  }

  public static long toLong(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).longValue();
    }
    try {
      return Long.valueOf(object.toString());
    } catch (final NumberFormatException | NullPointerException e) {
    }
    return 0;
  }

  public static short toShort(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).shortValue();
    }
    try {
      return Short.valueOf(object.toString());
    } catch (final NumberFormatException | NullPointerException e) {
    }
    return 0;
  }
}
