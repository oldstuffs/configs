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

import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.jsonparser.Json;
import io.github.portlek.configs.jsonparser.JsonArray;
import io.github.portlek.configs.jsonparser.JsonObject;
import io.github.portlek.configs.jsonparser.JsonValue;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class JsonHelper {

    private JsonHelper() {
    }

    @NotNull
    public static Optional<Object> jsonValueAsObject(@NotNull final JsonValue value) {
        @Nullable final Object object;

        if (value.isBoolean()) {
            object = value.asBoolean();
        } else if (value.isNumber()) {
            object = JsonHelper.parseNumber(value);
        } else if (value.isString()) {
            object = value.asString();
        } else if (value.isArray()) {
            object = JsonHelper.jsonArrayAsList(value.asArray());
        } else if (value.isObject()) {
            object = JsonHelper.jsonObjectAsMap(value.asObject());
        } else {
            object = null;
        }

        return Optional.ofNullable(object);
    }

    @NotNull
    public static List<Object> jsonArrayAsList(@NotNull final JsonArray array) {
        final List<Object> list = new ArrayList<>(array.size());

        for (final JsonValue element : array) {
            JsonHelper.jsonValueAsObject(element).ifPresent(list::add);
        }

        return list;
    }

    @NotNull
    public static Map<String, Object> jsonObjectAsMap(@NotNull final JsonValue value) {
        if (!(value instanceof JsonObject)) {
            return new HashMap<>();
        }
        final Iterable<JsonObject.Member> jsonObject = (JsonObject) value;
        final Map<String, Object> map = new HashMap<>();

        jsonObject.forEach(member ->
            JsonHelper.jsonValueAsObject(member.getValue()).ifPresent(o ->
                map.put(member.getName(), o)
            )
        );

        return map;
    }

    @NotNull
    public static Optional<JsonValue> objectAsJsonValue(@NotNull final Object object) {
        final JsonValue value;
        if (object instanceof Boolean) {
            value = Json.value((Boolean) object);
        } else if (object instanceof Integer) {
            value = Json.value((Integer) object);
        } else if (object instanceof Long) {
            value = Json.value((Long) object);
        } else if (object instanceof Float) {
            value = Json.value((Float) object);
        } else if (object instanceof Double) {
            value = Json.value((Double) object);
        } else if (object instanceof String) {
            value = Json.value((String) object);
        } else if (object instanceof Collection) {
            value = JsonHelper.collectionAsJsonArray((Collection<?>) object);
        } else if (object instanceof Map) {
            value = JsonHelper.mapAsJsonObject((Map<?, ?>) object);
        } else if (object instanceof ConfigurationSection) {
            value = JsonHelper.mapAsJsonObject(((ConfigurationSection) object).getValues(false));
        } else {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    @NotNull
    public static JsonArray collectionAsJsonArray(@NotNull final Iterable<?> collection) {
        final JsonArray array = new JsonArray();
        collection.forEach(o ->
            JsonHelper.objectAsJsonValue(o).ifPresent(array::add));
        return array;
    }

    @NotNull
    public static JsonObject mapAsJsonObject(@NotNull final Map<?, ?> map) {
        final JsonObject object = new JsonObject();
        map.forEach((key, value) ->
            JsonHelper.objectAsJsonValue(value).ifPresent(jsonValue ->
                object.add(String.valueOf(key), jsonValue)
            )
        );
        return object;
    }

    @Nullable
    private static Object parseNumber(@NotNull final JsonValue number) {
        try {
            Integer.parseInt(number.toString());
            return number.asInt();
        } catch (final NumberFormatException e) {
            try {
                Long.parseLong(number.toString());
                return number.asLong();
            } catch (final NumberFormatException e1) {
                try {
                    Double.parseDouble(number.toString());
                    return number.asDouble();
                } catch (final NumberFormatException ignored) {
                }
            }
        }
        return null;
    }

}
