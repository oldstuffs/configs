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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a JSON array, an ordered collection of JSON values.
 * <p>
 * Elements can be added using the {@code add(...)} methods which accept instances of
 * {@link JsonValue}, strings, primitive numbers, and boolean values. To replace an element of an
 * array, use the {@code set(int, ...)} methods.
 * </p>
 * <p>
 * This class also supports
 * iterating over the elements in document order using an {@link #iterator()} or an enhanced for
 * loop:
 * </p>
 * <pre>
 * for (JsonValue value : jsonArray) {
 *   ...
 * }
 * </pre>
 * <p>
 * An equivalent {@link List} can be obtained from the method {@link #values}.
 * </p>
 * <p>
 * Note that this class is <strong>not thread-safe</strong>. If multiple threads access a
 * {@code JsonArray} instance concurrently, while at least one of these threads modifies the
 * contents of this array, access to the instance must be synchronized externally. Failure to do so
 * may lead to an inconsistent state.
 * </p>
 * <p>
 * This class is <strong>not supposed to be extended</strong> by clients.
 * </p>
 */
@SuppressWarnings("serial") // use default serial UID
public class JsonArray extends JsonValue implements Iterable<JsonValue> {

    private final Collection<JsonValue> values = new ArrayList<>();

    /**
     * Appends the specified JSON value to the end of this array.
     *
     * @param value the JsonValue to add to the array, must not be {@code null}
     * @return the array itself, to enable method chaining
     */
    public final JsonArray add(final JsonValue value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.values.add(value);
        return this;
    }

    /**
     * Returns the number of elements in this array.
     *
     * @return the number of elements in this array
     */
    public final int size() {
        return this.values.size();
    }

    @Override
    public final boolean isArray() {
        return true;
    }

    @Override
    public final JsonArray asArray() {
        return this;
    }

    @Override
    public final int hashCode() {
        return this.values.hashCode();
    }

    /**
     * Indicates whether a given object is "equal to" this JsonArray. An object is considered equal
     * if it is also a {@code JsonArray} and both arrays contain the same list of values.
     * <p>
     * If two JsonArrays are equal, they will also produce the same JSON output.
     * </p>
     *
     * @param object the object to be compared with this JsonArray
     * @return true if the specified object is equal to this JsonArray, {@code false}
     * otherwise
     */
    @Override
    public final boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!this.getClass().equals(object.getClass())) {
            return false;
        }
        final JsonArray other = (JsonArray) object;
        return this.values.equals(other.values);
    }

    @Override
    final void write(final JsonWriter writer) throws IOException {
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

    /**
     * Returns an iterator over the values of this array in document order. The returned iterator
     * cannot be used to modify this array.
     *
     * @return an iterator over the values of this array
     */
    @Override
    public final Iterator<JsonValue> iterator() {
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

}
