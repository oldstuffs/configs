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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents json type implementation of {@link ConfigType}.
 */
public final class JsonType implements ConfigType {

  /**
   * the factory.
   */
  private static final JsonFactory FACTORY = new JsonFactoryBuilder()
    .configure(JsonReadFeature.ALLOW_JAVA_COMMENTS, true)
    .build();

  /**
   * the instance.
   */
  private static final JsonType INSTANCE = new JsonType();

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

  @Override
  public void load(@NotNull final File file) {
    try {
      final var parsed = JsonType.FACTORY.createParser(file)
        .readValueAs(Map.class);
      System.out.println(parsed);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
