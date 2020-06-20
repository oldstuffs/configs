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

package io.github.portlek.configs.files.json;

import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.files.json.minimaljson.Json;
import io.github.portlek.configs.files.json.minimaljson.JsonArray;
import io.github.portlek.configs.files.json.minimaljson.JsonObject;
import io.github.portlek.configs.files.json.minimaljson.JsonValue;
import io.github.portlek.configs.util.GeneralUtilities;
import java.util.*;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
class Helper {

    public void convertMapToSection(@NotNull final JsonObject object,
                                    @NotNull final ConfigurationSection section) {
        GeneralUtilities.convertMapToSection(Helper.jsonObjectAsMap(object), section);
    }

    @NotNull
    public Map<String, Object> jsonObjectAsMap(@NotNull final JsonObject object) {
        final Map<String, Object> map = new HashMap<>();
        object.forEach(member ->
            Helper.jsonValueAsObject(member.getValue()).ifPresent(o ->
                map.put(member.getName(), o)));
        return map;
    }

    @NotNull
    public JsonObject mapAsJsonObject(@NotNull final Map<?, ?> map) {
        final JsonObject object = new JsonObject();
        map.forEach((key, value) ->
            Helper.objectAsJsonValue(value).ifPresent(jsonValue ->
                object.add(String.valueOf(key), jsonValue)));
        return object;
    }

    @NotNull
    private Optional<Object> jsonValueAsObject(@NotNull final JsonValue value) {
        @Nullable final Object object;
        if (value.isBoolean()) {
            object = value.asBoolean();
        } else if (value.isNumber()) {
            object = GeneralUtilities.parseNumber(value);
        } else if (value.isString()) {
            object = value.asString();
        } else if (value.isArray()) {
            object = Helper.jsonArrayAsList(value.asArray());
        } else if (value.isObject()) {
            object = Helper.jsonObjectAsMap(value.asObject());
        } else {
            object = null;
        }
        return Optional.ofNullable(object);
    }

    @NotNull
    private List<Object> jsonArrayAsList(@NotNull final JsonArray array) {
        final List<Object> list = new ArrayList<>(array.size());
        array.forEach(element ->
            Helper.jsonValueAsObject(element).ifPresent(list::add));
        return list;
    }

    @NotNull
    private Optional<JsonValue> objectAsJsonValue(@NotNull final Object object) {
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
            value = Helper.collectionAsJsonArray((Iterable<?>) object);
        } else if (object instanceof Map) {
            value = Helper.mapAsJsonObject((Map<?, ?>) object);
        } else if (object instanceof ConfigurationSection) {
            value = Helper.mapAsJsonObject(((ConfigurationSection) object).getValues(false));
        } else {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    @NotNull
    private JsonArray collectionAsJsonArray(@NotNull final Iterable<?> collection) {
        final JsonArray array = new JsonArray();
        collection.forEach(o ->
            Helper.objectAsJsonValue(o).ifPresent(array::add));
        return array;
    }

}
