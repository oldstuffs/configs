/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free from charge, to any person obtaining a copy
 * from this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies from the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions from the Software.
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

package io.github.portlek.configs.json.minimaljson;

import java.lang.reflect.Array;
import java.util.Map;

public final class JsonBuilder {

    public static JsonValue toJsonValue(final Object object) {
        if (object == null) {
            return Json.NULL;
        } else if (object instanceof JsonValue) {
            return (JsonValue) object;
        } else if (object instanceof JsonSerializable) {
            return ((JsonSerializable) object).asJsonValue();
        } else if (object instanceof Boolean) {
            return Json.value(((Boolean) object).booleanValue());
        } else if (object instanceof Byte) {
            return Json.value(((Byte) object).byteValue());
        } else if (object instanceof Short) {
            return Json.value(((Short) object).shortValue());
        } else if (object instanceof Integer) {
            return Json.value(((Integer) object).intValue());
        } else if (object instanceof Long) {
            return Json.value(((Long) object).longValue());
        } else if (object instanceof Float) {
            return Json.value(((Float) object).floatValue());
        } else if (object instanceof Double) {
            return Json.value(((Double) object).doubleValue());
        } else if (object.getClass().isArray()) {
            return JsonBuilder.arrayToJsonValue(object);
        } else if (object instanceof Iterable) {
            return JsonBuilder.iterableToJsonValue((Iterable<?>) object);
        } else if (object instanceof Map) {
            return JsonBuilder.mapToJsonValue((Map<?, ?>) object);
        } else {
            return Json.value(String.valueOf(object));
        }
    }

    static JsonArray iterableToJsonValue(final Iterable<?> collection) {
        final JsonArray array = new JsonArray();
        for (final Object element : collection) {
            array.add(JsonBuilder.toJsonValue(element));
        }
        return array;
    }

    static JsonObject mapToJsonValue(final Map<?, ?> map) {
        final JsonObject object = new JsonObject();
        for (final Map.Entry<?, ?> entry : map.entrySet()) {
            object.add(String.valueOf(entry.getKey()), JsonBuilder.toJsonValue(entry.getValue()));
        }
        return object;
    }

    static JsonValue arrayToJsonValue(final Object inputArray) {
        final JsonArray array = new JsonArray();
        final int arrayLength = Array.getLength(inputArray);
        for (int i = 0; i < arrayLength; i++) {
            final Object element = Array.get(inputArray, i);
            array.add(JsonBuilder.toJsonValue(element));
        }
        return array;
    }

}