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
import java.net.URI;
import java.net.URLConnection;
import java.util.Optional;
import java.util.logging.Logger;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class GeneralUtilities {

    private final Logger LOG = Logger.getLogger(GeneralUtilities.class.getName());

    @NotNull
    public String addSeparator(@NotNull final String raw) {
        final String fnl;
        if (raw.isEmpty()) {
            fnl = "";
        } else if (raw.charAt(raw.length() - 1) == File.separatorChar) {
            fnl = raw;
        } else {
            fnl = raw + File.separatorChar;
        }
        return fnl;
    }

    @SneakyThrows
    @NotNull
    public Optional<File> basedir(@NotNull final Class<?> clazz) {
        final URI uri = clazz.getProtectionDomain().getCodeSource().getLocation().toURI();
        final File file = new File(uri);
        return Optional.of(file);
    }

    @NotNull
    public String calculatePath(@NotNull final String regex, @NotNull final String separator, @NotNull final String rawpath,
                                @NotNull final String fallback) {
        final String fieldpath;
        if (rawpath.isEmpty()) {
            if (!regex.isEmpty() && !separator.isEmpty()) {
                fieldpath = fallback.replace(regex, separator);
            } else {
                fieldpath = fallback;
            }
        } else {
            fieldpath = rawpath;
        }
        return fieldpath;
    }

    @NotNull
    public String putDot(@NotNull final String text) {
        final String fnltext;
        if (text.isEmpty() || text.charAt(text.length() - 1) == '.') {
            fnltext = text;
        } else {
            fnltext = text + '.';
        }
        return fnltext;
    }

    @NotNull
    public File saveResource(@NotNull final String datafolder, @NotNull final String path) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be empty");
        }
        final String replace = path.replace('\\', '/');
        final int lastindex = replace.lastIndexOf(47);
        final File outdir = new File(datafolder, replace.substring(0, Math.max(lastindex, 0)));
        if (!outdir.exists()) {
            outdir.getParentFile().mkdirs();
            outdir.mkdirs();
        }
        final File outfile = new File(datafolder, replace);
        if (!outfile.exists()) {
            try (final OutputStream out = new FileOutputStream(outfile);
                 final InputStream input = GeneralUtilities.getResource(replace)) {
                if (input == null) {
                    throw new IllegalArgumentException("The embedded resource '" + replace + "' cannot be found!");
                }
                final byte[] buf = new byte[1024];
                int len;
                while ((len = input.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }
        return outfile;
    }

    @Nullable
    public InputStream getResource(@NotNull final String path) {
        return Optional.ofNullable(GeneralUtilities.class.getClassLoader().getResource(path)).map(url -> {
            try {
                final URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
            return null;
        }).orElse(null);
    }

    public int toInt(@NotNull final Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (final NumberFormatException ignored) {
        }
        return 0;
    }

    public double toDouble(@NotNull final Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (final NumberFormatException ignored) {
        }
        return 0;
    }

    public float toFloat(@NotNull final Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (final NumberFormatException ignored) {
        }
        return (float) 0;
    }

    public long toLong(@NotNull final Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }
        try {
            return Long.parseLong(object.toString());
        } catch (final NumberFormatException ignored) {
        }
        return 0L;
    }

}
