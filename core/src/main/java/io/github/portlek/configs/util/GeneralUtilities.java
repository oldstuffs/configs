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

import io.github.portlek.configs.files.configuration.ConfigurationSection;
import io.github.portlek.configs.processors.ConfigProceed;
import io.github.portlek.configs.files.json.minimaljson.Json;
import io.github.portlek.configs.files.json.minimaljson.JsonArray;
import io.github.portlek.configs.files.json.minimaljson.JsonObject;
import io.github.portlek.configs.files.json.minimaljson.JsonValue;
import java.io.*;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class GeneralUtilities {

    @NotNull
    public String addSeparator(@NotNull final String raw) {
        if (raw.isEmpty()) {
            return "";
        }
        if (raw.charAt(raw.length() - 1) == File.separatorChar) {
            return raw;
        }
        return raw + File.separatorChar;
    }

    @NotNull
    public Optional<UUID> parseUniqueId(@NotNull final String uniqueId) {
        try {
            final Optional<String> optional = Optional.of(uniqueId);
            return optional.map(UUID::fromString);
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
                 final InputStream input = GeneralUtilities.getResource(replace).orElseThrow(() ->
                     new IllegalArgumentException("The embedded resource '" + replace + "' cannot be found!"))) {
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

    @NotNull
    public Optional<InputStream> getResource(@NotNull final String path) {
        return Optional.ofNullable(ConfigProceed.class.getClassLoader().getResource(path)).flatMap(url -> {
            try {
                final URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return Optional.of(connection.getInputStream());
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
            return Optional.empty();
        });
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
        return 0.0d;
    }

    public float toFloat(@NotNull final Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }
        try {
            return Float.parseFloat(object.toString());
        } catch (final NumberFormatException ignored) {
        }
        return 0.0f;
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

    @NotNull
    public Optional<Object> jsonValueAsObject(@NotNull final JsonValue value) {
        @Nullable final Object object;
        if (value.isBoolean()) {
            object = value.asBoolean();
        } else if (value.isNumber()) {
            object = GeneralUtilities.parseNumber(value);
        } else if (value.isString()) {
            object = value.asString();
        } else if (value.isArray()) {
            object = GeneralUtilities.jsonArrayAsList(value.asArray());
        } else if (value.isObject()) {
            object = GeneralUtilities.jsonObjectAsMap(value.asObject());
        } else {
            object = null;
        }
        return Optional.ofNullable(object);
    }

    @NotNull
    public List<Object> jsonArrayAsList(@NotNull final JsonArray array) {
        final List<Object> list = new ArrayList<>(array.size());
        array.forEach(element ->
            GeneralUtilities.jsonValueAsObject(element).ifPresent(list::add));
        return list;
    }

    @NotNull
    public Map<String, Object> jsonObjectAsMap(@NotNull final JsonValue value) {
        if (!(value instanceof JsonObject)) {
            return new HashMap<>();
        }
        // noinspection unchecked
        final Iterable<JsonObject.Member> jsonObject = (Iterable<JsonObject.Member>) value;
        final Map<String, Object> map = new HashMap<>();
        jsonObject.forEach(member ->
            GeneralUtilities.jsonValueAsObject(member.getValue()).ifPresent(o ->
                map.put(member.getName(), o)));
        return map;
    }

    @NotNull
    public Optional<JsonValue> objectAsJsonValue(@NotNull final Object object) {
        @Nullable final JsonValue value;
        if (object instanceof Boolean) {
            value = Json.value(object);
        } else if (object instanceof Integer) {
            value = Json.value(object);
        } else if (object instanceof Long) {
            value = Json.value(object);
        } else if (object instanceof Float) {
            value = Json.value(object);
        } else if (object instanceof Double) {
            value = Json.value(object);
        } else if (object instanceof String) {
            value = Json.value((String) object);
        } else if (object instanceof Iterable<?>) {
            value = GeneralUtilities.collectionAsJsonArray((Iterable<?>) object);
        } else if (object instanceof Map) {
            value = GeneralUtilities.mapAsJsonObject((Map<?, ?>) object);
        } else if (object instanceof ConfigurationSection) {
            value = GeneralUtilities.mapAsJsonObject(((ConfigurationSection) object).getValues(false));
        } else {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    @NotNull
    public JsonArray collectionAsJsonArray(@NotNull final Iterable<?> collection) {
        final JsonArray array = new JsonArray();
        collection.forEach(o ->
            GeneralUtilities.objectAsJsonValue(o).ifPresent(array::add));
        return array;
    }

    @NotNull
    public JsonObject mapAsJsonObject(@NotNull final Map<?, ?> map) {
        final JsonObject object = new JsonObject();
        map.forEach((key, value) ->
            GeneralUtilities.objectAsJsonValue(value).ifPresent(jsonValue ->
                object.add(String.valueOf(key), jsonValue)));
        return object;
    }

    @NotNull
    public Object serialize(@NotNull final Object value) {
        Object finalvalue;
        if (value instanceof Object[]) {
            finalvalue = new ArrayList<>(Arrays.asList((Object[]) value));
        } else {
            finalvalue = value;
        }
        if (finalvalue instanceof ConfigurationSection) {
            finalvalue = GeneralUtilities.buildMap(((ConfigurationSection) finalvalue).getValues(false));
        } else if (finalvalue instanceof Map) {
            finalvalue = GeneralUtilities.buildMap((Map<?, ?>) finalvalue);
        } else if (finalvalue instanceof List) {
            finalvalue = GeneralUtilities.buildList((Collection<?>) finalvalue);
        }
        return finalvalue;
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
                    if (value instanceof Collection<?>) {
                        return GeneralUtilities.deserialize((Collection<?>) entry.getValue());
                    }
                    return value;
                }));
    }

    @Nullable
    private Object parseNumber(@NotNull final JsonValue number) {
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

    /**
     * Takes a Map and parses through the values, to ensure that, before saving, all objects are as appropriate as
     * possible for storage in most data formats.
     * <p>
     * Specifically it does the following:
     * for Map: calls this method recursively on the Map before putting it in the returned Map.
     * for List: calls {@link #buildList(Collection)} which functions similar to this method.
     * for ConfigurationSection: gets the values as a map and calls this method recursively on the Map before putting
     * it in the returned Map.
     * for Everything else: stores it as is in the returned Map.
     */
    @NotNull
    private Map<String, Object> buildMap(@NotNull final Map<?, ?> map) {
        return map.entrySet().stream()
            .collect(Collectors.toMap(
                entry -> Objects.toString(entry.getKey()),
                entry -> GeneralUtilities.serialize(entry.getValue())));
    }

    /**
     * Takes a Collection and parses through the values, to ensure that, before saving, all objects are as appropriate
     * as possible for storage in most data formats.
     * <p>
     * Specifically it does the following:
     * for Map: calls {@link #buildMap(Map)} on the Map before adding to the returned list.
     * for List: calls this method recursively on the List.
     * for ConfigurationSection: gets the values as a map and calls {@link #buildMap(Map)} on the Map
     * before adding to the returned list.
     * for Everything else: stores it as is in the returned List.
     */
    @NotNull
    private List<Object> buildList(@NotNull final Collection<?> collection) {
        return collection.stream()
            .map(GeneralUtilities::serialize)
            .collect(Collectors.toCollection(() -> new ArrayList<>(collection.size())));
    }

    /**
     * Functions similarly to {@link #deserialize(Map)} but only for detecting lists within
     * lists and maps within lists.
     */
    @NotNull
    private Collection<Object> deserialize(@NotNull final Collection<?> input) {
        return input.stream()
            .map(o -> {
                if (o instanceof Map) {
                    return GeneralUtilities.deserialize((Map<?, ?>) o);
                }
                if (o instanceof List<?>) {
                    return GeneralUtilities.deserialize((List<?>) o);
                }
                return o;
            })
            .collect(Collectors.toList());
    }

}
