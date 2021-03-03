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

import io.github.portlek.configs.ConfigType;
import io.github.portlek.configs.tree.FileConfiguration;
import io.github.portlek.configs.exceptions.InvalidConfigurationException;
import java.io.File;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public final class JsonType implements ConfigType {

  private static final JsonType INSTANCE = new JsonType();

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
  public FileConfiguration load(@NotNull final File file) throws IOException, InvalidConfigurationException {
    final var json = new Json();
    json.load(file);
    return json;
  }

  @Override
  public void save(@NotNull final File file, @NotNull final FileConfiguration configuration) throws IOException {
    configuration.save(file);
  }
}
