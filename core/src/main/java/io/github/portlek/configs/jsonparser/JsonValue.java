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
package io.github.portlek.configs.jsonparser;

import java.io.*;

/**
 * Represents a JSON value. This can be a JSON <strong>object</strong>, an <strong> array</strong>,
 * a <strong>number</strong>, a <strong>string</strong>, or one of the literals
 * <strong>true</strong>, <strong>false</strong>, and <strong>null</strong>.
 * <p>
 * The literals <strong>true</strong>, <strong>false</strong>, and <strong>null</strong> are
 * represented by the constants {@link Json#TRUE}, {@link Json#FALSE}, and {@link Json#NULL}.
 * </p>
 * <p>
 * JSON <strong>objects</strong> and <strong>arrays</strong> are represented by the subtypes
 * {@link JsonObject} and {@link JsonArray}. Instances of these types can be created using the
 * public constructors of these classes.
 * </p>
 * <p>
 * Instances that represent JSON <strong>numbers</strong>, <strong>strings</strong> and
 * <strong>boolean</strong> values can be created using the static factory methods
 * {@link Json#value(String)}, {@link Json#value(long)}, {@link Json#value(double)}, etc.
 * </p>
 * <p>
 * In order to find out whether an instance of this class is of a certain type, the methods
 * {@link #isObject()}, {@link #isArray()}, {@link #isString()}, {@link #isNumber()} etc. can be
 * used.
 * </p>
 * <p>
 * If the type of a JSON value is known, the methods {@link #asObject()}, {@link #asArray()},
 * {@link #asString()}, {@link #asInt()}, etc. can be used to get this value directly in the
 * appropriate target type.
 * </p>
 * <p>
 * This class is <strong>not supposed to be extended</strong> by clients.
 * </p>
 */
@SuppressWarnings("serial") // use default serial UID
public abstract class JsonValue implements Serializable {

    /**
     * Represents the JSON literal {@code true}.
     *
     * @deprecated Use {@code Json.TRUE} instead
     */
    @Deprecated
    public static final JsonValue TRUE = new JsonLiteral("true");

    /**
     * Represents the JSON literal {@code false}.
     *
     * @deprecated Use {@code Json.FALSE} instead
     */
    @Deprecated
    public static final JsonValue FALSE = new JsonLiteral("false");

    /**
     * Represents the JSON literal {@code null}.
     *
     * @deprecated Use {@code Json.NULL} instead
     */
    @Deprecated
    public static final JsonValue NULL = new JsonLiteral("null");

    JsonValue() {
        // prevent subclasses outside of this package
    }

    /**
     * Reads a JSON value from the given reader.
     * <p>
     * Characters are read in chunks and buffered internally, therefore wrapping an existing reader in
     * an additional {@code BufferedReader} does <strong>not</strong> improve reading
     * performance.
     * </p>
     *
     * @param reader the reader to read the JSON value from
     * @return the JSON value that has been read
     * @throws IOException if an I/O error occurs in the reader
     * @throws ParseException if the input is not valid JSON
     * @deprecated Use {@link Json#parse(Reader)} instead
     */
    @Deprecated
    public static JsonValue readFrom(final Reader reader) throws IOException {
        return Json.parse(reader);
    }

    /**
     * Reads a JSON value from the given string.
     *
     * @param text the string that contains the JSON value
     * @return the JSON value that has been read
     * @throws ParseException if the input is not valid JSON
     * @deprecated Use {@link Json#parse(String)} instead
     */
    @Deprecated
    public static JsonValue readFrom(final String text) {
        return Json.parse(text);
    }

    /**
     * Returns a JsonValue instance that represents the given {@code int} value.
     *
     * @param value the value to get a JSON representation for
     * @return a JSON value that represents the given value
     * @deprecated Use {@code Json.value()} instead
     */
    @Deprecated
    public static JsonValue valueOf(final int value) {
        return Json.value(value);
    }

    /**
     * Returns a JsonValue instance that represents the given {@code long} value.
     *
     * @param value the value to get a JSON representation for
     * @return a JSON value that represents the given value
     * @deprecated Use {@code Json.value()} instead
     */
    @Deprecated
    public static JsonValue valueOf(final long value) {
        return Json.value(value);
    }

    /**
     * Returns a JsonValue instance that represents the given {@code float} value.
     *
     * @param value the value to get a JSON representation for
     * @return a JSON value that represents the given value
     * @deprecated Use {@code Json.value()} instead
     */
    @Deprecated
    public static JsonValue valueOf(final float value) {
        return Json.value(value);
    }

    /**
     * Returns a JsonValue instance that represents the given {@code double} value.
     *
     * @param value the value to get a JSON representation for
     * @return a JSON value that represents the given value
     * @deprecated Use {@code Json.value()} instead
     */
    @Deprecated
    public static JsonValue valueOf(final double value) {
        return Json.value(value);
    }

    /**
     * Returns a JsonValue instance that represents the given string.
     *
     * @param string the string to get a JSON representation for
     * @return a JSON value that represents the given string
     * @deprecated Use {@code Json.value()} instead
     */
    @Deprecated
    public static JsonValue valueOf(final String string) {
        return Json.value(string);
    }

    /**
     * Returns a JsonValue instance that represents the given {@code boolean} value.
     *
     * @param value the value to get a JSON representation for
     * @return a JSON value that represents the given value
     * @deprecated Use {@code Json.value()} instead
     */
    @Deprecated
    public static JsonValue valueOf(final boolean value) {
        return Json.value(value);
    }

    /**
     * Detects whether this value represents a JSON object. If this is the case, this value is an
     * instance of {@link JsonObject}.
     *
     * @return {@code true} if this value is an instance of JsonObject
     */
    public boolean isObject() {
        return false;
    }

    /**
     * Detects whether this value represents a JSON array. If this is the case, this value is an
     * instance of {@link JsonArray}.
     *
     * @return {@code true} if this value is an instance of JsonArray
     */
    public boolean isArray() {
        return false;
    }

    /**
     * Detects whether this value represents a JSON number.
     *
     * @return {@code true} if this value represents a JSON number
     */
    public boolean isNumber() {
        return false;
    }

    /**
     * Detects whether this value represents a JSON string.
     *
     * @return {@code true} if this value represents a JSON string
     */
    public boolean isString() {
        return false;
    }

    /**
     * Detects whether this value represents a boolean value.
     *
     * @return {@code true} if this value represents either the JSON literal {@code true} or
     * {@code false}
     */
    public boolean isBoolean() {
        return false;
    }

    /**
     * Detects whether this value represents the JSON literal {@code true}.
     *
     * @return {@code true} if this value represents the JSON literal {@code true}
     */
    public boolean isTrue() {
        return false;
    }

    /**
     * Detects whether this value represents the JSON literal {@code false}.
     *
     * @return {@code true} if this value represents the JSON literal {@code false}
     */
    public boolean isFalse() {
        return false;
    }

    /**
     * Detects whether this value represents the JSON literal {@code null}.
     *
     * @return {@code true} if this value represents the JSON literal {@code null}
     */
    public boolean isNull() {
        return false;
    }

    /**
     * Returns this JSON value as {@link JsonObject}, assuming that this value represents a JSON
     * object. If this is not the case, an exception is thrown.
     *
     * @return a JSONObject for this value
     * @throws UnsupportedOperationException if this value is not a JSON object
     */
    public JsonObject asObject() {
        throw new UnsupportedOperationException("Not an object: " + this.toString());
    }

    /**
     * Returns the JSON string for this value using the given formatting.
     *
     * @param config a configuration that controls the formatting or {@code null} for the minimal form
     * @return a JSON string that represents this value
     */
    public final String toString(final WriterConfig config) {
        final StringWriter writer = new StringWriter();
        try {
            this.writeTo(writer, config);
        } catch (final IOException exception) {
            // StringWriter does not throw IOExceptions
            throw new RuntimeException(exception);
        }
        return writer.toString();
    }

    /**
     * Writes the JSON representation of this value to the given writer using the given formatting.
     * <p>
     * Writing performance can be improved by using a {@link BufferedWriter BufferedWriter}.
     * </p>
     *
     * @param writer the writer to write this value to
     * @param config a configuration that controls the formatting or {@code null} for the minimal form
     * @throws IOException if an I/O error occurs in the writer
     */
    public final void writeTo(final Writer writer, final WriterConfig config) throws IOException {
        if (writer == null) {
            throw new NullPointerException("writer is null");
        }
        if (config == null) {
            throw new NullPointerException("config is null");
        }
        final WritingBuffer buffer = new WritingBuffer(writer);
        this.write(config.createWriter(buffer));
        buffer.flush();
    }

    /**
     * Returns this JSON value as {@link JsonArray}, assuming that this value represents a JSON array.
     * If this is not the case, an exception is thrown.
     *
     * @return a JSONArray for this value
     * @throws UnsupportedOperationException if this value is not a JSON array
     */
    public JsonArray asArray() {
        throw new UnsupportedOperationException("Not an array: " + this.toString());
    }

    /**
     * Returns this JSON value as an {@code int} value, assuming that this value represents a
     * JSON number that can be interpreted as Java {@code int}. If this is not the case, an
     * exception is thrown.
     * <p>
     * To be interpreted as Java {@code int}, the JSON number must neither contain an exponent
     * nor a fraction part. Moreover, the number must be in the {@code Integer} range.
     * </p>
     *
     * @return this value as {@code int}
     * @throws UnsupportedOperationException if this value is not a JSON number
     * @throws NumberFormatException if this JSON number can not be interpreted as {@code int} value
     */
    public int asInt() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    /**
     * Returns this JSON value as a {@code long} value, assuming that this value represents a
     * JSON number that can be interpreted as Java {@code long}. If this is not the case, an
     * exception is thrown.
     * <p>
     * To be interpreted as Java {@code long}, the JSON number must neither contain an exponent
     * nor a fraction part. Moreover, the number must be in the {@code Long} range.
     * </p>
     *
     * @return this value as {@code long}
     * @throws UnsupportedOperationException if this value is not a JSON number
     * @throws NumberFormatException if this JSON number can not be interpreted as {@code long} value
     */
    public long asLong() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    /**
     * Returns this JSON value as a {@code double} value, assuming that this value represents a
     * JSON number. If this is not the case, an exception is thrown.
     * <p>
     * If the JSON number is out of the {@code Double} range, {@link Double#POSITIVE_INFINITY} or
     * {@link Double#NEGATIVE_INFINITY} is returned.
     * </p>
     *
     * @return this value as {@code double}
     * @throws UnsupportedOperationException if this value is not a JSON number
     */
    public double asDouble() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    /**
     * Returns this JSON value as String, assuming that this value represents a JSON string. If this
     * is not the case, an exception is thrown.
     *
     * @return the string represented by this value
     * @throws UnsupportedOperationException if this value is not a JSON string
     */
    public String asString() {
        throw new UnsupportedOperationException("Not a string: " + this.toString());
    }

    /**
     * Returns this JSON value as a {@code boolean} value, assuming that this value is either
     * {@code true} or {@code false}. If this is not the case, an exception is thrown.
     *
     * @return this value as {@code boolean}
     * @throws UnsupportedOperationException if this value is neither {@code true} or {@code false}
     */
    public boolean asBoolean() {
        throw new UnsupportedOperationException("Not a boolean: " + this.toString());
    }

    /**
     * Returns the JSON string for this value in its minimal form, without any additional whitespace.
     * The result is guaranteed to be a valid input for the method {@link Json#parse(String)} and to
     * create a value that is <em>equal</em> to this object.
     *
     * @return a JSON string that represents this value
     */
    @Override
    public String toString() {
        return this.toString(WriterConfig.MINIMAL);
    }

    abstract void write(JsonWriter writer) throws IOException;

}
