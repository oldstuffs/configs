package io.github.portlek.configs.util;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class JsonUtils {

    private JsonUtils() {
    }

    @NotNull
    public static Optional<Object> jsonValueAsObject(@NotNull final JsonValue value) {
        @Nullable final Object object;
        if (value.isBoolean()) {
            object = value.asBoolean();
        } else if (value.isNumber()) {
            object = JsonUtils.parseNumber(value);
        } else if (value.isString()) {
            object = value.asString();
        } else if (value.isArray()) {
            object = JsonUtils.jsonArrayAsList(value.asArray());
        } else if (value.isObject()) {
            object = JsonUtils.jsonObjectAsMap(value.asObject());
        } else {
            object = null;
        }
        return Optional.ofNullable(object);
    }

    @NotNull
    public static List<Object> jsonArrayAsList(@NotNull final JsonArray array) {
        return array.values().stream()
            .map(jsonValue -> JsonUtils.jsonValueAsObject(jsonValue).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @NotNull
    public static Map<String, Object> jsonObjectAsMap(@NotNull final JsonValue value) {
        final Map<String, Object> map = new HashMap<>();
        if (value instanceof JsonObject) {
            ((JsonObject) value).forEach(member ->
                JsonUtils.jsonValueAsObject(member.getValue()).ifPresent(o ->
                    map.put(member.getName(), o)
                )
            );
        }
        return map;
    }

    @NotNull
    public static Optional<JsonValue> objectAsJsonValue(@NotNull final Object object) {
        @Nullable final JsonValue value;
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
        } else if (object instanceof Iterable) {
            value = JsonUtils.collectionAsJsonArray((Iterable<?>) object);
        } else if (object instanceof Map) {
            value = JsonUtils.mapAsJsonObject((Map<?, ?>) object);
        }
//        else if (object instanceof ConfigurationSection) {
//            value = JsonHelper.mapAsJsonObject(((ConfigurationSection) object).getValues(false));
//        }
        else {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    @NotNull
    public static JsonArray collectionAsJsonArray(@NotNull final Iterable<?> iterable) {
        final JsonArray array = new JsonArray();
        iterable.forEach(o -> JsonUtils.objectAsJsonValue(o).ifPresent(array::add));
        return array;
    }

    @NotNull
    public static JsonObject mapAsJsonObject(@NotNull final Map<?, ?> map) {
        final JsonObject object = new JsonObject();
        map.forEach((key, value) ->
            JsonUtils.objectAsJsonValue(value).ifPresent(jsonValue ->
                object.add(String.valueOf(key), jsonValue)
            )
        );
        return object;
    }

    @Nullable
    private static Object parseNumber(@NotNull final JsonValue number) {
        @Nullable Object object = null;
        try {
            Long.parseLong(number.toString());
            object = number.asLong();
        } catch (final NumberFormatException e) {
            try {
                Integer.parseInt(number.toString());
                object = number.asInt();
            } catch (final NumberFormatException e1) {
                try {
                    Float.parseFloat(number.toString());
                    object = number.asFloat();
                } catch (final NumberFormatException e2) {
                    try {
                        Double.parseDouble(number.toString());
                        object = number.asDouble();
                    } catch (final NumberFormatException ignored) {
                    }
                }
            }
        }
        return object;
    }

}
