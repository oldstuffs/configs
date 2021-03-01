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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import org.jetbrains.annotations.NotNull;

/**
 * This is a base class for all File based implementations of {@link Configuration}
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/file/FileConfiguration.java">Bukkit
 *   Source</a>
 */
public abstract class FileConfiguration extends MemoryConfiguration {

  /**
   * Creates an empty {@link FileConfiguration} with no default values.
   */
  public FileConfiguration() {
    super();
  }

  /**
   * Creates an empty {@link FileConfiguration} using the specified {@link
   * Configuration} as a source for all default values.
   *
   * @param defaults Default value provider
   */
  public FileConfiguration(final Configuration defaults) {
    super(defaults);
  }

  /**
   * Loads this {@link FileConfiguration} from the specified location.
   * <p>
   * All the values contained within this configuration will be removed,
   * leaving only settings and defaults, and the new values will be loaded
   * from the given file.
   * <p>
   * If the file cannot be loaded for any reason, an exception will be
   * thrown.
   * <p>
   * This method will use the {@link #options()} {@link FileConfigurationOptions#charset() charset} encoding,
   * which defaults to UTF8.
   *
   * @param file File to load from.
   *
   * @throws FileNotFoundException Thrown when the given file cannot be opened.
   * @throws IOException Thrown when the given file cannot be read.
   * @throws InvalidConfigurationException Thrown when the given file is not
   *   a valid Configuration.
   * @throws IllegalArgumentException Thrown when file is null.
   */
  public void load(@NotNull final String file) throws FileNotFoundException, IOException, InvalidConfigurationException {
    this.load(new File(file));
  }

  /**
   * Loads this {@link FileConfiguration} from the specified location.
   * <p>
   * All the values contained within this configuration will be removed,
   * leaving only settings and defaults, and the new values will be loaded
   * from the given file.
   * <p>
   * If the file cannot be loaded for any reason, an exception will be
   * thrown.
   * <p>
   * This method will use the {@link #options()} {@link FileConfigurationOptions#charset() charset} encoding,
   * which defaults to UTF8.
   *
   * @param file File to load from.
   *
   * @throws FileNotFoundException Thrown when the given file cannot be opened.
   * @throws IOException Thrown when the given file cannot be read.
   * @throws InvalidConfigurationException Thrown when the given file is not
   *   a valid Configuration.
   * @throws IllegalArgumentException Thrown when file is null.
   */
  public void load(@NotNull final File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
    this.load(new FileInputStream(file));
  }

  /**
   * Loads this {@link FileConfiguration} from the specified stream.
   * <p>
   * All the values contained within this configuration will be removed,
   * leaving only settings and defaults, and the new values will be loaded
   * from the given stream.
   * <p>
   * If the file cannot be loaded for any reason, an exception will be
   * thrown.
   * <p>
   * This method will use the {@link #options()} {@link FileConfigurationOptions#charset() charset} encoding,
   * which defaults to UTF8.
   *
   * @param stream Stream to load from
   *
   * @throws IOException Thrown when the given file cannot be read.
   * @throws InvalidConfigurationException Thrown when the given file is not
   *   a valid Configuration.
   * @throws IllegalArgumentException Thrown when stream is null.
   * @see #load(Reader)
   */
  public void load(@NotNull final InputStream stream) throws IOException, InvalidConfigurationException {
    this.load(new InputStreamReader(stream, this.options().charset()));
  }

  /**
   * Loads this {@link FileConfiguration} from the specified reader.
   * <p>
   * All the values contained within this configuration will be removed,
   * leaving only settings and defaults, and the new values will be loaded
   * from the given stream.
   * <p>
   * If the file cannot be loaded for any reason, an exception will be
   * thrown.
   *
   * @param reader the reader to load from
   *
   * @throws IOException thrown when underlying reader throws an IOException
   * @throws InvalidConfigurationException thrown when the reader does not
   *   represent a valid Configuration
   * @throws IllegalArgumentException thrown when reader is null
   */
  public void load(final Reader reader) throws IOException, InvalidConfigurationException {
    try (final BufferedReader input = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader)) {
      final StringBuilder builder = new StringBuilder();
      String line;
      while ((line = input.readLine()) != null) {
        builder.append(line);
        builder.append('\n');
      }
      this.loadFromString(builder.toString());
    }
  }

  /**
   * Loads this {@link FileConfiguration} from the specified string, as
   * opposed to from file.
   * <p>
   * All the values contained within this configuration will be removed,
   * leaving only settings and defaults, and the new values will be loaded
   * from the given string.
   * <p>
   * If the string is invalid in any way, an exception will be thrown.
   *
   * @param contents Contents of a Configuration to load.
   *
   * @throws InvalidConfigurationException Thrown if the specified string is
   *   invalid.
   * @throws IllegalArgumentException Thrown if contents is null.
   */
  public abstract void loadFromString(@NotNull String contents) throws InvalidConfigurationException;

  @Override
  public FileConfigurationOptions options() {
    if (this.options == null) {
      this.options = new FileConfigurationOptions(this);
    }
    return (FileConfigurationOptions) this.options;
  }

  /**
   * Saves this {@link FileConfiguration} to the specified location.
   * <p>
   * If the file does not exist, it will be created. If already exists, it
   * will be overwritten. If it cannot be overwritten or created, an
   * exception will be thrown.
   * <p>
   * This method will use the {@link #options()} {@link FileConfigurationOptions#charset() charset} encoding,
   * which defaults to UTF8.
   *
   * @param file File to save to.
   *
   * @throws IOException Thrown when the given file cannot be written to for
   *   any reason.
   * @throws IllegalArgumentException Thrown when file is null.
   */
  public void save(@NotNull final File file) throws IOException {
    this.write(file, this.saveToString());
  }

  /**
   * Saves this {@link FileConfiguration} to the specified location.
   * <p>
   * If the file does not exist, it will be created. If already exists, it
   * will be overwritten. If it cannot be overwritten or created, an
   * exception will be thrown.
   * <p>
   * This method will use the {@link #options()} {@link FileConfigurationOptions#charset() charset} encoding,
   * which defaults to UTF8.
   *
   * @param file File to save to.
   *
   * @throws IOException Thrown when the given file cannot be written to for any reason.
   * @throws IllegalArgumentException Thrown when file is null.
   */
  public void save(@NotNull final String file) throws IOException {
    this.save(new File(file));
  }

  /**
   * Saves this {@link FileConfiguration} to a string, and returns it.
   *
   * @return a String containing this configuration.
   *
   * @throws IOException Thrown when the contents cannot be written for any reason.
   */
  public abstract String saveToString() throws IOException;

  /**
   * Compiles the header for this {@link FileConfiguration} and returns the
   * result.
   *
   * @return Compiled header
   */
  protected String buildHeader() {
    return "";
  }

  protected void write(final File file, final String data) throws IOException {
    if (file.getParentFile() != null) {
      file.getParentFile().mkdirs();
    }
    try (final Writer writer = new OutputStreamWriter(new FileOutputStream(file), this.options().charset())) {
      writer.write(data);
    }
  }
}
