/*******************************************************************************
 * Copyright (c) 2013, 2017 EclipseSource.
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

import java.io.*;

@SuppressWarnings("serial") // use default serial UID
public abstract class JsonValue implements Serializable {

    public static final String NOT_AN_OBJECT = "Not an object: ";

    public static final String NOT_AN_ARRAY = "Not an array: ";

    public static final String NOT_A_NUMBER = "Not a number: ";

    public static final String NOT_A_STRING = "Not a string: ";

    public static final String NOT_A_BOOLEAN = "Not a boolean: ";

    JsonValue() {
        // prevent subclasses outside from this package
    }

    @Deprecated
    public static JsonValue readFrom(final Reader reader) throws IOException {
        return Json.parse(reader);
    }

    @Deprecated
    public static JsonValue readFrom(final String text) {
        return Json.parse(text);
    }

    @Deprecated
    public static JsonValue valueOf(final int value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(final long value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(final float value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(final double value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(final String string) {
        return Json.value(string);
    }

    @Deprecated
    public static JsonValue valueOf(final boolean value) {
        return Json.value(value);
    }

    public boolean isObject() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isTrue() {
        return false;
    }

    public boolean isFalse() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public JsonObject asObject() {
        throw new UnsupportedOperationException(JsonValue.NOT_AN_OBJECT + this.toString());
    }

    public JsonArray asArray() {
        throw new UnsupportedOperationException(JsonValue.NOT_AN_ARRAY + this.toString());
    }

    public int asInt() {
        throw new UnsupportedOperationException(JsonValue.NOT_A_NUMBER + this.toString());
    }

    public long asLong() {
        throw new UnsupportedOperationException(JsonValue.NOT_A_NUMBER + this.toString());
    }

    public float asFloat() {
        throw new UnsupportedOperationException(JsonValue.NOT_A_NUMBER + this.toString());
    }

    public double asDouble() {
        throw new UnsupportedOperationException(JsonValue.NOT_A_NUMBER + this.toString());
    }

    public String asString() {
        throw new UnsupportedOperationException(JsonValue.NOT_A_STRING + this.toString());
    }

    public boolean asBoolean() {
        throw new UnsupportedOperationException(JsonValue.NOT_A_BOOLEAN + this.toString());
    }

    public void writeTo(final Writer writer) throws IOException {
        this.writeTo(writer, WriterConfig.MINIMAL);
    }

    public void writeTo(final Writer writer, final WriterConfig config) throws IOException {
        if (writer == null) {
            throw new NullPointerException("writer is null");
        }
        if (config == null) {
            throw new NullPointerException("config is null");
        }
        final WritingBuffer buffer = new WritingBuffer(writer, 128);
        this.write(config.createWriter(buffer));
        buffer.flush();
    }

    public String toString(final WriterConfig config) {
        final StringWriter writer = new StringWriter();
        try {
            this.writeTo(writer, config);
        } catch (final IOException exception) {
            // StringWriter does not throw IOExceptions
            throw new RuntimeException(exception);
        }
        return writer.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        return super.equals(object);
    }

    @Override
    public String toString() {
        return this.toString(WriterConfig.MINIMAL);
    }

    abstract void write(JsonWriter writer) throws IOException;

}
