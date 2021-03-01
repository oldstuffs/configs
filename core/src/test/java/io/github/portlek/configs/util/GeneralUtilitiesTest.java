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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.HasValues;
import org.llorllale.cactoos.matchers.IsTrue;

class GeneralUtilitiesTest {

  @Test
  void addSeparator() {
    final String one = "";
    final String two = "test";
    final String three = "test/";
    final String four = "test/test";
    final String five = "test/test/";
    MatcherAssert.assertThat(
      "Touched!",
      GeneralUtilities.addSeparator(one),
      new IsEqual<>("")
    );
    MatcherAssert.assertThat(
      "It didn't add the separator!",
      GeneralUtilities.addSeparator(two),
      new IsEqual<>("test/")
    );
    MatcherAssert.assertThat(
      "Touched!",
      GeneralUtilities.addSeparator(three),
      new IsEqual<>("test/")
    );
    MatcherAssert.assertThat(
      "It didn't add the separator!",
      GeneralUtilities.addSeparator(four),
      new IsEqual<>("test/test/")
    );
    MatcherAssert.assertThat(
      "Touched!",
      GeneralUtilities.addSeparator(five),
      new IsEqual<>("test/test/")
    );
  }

  @Test
  void basedir() {
    GeneralUtilities.basedir(this.getClass());
  }

  @Test
  void calculatePath() {
    final String one = GeneralUtilities.calculatePath("_", "-", "test-test", "fallback_fallback");
    final String two = GeneralUtilities.calculatePath("_", "-", "", "fallback_fallback");
    final String three = GeneralUtilities.calculatePath("-", "_", "", "fallback-fallback");
    MatcherAssert.assertThat(
      "Couldn't calculate the path!",
      one,
      new IsEqual<>("test-test")
    );
    MatcherAssert.assertThat(
      "Couldn't calculate the path!",
      two,
      new IsEqual<>("fallback-fallback")
    );
    MatcherAssert.assertThat(
      "Couldn't calculate the path!",
      three,
      new IsEqual<>("fallback_fallback")
    );
  }

  @Test
  void getResource() throws IOException {
    final Optional<InputStream> resource = GeneralUtilities.getResource(this.getClass(), "util/generalutilities/test.yml");
    MatcherAssert.assertThat(
      "Couldn't find the resource",
      resource.isPresent(),
      new IsTrue()
    );
    final InputStream stream = resource.get();
    final byte[] bytes = new byte[1024];
    MatcherAssert.assertThat(
      "The resource file not created with just 30 bytes!",
      stream.read(bytes),
      new IsEqual<>(12)
    );
  }

  @Test
  void instanceOptional() {
    final Optional<String> optional = GeneralUtilities.instanceOptional(() -> "Test");
    MatcherAssert.assertThat(
      "Couldn't return the correct value!",
      optional.isPresent(),
      new IsTrue()
    );
    MatcherAssert.assertThat(
      "Couldn't return the correct value!",
      optional.get(),
      new IsEqual<>("Test")
    );
  }

  @Test
  void parseUniqueId() {
    final UUID uuid = UUID.randomUUID();
    final Optional<UUID> optional = GeneralUtilities.parseUniqueId(uuid.toString());
    MatcherAssert.assertThat(
      "Couldn't parse the unique id!",
      optional.isPresent(),
      new IsTrue()
    );
    MatcherAssert.assertThat(
      "Couldn't parse the unique id!",
      optional.get(),
      new IsEqual<>(uuid)
    );
  }

  @Test
  void putDot() {
    final String one = GeneralUtilities.putDot("");
    final String two = GeneralUtilities.putDot("test.");
    final String three = GeneralUtilities.putDot("test.");
    MatcherAssert.assertThat(
      "Touched!",
      one,
      new IsEqual<>("")
    );
    MatcherAssert.assertThat(
      "Couldn't put the dot correctly!",
      two,
      new IsEqual<>("test.")
    );
    MatcherAssert.assertThat(
      "Touched!",
      three,
      new IsEqual<>("test.")
    );
  }

  @Test
  void saveResource() throws IOException {
    final File basedir = GeneralUtilities.basedir(this.getClass());
    final File outFile = new File(basedir.getParentFile(), "util/generalutilities/test.yml");
    GeneralUtilities.saveResource(this.getClass(), outFile, "util/generalutilities/test.yml");
    final List<String> lines = Files.readAllLines(outFile.toPath());
    MatcherAssert.assertThat(
      "Couldn't save the resource!",
      lines,
      new HasValues<>(
        "test: \"test\""
      )
    );
  }
}