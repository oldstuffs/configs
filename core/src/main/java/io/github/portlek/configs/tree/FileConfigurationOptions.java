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

package io.github.portlek.configs.tree;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Various settings for controlling the input and output of a {@link FileConfiguration}
 *
 * @author Bukkit
 * @author Carlos Lazaro Costa (added charset option)
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/file/FileConfigurationOptions.java">Bukkit
 *   Source</a>
 */
public class FileConfigurationOptions extends MemoryConfigurationOptions {

  private Charset charset = StandardCharsets.UTF_8;

  private boolean copyHeader = true;

  private String header = null;

  protected FileConfigurationOptions(final MemoryConfiguration configuration) {
    super(configuration);
  }

  public Charset charset() {
    return this.charset;
  }

  public FileConfigurationOptions charset(final Charset charset) {
    this.charset = charset;
    return this;
  }

  @Override
  public FileConfiguration configuration() {
    return (FileConfiguration) super.configuration();
  }

  @Override
  public FileConfigurationOptions copyDefaults(final boolean value) {
    super.copyDefaults(value);
    return this;
  }

  @Override
  public FileConfigurationOptions pathSeparator(final char value) {
    super.pathSeparator(value);
    return this;
  }

  /**
   * Gets whether or not the header should be copied from a default source.
   *
   * @return Whether or not to copy the header
   */
  public boolean copyHeader() {
    return this.copyHeader;
  }

  /**
   * Sets whether or not the header should be copied from a default source.
   *
   * @param value Whether or not to copy the header
   *
   * @return This object, for chaining
   */
  public FileConfigurationOptions copyHeader(final boolean value) {
    this.copyHeader = value;
    return this;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.charset, this.header, this.copyHeader);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FileConfigurationOptions)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    final FileConfigurationOptions that = (FileConfigurationOptions) o;
    return this.copyHeader == that.copyHeader &&
      Objects.equals(this.charset, that.charset) &&
      Objects.equals(this.header, that.header);
  }

  /**
   * Sets the header that will be applied to the top of the saved output.
   *
   * @param value New header
   *
   * @return This object, for chaining
   */
  public FileConfigurationOptions header(final String value) {
    this.header = value;
    return this;
  }

  /**
   * Gets the header that will be applied to the top of the saved output.
   *
   * @return Header
   */
  public String header() {
    return this.header;
  }

  public boolean isUnicode() {
    return this.charset.name().startsWith("UTF");
  }
}
