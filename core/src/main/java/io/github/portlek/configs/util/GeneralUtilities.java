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

import io.github.portlek.configs.processors.ConfigProceed;
import java.io.*;
import java.net.URI;
import java.net.URLConnection;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class GeneralUtilities {

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
        return Optional.ofNullable(ConfigProceed.class.getClassLoader().getResource(path)).map(url -> {
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

    public int toInt(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public double toDouble(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public float toFloat(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return (float) 0;
    }

    public long toLong(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        try {
            return Long.parseLong(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0L;
    }

    /**
     * <p>Validate that the argument condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     *
     * <pre>
     * Validate.isTrue( (i bigger than 0), "The value must be greater than zero");
     * Validate.isTrue( myObject.isOk(), "The object is not OK");
     * </pre>
     *
     * @param expression the boolean expression to check
     * @param message the exception message if invalid
     * @throws IllegalArgumentException if expression is {@code false}
     */
    public void isTrue(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
    // notNull
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument is not {@code null};
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.notNull(myObject, "The object must not be null");</pre>
     *
     * @param object the object to check
     * @param message the exception message if invalid
     */
    public void notNull(final Object object, final String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
    // notEmpty string
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument string is
     * neither {@code null} nor a length from zero (no characters);
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.notEmpty(myString, "The string must not be empty");</pre>
     *
     * @param string the string to check
     * @param message the exception message if invalid
     * @throws IllegalArgumentException if the string is empty
     */
    public void notEmpty(final String string, final String message) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

}
