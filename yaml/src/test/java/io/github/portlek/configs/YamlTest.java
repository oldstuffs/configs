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

import io.github.portlek.configs.annotation.Ignore;
import io.github.portlek.configs.annotation.Route;
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.configuration.FileConfiguration;
import io.github.portlek.configs.yaml.YamlType;
import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

public final class YamlTest {

  public static void main(final String[] args) throws ExecutionException, InterruptedException {
    ConfigLoader.builder()
      .setFileName("test")
      .setFolder(Path.of(System.getProperty("user.dir")))
      .setConfigType(YamlType.get())
      .setConfigHolder(new ConfigHolder0(new File("test")))
      .setAsyncExecutor(Executors.newFixedThreadPool(4))
      .build()
      .load(true, true)
      .thenAccept(configLoader -> {
        System.out.println("config holder 1 -> " + ConfigHolder1.testMap);
        System.out.println(ConfigHolder0.address);
      }).get();
  }

  @RequiredArgsConstructor
  private static final class ConfigHolder0 implements ConfigHolder {

    public static final ConfigHolder1 CHILD = new ConfigHolder1();

    public static InetSocketAddress address = new InetSocketAddress("localhost", 25565);

    public static FileConfiguration configuration;

    public static File file;

    public static ConfigLoader loader;

    public static ConfigurationSection section;

    public static String test = "test";

    public static List<Integer> testList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    @NotNull
    @Ignore
    private final File folder;
  }

  @Route("section-2")
  private static final class ConfigHolder1 implements ConfigHolder {

    public static FileConfiguration configuration;

    public static File file;

    public static ConfigLoader loader;

    public static ConfigurationSection section;

    public static String test = "test section";

    public static List<String> testList = List.of("1", "2", "3", "4", "5", "6", "7");

    public static Map<String, Integer> testMap = Map.of(
      "test-1", 1,
      "test-2", 2,
      "test-3", 3,
      "test-4", 4,
      "test-5", 5);
  }
}
