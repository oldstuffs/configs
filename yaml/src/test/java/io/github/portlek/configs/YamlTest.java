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

import io.github.portlek.configs.tree.FileConfiguration;
import io.github.portlek.configs.yaml.YamlType;
import java.nio.file.Path;

public final class YamlTest {

  public static void main(final String[] args) throws InterruptedException {
    ConfigLoader.builder()
      .setFileName("test")
      .setFolderPath(Path.of(System.getProperty("user.dir")))
      .setConfigType(YamlType.get())
      .setPathHolder(PathHolder0.class)
      .build()
      .load(true, true);
    while (true) {
      Thread.sleep(5L);
    }
  }

  private static final class PathHolder0 implements PathHolder {

    public static final DefaultPath<String> STRING_PATH_1 = Paths.string("test-1", "test-1");

    public static final DefaultPath<String> STRING_PATH_2 = Paths.string("test-2.test-1", "test-2");

    public static final DefaultPath<String> STRING_PATH_3 = Paths.string("test-2.test-2", "test-3");

    public static final DefaultPath<String> STRING_PATH_4 = Paths.string("test-3.test-1.test-1", "test-4");

    public static final DefaultPath<String> STRING_PATH_5 = Paths.string("test-3.test-2.test-1", "test-5");

    public static final DefaultPath<String> STRING_PATH_6 = Paths.string("test-3.test-3.test-1", "test-6");

    public static final DefaultPath<String> STRING_PATH_7 = Paths.string("test-3.test-2.test-2", "test-7");

    public static final DefaultPath<String> STRING_PATH_8 = Paths.string("test-3.test-3.test-3", "test-8");

    public static FileConfiguration CONFIGURATION;
  }
}
