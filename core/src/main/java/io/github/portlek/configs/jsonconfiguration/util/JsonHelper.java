package io.github.portlek.configs.jsonconfiguration.util;

import io.github.portlek.configs.json.Json;
import io.github.portlek.configs.json.JsonArray;
import io.github.portlek.configs.json.JsonObject;
import io.github.portlek.configs.json.JsonValue;
import io.github.portlek.configs.simpleyaml.configuration.ConfigurationSection;
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
        final JsonObject jsonObject = (JsonObject) value;
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
    public static JsonArray collectionAsJsonArray(@NotNull final Collection<?> collection) {
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
