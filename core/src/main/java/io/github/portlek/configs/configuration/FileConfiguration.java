/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
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

package io.github.portlek.configs.configuration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;


public abstract class FileConfiguration extends MemoryConfiguration {


    protected FileConfiguration() {
        super();
    }


    @SneakyThrows
    public final void save(@NotNull final File file) {
        file.getParentFile().mkdirs();
        final String data = this.saveToString();
        try (final Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write(data);
        }
    }


    @NotNull
    public abstract String saveToString();


    public final void load(@NotNull final String file) {
        this.load(new File(file));
    }


    @SneakyThrows
    public final void load(@NotNull final File file) {
        final FileInputStream stream = new FileInputStream(file);
        this.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }


    @SneakyThrows
    public final void load(@NotNull final Reader reader) {
        final BufferedReader input;
        if (reader instanceof BufferedReader) {
            input = (BufferedReader) reader;
        } else {
            input = new BufferedReader(reader);
        }
        @Cleanup final BufferedReader fnlinput = input;
        final StringBuilder builder = new StringBuilder();
        String line;
        while ((line = fnlinput.readLine()) != null) {
            builder.append(line);
            builder.append('\n');
        }
        this.loadFromString(builder.toString());
    }


    public abstract void loadFromString(@NotNull String contents);

    @NotNull
    @Override
    public FileConfigurationOptions options() {
        if (this.options == null) {
            this.options = new FileConfigurationOptions(this);
        }
        return (FileConfigurationOptions) this.options;
    }

}
