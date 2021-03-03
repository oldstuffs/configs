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

package io.github.portlek.configs.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.configuration.FileConfiguration;
import io.github.portlek.configs.exceptions.InvalidConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public final class Json extends FileConfiguration {

  /**
   * the mapper.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper()
    .enable(SerializationFeature.INDENT_OUTPUT);

  /**
   * the map type.
   */
  private static final MapType MAP_TYPE = Json.MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class,
    Object.class);

  @Override
  public void loadFromString(@NotNull final String contents) throws InvalidConfigurationException {
    final Map<?, ?> input;
    try {
      input = Json.MAPPER.readValue(contents.isEmpty() ? "{}" : contents, Json.MAP_TYPE);
    } catch (final JsonProcessingException e) {
      throw new InvalidConfigurationException(e);
    }
    this.convertMapsToSections(input, this);
  }

  @Override
  public String saveToString() throws IOException {
    return Json.MAPPER.writeValueAsString(this.getMapValues(false));
  }

  private void convertMapsToSections(final Map<?, ?> input, final ConfigurationSection section) {
    input.forEach((key1, value) -> {
      final String key = key1.toString();
      if (value instanceof Map) {
        this.convertMapsToSections((Map<?, ?>) value, section.createSection(key));
      } else {
        section.set(key, value);
      }
    });
  }
}
