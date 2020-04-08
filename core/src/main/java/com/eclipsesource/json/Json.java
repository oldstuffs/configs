/*******************************************************************************
 * Copyright (c) 2015, 2016 EclipseSource.
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
 ******************************************************************************/
package com.eclipsesource.json;

import java.io.IOException;
import java.io.Reader;

/**
 * This class serves as the entry point to the minimal-json API.
 * <p>
 * To <strong>parse</strong> a given JSON input, use the <code>parse()</code> methods like in this
 * example:
 * </p>
 * <pre>
 * JsonObject object = Json.parse(string).asObject();
 * </pre>
 * <p>
 * To <strong>create</strong> a JSON data structure to be serialized, use the methods
 * <code>value()</code>, <code>array()</code>, and <code>object()</code>. For example, the following
 * snippet will produce the JSON string <em>{"foo": 23, "bar": true}</em>:
 * </p>
 * <pre>
 * String string = Json.object().add("foo", 23).add("bar", true).toString();
 * </pre>
 * <p>
 * To create a JSON array from a given Java array, you can use one of the <code>array()</code>
 * methods with varargs parameters:
 * </p>
 * <pre>
 * String[] names = ...
 * JsonArray array = Json.array(names);
 * </pre>
 */
public final class Json {

    /**
     * Represents the JSON literal <code>null</code>.
     */
    public static final JsonValue NULL = new JsonLiteral("null");

    /**
     * Represents the JSON literal <code>true</code>.
     */
    public static final JsonValue TRUE = new JsonLiteral("true");

    /**
     * Represents the JSON literal <code>false</code>.
     */
    public static final JsonValue FALSE = new JsonLiteral("false");

    private Json() {
        // not meant to be instantiated
    }

    /**
     * Returns a JsonValue instance that represents the given <code>int</code> value.
     *
     * @param value the value to get a JSON representation for
     * @return a JSON value that represents the given value
     */
    public static JsonValue value(final int value) {
        return new JsonNumber(Integer.toString(value, 10));
    }

    /**
     * Returns a JsonValue instance that represents the given <code>long</code> value.
     *
     * @param value the value to get a JSON representation for
     * @return a JSON value that represents the given value
     */
    public static JsonValue value(final long value) {
        return new JsonNumber(Long.toString(value, 10));
    }

    /**
     * Returns a JsonValue instance that represents the given <code>float</code> value.
     *
     * @param value the value to get a JSON representation for
     * @return a JSON value that represents the given value
     */
    public static JsonValue value(final float value) {
        if (Float.isInfinite(value) || Float.isNaN(value)) {
            throw new IllegalArgumentException("Infinite and NaN values not permitted in JSON");
        }
        return new JsonNumber(Float.toString(value));
    }

    /**
     * Returns a JsonValue instance that represents the given <code>double</code> value.
     *
     * @param value the value to get a JSON representation for
     * @return a JSON    value that represents the given value
     */
    public static JsonValue value(final double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            throw new IllegalArgumentException("Infinite and NaN values not permitted in JSON");
        }
        return new JsonNumber(Double.toString(value));
    }

    /**
     * Returns a JsonValue instance that represents the given string.
     *
     * @param string the string to get a JSON representation for
     * @return a JSON value that represents the given string
     */
    public static JsonValue value(final String string) {
        return string == null ? Json.NULL : new JsonString(string);
    }

    /**
     * Returns a JsonValue instance that represents the given <code>boolean</code> value.
     *
     * @param value the value to get a JSON representation for
     * @return a JSON value that represents the given value
     */
    public static JsonValue value(final boolean value) {
        return value ? Json.TRUE : Json.FALSE;
    }

    /**
     * Parses the given input string as JSON. The input must contain a valid JSON value, optionally
     * padded with whitespace.
     *
     * @param string the input string, must be valid JSON
     * @return a value that represents the parsed JSON
     * @throws ParseException if the input is not valid JSON
     */
    public static JsonValue parse(final String string) {
        if (string == null) {
            throw new NullPointerException("string is null");
        }
        final Json.DefaultHandler handler = new Json.DefaultHandler();
        new JsonParser(handler).parse(string);
        return handler.getValue();
    }

    /**
     * Reads the entire input from the given reader and parses it as JSON. The input must contain a
     * valid JSON value, optionally padded with whitespace.
     * <p>
     * Characters are read in chunks into an input buffer. Hence, wrapping a reader in an additional
     * <code>BufferedReader</code> likely won't improve reading performance.
     * </p>
     *
     * @param reader the reader to read the JSON value from
     * @return a value that represents the parsed JSON
     * @throws IOException if an I/O error occurs in the reader
     * @throws ParseException if the input is not valid JSON
     */
    public static JsonValue parse(final Reader reader) throws IOException {
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        final Json.DefaultHandler handler = new Json.DefaultHandler();
        new JsonParser(handler).parse(reader);
        return handler.getValue();
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
