/*******************************************************************************
 * Copyright (c) 2013, 2015 EclipseSource.
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
package io.github.portlek.configs.json.minimaljson;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial") // use default serial UID
public class JsonArray extends JsonValue implements Iterable<JsonValue> {

    public static final String ARRAY_IS_NULL = "array is null";

    private final List<JsonValue> values;

    public JsonArray() {
        this.values = new ArrayList<JsonValue>();
    }

    public JsonArray(final JsonArray array) {
        this(array, false);
    }

    private JsonArray(final JsonArray array, final boolean unmodifiable) {
        if (array == null) {
            throw new NullPointerException(JsonArray.ARRAY_IS_NULL);
        }
        if (unmodifiable) {
            this.values = Collections.unmodifiableList(array.values);
        } else {
            this.values = new ArrayList<JsonValue>(array.values);
        }
    }

    @Deprecated
    public static JsonArray readFrom(final Reader reader) throws IOException {
        return JsonValue.readFrom(reader).asArray();
    }

    @Deprecated
    public static JsonArray readFrom(final String string) {
        return JsonValue.readFrom(string).asArray();
    }

    public static JsonArray unmodifiableArray(final JsonArray array) {
        return new JsonArray(array, true);
    }

    public JsonArray add(final int value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final long value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final float value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final double value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final boolean value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final String value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(final JsonValue value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.values.add(value);
        return this;
    }

    public JsonArray set(final int index, final int value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final long value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final float value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final double value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final boolean value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final String value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(final int index, final JsonValue value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.values.set(index, value);
        return this;
    }

    public JsonArray remove(final int index) {
        this.values.remove(index);
        return this;
    }

    public int size() {
        return this.values.size();
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public JsonValue get(final int index) {
        return this.values.get(index);
    }

    public List<JsonValue> values() {
        return Collections.unmodifiableList(this.values);
    }

    @Override
    public Iterator<JsonValue> iterator() {
        final Iterator<JsonValue> iterator = this.values.iterator();
        return new Iterator<JsonValue>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonValue next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public JsonArray asArray() {
        return this;
    }

    @Override
    public int hashCode() {
        return this.values.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        final JsonArray other = (JsonArray) object;
        return this.values.equals(other.values);
    }

    @Override
    void write(final JsonWriter writer) throws IOException {
        writer.writeArrayOpen();
        final Iterator<JsonValue> iterator = this.iterator();
        if (iterator.hasNext()) {
            iterator.next().write(writer);
            while (iterator.hasNext()) {
                writer.writeArraySeparator();
                iterator.next().write(writer);
            }
        }
        writer.writeArrayClose();
    }

}
