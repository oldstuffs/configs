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

package io.github.portlek.configs.test;

import static io.github.portlek.configs.paths.Pth.comment;
import static io.github.portlek.configs.paths.Pth.string;
import io.github.portlek.configs.ConfigLoader;
import io.github.portlek.configs.PathHolder;
import io.github.portlek.configs.json.JsonType;
import io.github.portlek.configs.paths.Pth;
import io.github.portlek.configs.yaml.YamlType;
import java.nio.file.Path;

public final class TestPathHolder implements PathHolder {

  public static final Pth<String> TEST_STRING = comment(string("path.to.string",
    "default-value"), "Top comment");

  public static final Pth<String> TEST_STRING_2 = comment(string("path.string",
    "default-value"), "Top comment");

  public static final Pth<String> TEST_STRING_3 = comment(string("string",
    "default-value"), "Top comment");

  public static final Pth<String> TEST_STRING_4 = comment(string("test",
    "default-value"), "Top comment");

  public static final Pth<String> TEST_STRING_5 = comment(string("test-2",
    "default-value"), "Top comment");

  public static final Pth<String> TEST_STRING_6 = comment(string("path.string-2",
    "default-value"), "Top comment");

  public static final Pth<String> TEST_STRING_7 = comment(string("path.to.asdstring",
    "default-value"), "Top comment");

  public static final Pth<String> TEST_STRING_8 = comment(string("path.to.stringasd",
    "default-value"), "Top comment");

  public static void main(final String[] args) {
    final var here = System.getProperty("user.dir");
    final var path = Path.of(here);
    ConfigLoader.builder()
      .setFileName("test")
      .setFolderPath(path)
      .setConfigType(YamlType.get())
      .setPathHolder(new TestPathHolder())
      .build()
      .load(true);
  }
}
