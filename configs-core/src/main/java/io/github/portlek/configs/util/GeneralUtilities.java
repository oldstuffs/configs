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

import io.github.portlek.configs.json.minimaljson.JsonValue;
import java.io.*;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;

@UtilityClass
public class GeneralUtilities {

    @NotNull
    public String addSeparator(@NotNull final String raw) {
        if (raw.isEmpty()) {
            return "";
        }
        if (raw.charAt(raw.length() - 1) == '/') {
            return raw;
        }
        return raw + '/';
    }

    @NotNull
    public Optional<UUID> parseUniqueId(@NotNull final String uniqueId) {
        try {
            return Optional.of(uniqueId).map(UUID::fromString);
        } catch (final IllegalArgumentException e) {
            return Optional.empty();
        }
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
    public void saveResource(@NotNull final File outFile, @NotNull final String path) {
        GeneralUtilities.saveResource(GeneralUtilities.class, outFile, path);
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
    public Optional<InputStream> getResource(@NotNull final String path) {
        return GeneralUtilities.getResource(GeneralUtilities.class, path);
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

    public Optional<Integer> toInt(@Nullable final Object object) {
        if (object == null) {
            return Optional.empty();
        }
        if (object instanceof Number) {
            return Optional.of(((Number) object).intValue());
        }
        try {
            return Optional.of(Integer.parseInt(object.toString()));
        } catch (final NumberFormatException ignored) {
        }
        return Optional.empty();
    }

    public Optional<Double> toDouble(@Nullable final Object object) {
        if (object == null) {
            return Optional.empty();
        }
        if (object instanceof Number) {
            return Optional.of(((Number) object).doubleValue());
        }
        try {
            return Optional.of(Double.parseDouble(object.toString()));
        } catch (final NumberFormatException ignored) {
        }
        return Optional.empty();
    }

    public Optional<Float> toFloat(@Nullable final Object object) {
        if (object == null) {
            return Optional.empty();
        }
        if (object instanceof Number) {
            return Optional.of(((Number) object).floatValue());
        }
        try {
            return Optional.of(Float.parseFloat(object.toString()));
        } catch (final NumberFormatException ignored) {
        }
        return Optional.empty();
    }

    public Optional<Long> toLong(@Nullable final Object object) {
        if (object == null) {
            return Optional.empty();
        }
        if (object instanceof Number) {
            return Optional.of(((Number) object).longValue());
        }
        try {
            return Optional.of(Long.parseLong(object.toString()));
        } catch (final NumberFormatException ignored) {
        }
        return Optional.empty();
    }

    public Optional<Byte> toByte(@Nullable final Object object) {
        if (object == null) {
            return Optional.empty();
        }
        if (object instanceof Number) {
            return Optional.of(((Number) object).byteValue());
        }
        try {
            return Optional.of(Byte.parseByte(object.toString()));
        } catch (final NumberFormatException ignored) {
        }
        return Optional.empty();
    }

    public Optional<Short> toShort(@Nullable final Object object) {
        if (object == null) {
            return Optional.empty();
        }
        if (object instanceof Number) {
            return Optional.of(((Number) object).shortValue());
        }
        try {
            return Optional.of(Short.parseShort(object.toString()));
        } catch (final NumberFormatException ignored) {
        }
        return Optional.empty();
    }

    public Optional<Boolean> toBoolean(@Nullable final Object object) {
        if (object == null) {
            return Optional.empty();
        }
        if (object instanceof Boolean) {
            return Optional.of((Boolean) object);
        }
        if ("true".equalsIgnoreCase(object.toString())) {
            return Optional.of(true);
        }
        if ("false".equalsIgnoreCase(object.toString())) {
            return Optional.of(false);
        }
        return Optional.empty();
    }

    public Optional<Character> toCharacter(@Nullable final Object object) {
        if (object instanceof Character) {
            return Optional.of((Character) object);
        }
        return Optional.empty();
    }

    public void convertMapToSection(@NotNull final Map<?, ?> input,
                                    @NotNull final ConfigurationSection section) {
        final Map<String, Object> result = GeneralUtilities.deserialize(input);
        for (final Map.Entry<?, ?> entry : result.entrySet()) {
            final String key = entry.getKey().toString();
            final Object value = entry.getValue();
            if (value instanceof Map<?, ?>) {
                GeneralUtilities.convertMapToSection((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
    }

    @NotNull
    public Map<String, Object> deserialize(@NotNull final Map<?, ?> input) {
        return input.entrySet().stream()
            .collect(Collectors.toMap(
                entry -> Objects.toString(entry.getKey()),
                entry -> {
                    final Object value = entry.getValue();
                    if (value instanceof Map<?, ?>) {
                        return GeneralUtilities.deserialize((Map<?, ?>) value);
                    }
                    if (value instanceof Iterable<?>) {
                        return GeneralUtilities.deserialize((Iterable<?>) value);
                    }
                    if (value instanceof Stream<?>) {
                        return GeneralUtilities.deserialize(((Stream<?>) value).collect(Collectors.toList()));
                    }
                    return value;
                }));
    }

    @Nullable
    public Object parseNumber(@NotNull final JsonValue number) {
        try {
            return number.asInt();
        } catch (final NumberFormatException e) {
            try {
                return number.asLong();
            } catch (final NumberFormatException e1) {
                try {
                    return number.asDouble();
                } catch (final NumberFormatException ignored) {
                }
            }
        }
        return null;
    }

    @NotNull
    private Collection<Object> deserialize(@NotNull final Iterable<?> input) {
        final Collection<Object> objects = new ArrayList<>();
        input.forEach(o -> {
            if (o instanceof Map) {
                objects.add(GeneralUtilities.deserialize((Map<?, ?>) o));
            } else if (o instanceof List<?>) {
                objects.add(GeneralUtilities.deserialize((Iterable<?>) o));
            } else {
                objects.add(o);
            }
        });
        return objects;
    }

}
