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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents json type implementation of {@link ConfigType}.
 */
public final class JsonType implements ConfigType {

  /**
   * the instance.
   */
  private static final JsonType INSTANCE = new JsonType();

  /**
   * the factory.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper()
    .enable(SerializationFeature.INDENT_OUTPUT);

  /**
   * the map type.
   */
  private static final MapType MAP_TYPE = JsonType.MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class,
    Object.class);

  /**
   * ctor.
   */
  private JsonType() {
  }

  /**
   * obtains the singleton instance.
   *
   * @return singleton instance.
   */
  @NotNull
  public static JsonType get() {
    return JsonType.INSTANCE;
  }

  @NotNull
  @Override
  public String getSuffix() {
    return ".json";
  }

  @NotNull
  @Override
  public Map<String, Object> load(@NotNull final File file) {
    try {
      return JsonType.MAPPER.readValue(file, JsonType.MAP_TYPE);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void save(@NotNull final File file, @NotNull final Map<String, Object> map) {
    try {
      JsonType.MAPPER.writeValue(file, map);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void writeDefault(@NotNull final File file) {
    this.save(file, new HashMap<>());
  }
}
