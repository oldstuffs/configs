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
 * Utils for casting number types to other number types
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/util/NumberConversions.java">Bukkit
 *   Source</a>
 */
@UtilityClass
public class NumberConversions {

  public int ceil(final double num) {
    final int floor = (int) num;
    return floor == num ? floor : floor + (int) (~Double.doubleToRawLongBits(num) >>> 63);
  }

  public int floor(final double num) {
    final int floor = (int) num;
    return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
  }

  public int round(final double num) {
    return NumberConversions.floor(num + 0.5d);
  }

  public double square(final double num) {
    return num * num;
  }

  public byte toByte(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).byteValue();
    }
    try {
      return Byte.parseByte(object.toString());
    } catch (final NumberFormatException | NullPointerException ignored) {
    }
    return 0;
  }

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

  public float toFloat(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).floatValue();
    }
    try {
      return Float.parseFloat(object.toString());
    } catch (final NumberFormatException | NullPointerException ignored) {
    }
    return 0;
  }

  public int toInt(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).intValue();
    }
    try {
      return Integer.parseInt(object.toString());
    } catch (final NumberFormatException | NullPointerException ignored) {
    }
    return 0;
  }

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

  public short toShort(final Object object) {
    if (object instanceof Number) {
      return ((Number) object).shortValue();
    }
    try {
      return Short.parseShort(object.toString());
    } catch (final NumberFormatException | NullPointerException ignored) {
    }
    return 0;
  }
}
