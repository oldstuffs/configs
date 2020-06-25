/*******************************************************************************
 * Copyright (c) 2015, 2016 EclipseSource.
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
 ******************************************************************************/
package io.github.portlek.configs.files.json.minimaljson;

import java.io.IOException;
import java.io.Reader;

public final class Json {

    public static final JsonValue NULL = new JsonLiteral("null");

    public static final JsonValue TRUE = new JsonLiteral("true");

    public static final JsonValue FALSE = new JsonLiteral("false");

    public static final String INFINITE_AND_NAN = "Infinite and NaN values not permitted in JSON";

    public static final String VALUES_IS_NULL = "values is null";

    public static final String STRING_IS_NULL = "string is null";

    public static final String READER_IS_NULL = "reader is null";

    private Json() {
        // not meant to be instantiated
    }

    public static JsonValue value(final Object object) {
        return JsonBuilder.toJsonValue(object);
    }

    public static JsonValue value(final int value) {
        return new JsonNumber(Integer.toString(value, 10));
    }

    public static JsonValue value(final long value) {
        return new JsonNumber(Long.toString(value, 10));
    }

    public static JsonValue value(final float value) {
        if (Float.isInfinite(value) || Float.isNaN(value)) {
            throw new IllegalArgumentException(Json.INFINITE_AND_NAN);
        }
        return new JsonNumber(Json.cutOffPointZero(Float.toString(value)));
    }

    public static JsonValue value(final double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            throw new IllegalArgumentException(Json.INFINITE_AND_NAN);
        }
        return new JsonNumber(Json.cutOffPointZero(Double.toString(value)));
    }

    public static JsonValue value(final String string) {
        return string == null ? Json.NULL : new JsonString(string);
    }

    public static JsonValue value(final boolean value) {
        return value ? Json.TRUE : Json.FALSE;
    }

    public static JsonArray array() {
        return new JsonArray();
    }

    public static JsonArray array(final int... values) {
        if (values == null) {
            throw new NullPointerException(Json.VALUES_IS_NULL);
        }
        final JsonArray array = new JsonArray();
        for (final int value : values) {
            array.add(value);
        }
        return array;
    }

    public static JsonArray array(final long... values) {
        if (values == null) {
            throw new NullPointerException(Json.VALUES_IS_NULL);
        }
        final JsonArray array = new JsonArray();
        for (final long value : values) {
            array.add(value);
        }
        return array;
    }

    public static JsonArray array(final float... values) {
        if (values == null) {
            throw new NullPointerException(Json.VALUES_IS_NULL);
        }
        final JsonArray array = new JsonArray();
        for (final float value : values) {
            array.add(value);
        }
        return array;
    }

    public static JsonArray array(final double... values) {
        if (values == null) {
            throw new NullPointerException(Json.VALUES_IS_NULL);
        }
        final JsonArray array = new JsonArray();
        for (final double value : values) {
            array.add(value);
        }
        return array;
    }

    public static JsonArray array(final boolean... values) {
        if (values == null) {
            throw new NullPointerException(Json.VALUES_IS_NULL);
        }
        final JsonArray array = new JsonArray();
        for (final boolean value : values) {
            array.add(value);
        }
        return array;
    }

    public static JsonArray array(final String... strings) {
        if (strings == null) {
            throw new NullPointerException(Json.VALUES_IS_NULL);
        }
        final JsonArray array = new JsonArray();
        for (final String value : strings) {
            array.add(value);
        }
        return array;
    }

    public static JsonObject object() {
        return new JsonObject();
    }

    public static JsonValue parse(final String string) {
        if (string == null) {
            throw new NullPointerException(Json.STRING_IS_NULL);
        }
        final Json.DefaultHandler handler = new Json.DefaultHandler();
        new JsonParser(handler).parse(string);
        return handler.getValue();
    }

    public static JsonValue parse(final Reader reader) throws IOException {
        if (reader == null) {
            throw new NullPointerException(Json.READER_IS_NULL);
        }
        final Json.DefaultHandler handler = new Json.DefaultHandler();
        new JsonParser(handler).parse(reader);
        return handler.getValue();
    }

    private static String cutOffPointZero(final String string) {
        if (string.endsWith(".0")) {
            return string.substring(0, string.length() - 2);
        }
        return string;
    }

    static class DefaultHandler extends JsonHandler<JsonArray, JsonObject> {

        protected JsonValue value;

        @Override
        public void endNull() {
            this.value = Json.NULL;
        }

        @Override
        public void endBoolean(final boolean bool) {
            this.value = bool ? Json.TRUE : Json.FALSE;
        }

        @Override
        public void endString(final String string) {
            this.value = new JsonString(string);
        }

        @Override
        public void endNumber(final String string) {
            this.value = new JsonNumber(string);
        }

        @Override
        public JsonArray startArray() {
            return new JsonArray();
        }

        @Override
        public void endArray(final JsonArray array) {
            this.value = array;
        }

        @Override
        public void endArrayValue(final JsonArray array) {
            array.add(this.value);
        }

        @Override
        public JsonObject startObject() {
            return new JsonObject();
        }

        @Override
        public void endObject(final JsonObject object) {
            this.value = object;
        }

        @Override
        public void endObjectValue(final JsonObject object, final String name) {
            object.add(name, this.value);
        }

        JsonValue getValue() {
            return this.value;
        }

    }

}
