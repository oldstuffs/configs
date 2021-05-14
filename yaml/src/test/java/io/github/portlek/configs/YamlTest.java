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

import io.github.portlek.configs.annotation.FileVersion;
import io.github.portlek.configs.annotation.From;
import io.github.portlek.configs.annotation.Route;
import io.github.portlek.configs.yaml.YamlType;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.Executors;

public final class YamlTest {

  public static void main(final String[] args) {
    ConfigLoader.builder()
      .setFileName("test")
      .setFolder(Path.of(System.getProperty("user.dir")))
      .setConfigType(YamlType.get())
      .setConfigHolder(new ConfigHolder0())
      .setAsyncExecutor(Executors.newFixedThreadPool(4))
      .addLoaders(FlTestData.INSTANCE)
      .setFileVersion(3)
      .addFileVersionOperation(
        Map.entry(1, () -> {
        }))
      .build()
      .load(true);
  }

  @FileVersion(3)
  private static final class ConfigHolder0 implements ConfigHolder {

    @From(changedVersion = 3, remove = true)
    public static TestData test = new TestData("title-1", "sub-title-1", 20, 20, 20);

    @From(createdVersion = 2)
    @Route("test-2")
    public static TestData test2 = new TestData("title-2", "sub-title-2", 20, 20, 20);

    @From(createdVersion = 3)
    @Route("test-3")
    public static TestData test3 = new TestData("title-1", "sub-title-1", 20, 20, 20);
  }
}
