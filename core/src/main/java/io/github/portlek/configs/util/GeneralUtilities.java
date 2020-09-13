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

package io.github.portlek.configs.util;

import java.io.*;
import java.net.URLConnection;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class GeneralUtilities {

    @NotNull
    public String addSeparator(@NotNull final String raw) {
        if (raw.isEmpty() || raw.charAt(raw.length() - 1) == '/') {
            return raw;
        }
        return raw + '/';
    }

    @NotNull
    public <T> Optional<T> instanceOptional(@NotNull final Supplier<T> supplier) {
        try {
            return Optional.ofNullable(supplier.get());
        } catch (final Throwable exception) {
            return Optional.empty();
        }
    }

    @NotNull
    public Optional<UUID> parseUniqueId(@NotNull final String uniqueId) {
        return GeneralUtilities.instanceOptional(() -> UUID.fromString(uniqueId));
    }

    @SneakyThrows
    @NotNull
    public File basedir(@NotNull final Class<?> clazz) {
        return new File(
            clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    @NotNull
    public String calculatePath(@NotNull final String regex, @NotNull final String separator,
                                @NotNull final String rawpath, @NotNull final String fallback) {
        if (!rawpath.isEmpty()) {
            return rawpath;
        }
        if (regex.isEmpty() || separator.isEmpty()) {
            return fallback;
        }
        return fallback.replace(regex, separator);
    }

    @NotNull
    public String putDot(@NotNull final String text) {
        if (text.isEmpty() || text.charAt(text.length() - 1) == '.') {
            return text;
        }
        return text + '.';
    }

    @SneakyThrows
    @NotNull
    public void saveResource(@NotNull final Class<?> clazz, @NotNull final File outFile, @NotNull final String path) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be empty");
        }
        if (!outFile.exists()) {
            outFile.getParentFile().mkdirs();
            outFile.createNewFile();
        }
        try (final OutputStream out = new FileOutputStream(outFile);
             final InputStream input = GeneralUtilities.getResource(clazz, path).orElseThrow(() ->
                 new IllegalArgumentException("The embedded resource '" + path + "' cannot be found!"))) {
            final byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
    }

    @NotNull
    public Optional<InputStream> getResource(@NotNull final Class<?> clazz, @NotNull final String path) {
        return Optional.ofNullable(clazz.getClassLoader().getResource(path))
            .flatMap(url -> {
                try {
                    final URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);
                    return Optional.of(connection.getInputStream());
                } catch (final IOException ignored) {
                }
                return Optional.empty();
            });
    }

}
