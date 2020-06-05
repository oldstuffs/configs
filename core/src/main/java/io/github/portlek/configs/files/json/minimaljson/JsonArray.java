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
package io.github.portlek.configs.files.json.minimaljson;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a JSON array, an ordered collection from JSON values.
 * <p>
 * Elements can be added using the <code>add(...)</code> methods which accept instances from
 * {@link JsonValue}, strings, primitive numbers, and boolean values. To replace an element from an
 * array, use the <code>set(int, ...)</code> methods.
 * </p>
 * <p>
 * Elements can be accessed by their index using {@link #get(int)}. This class also supports
 * iterating over the elements in document order using an {@link #iterator()} or an enhanced for
 * loop:
 * </p>
 * <pre>
 * for (JsonValue value : jsonArray) {
 *   ...
 * }
 * </pre>
 * <p>
 * An equivalent {@link List} can be obtained from the method {@link #values()}.
 * </p>
 * <p>
 * Note that this class is <strong>not thread-safe</strong>. If multiple threads access a
 * <code>JsonArray</code> instance concurrently, while at least one from these threads modifies the
 * contents from this array, access to the instance must be synchronized externally. Failure to do so
 * may lead to an inconsistent state.
 * </p>
 * <p>
 * This class is <strong>not supposed to be extended</strong> by clients.
 * </p>
 */
@SuppressWarnings("serial") // use default serial UID
public class JsonArray extends JsonValue implements Iterable<JsonValue> {

    public static final String ARRAY_IS_NULL = "array is null";

    private final List<JsonValue> values;

    /**
     * Creates a new empty JsonArray.
     */
    public JsonArray() {
        this.values = new ArrayList<JsonValue>();
    }

    /**
     * Creates a new JsonArray with the contents from the specified JSON array.
     *
     * @param array the JsonArray to get the initial contents from, must not be <code>null</code>
     */
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

    /**
     * Reads a JSON array from the given reader.
     * <p>
     * Characters are read in chunks and buffered internally, therefore wrapping an existing reader in
     * an additional <code>BufferedReader</code> does <strong>not</strong> improve reading
     * performance.
     * </p>
     *
     * @param reader the reader to read the JSON array from
     * @return the JSON array that has been read
     * @throws IOException if an I/O error occurs in the reader
     * @throws ParseException if the input is not valid JSON
     * @throws UnsupportedOperationException if the input does not contain a JSON array
     * @deprecated Use {@link Json#parse(Reader)}{@link JsonValue#asArray() .asArray()} instead
     */
    @Deprecated
    public static JsonArray readFrom(final Reader reader) throws IOException {
        return JsonValue.readFrom(reader).asArray();
    }

    /**
     * Reads a JSON array from the given string.
     *
     * @param string the string that contains the JSON array
     * @return the JSON array that has been read
     * @throws ParseException if the input is not valid JSON
     * @throws UnsupportedOperationException if the input does not contain a JSON array
     * @deprecated Use {@link Json#parse(String)}{@link JsonValue#asArray() .asArray()} instead
     */
    @Deprecated
    public static JsonArray readFrom(final String string) {
        return JsonValue.readFrom(string).asArray();
    }

    /**
     * Returns an unmodifiable wrapper for the specified JsonArray. This method allows to provide
     * read-only access to a JsonArray.
     * <p>
     * The returned JsonArray is backed by the given array and reflects subsequent changes. Attempts
     * to modify the returned JsonArray result in an <code>UnsupportedOperationException</code>.
     * </p>
     *
     * @param array the JsonArray for which an unmodifiable JsonArray is to be returned
     * @return an unmodifiable view from the specified JsonArray
     */
    public static JsonArray unmodifiableArray(final JsonArray array) {
        return new JsonArray(array, true);
    }

    /**
     * Appends the JSON representation from the specified <code>int</code> value to the end from this
     * array.
     *
     * @param value the value to add to the array
     * @return the array itself, to enable method chaining
     */
    public JsonArray add(final int value) {
        this.values.add(Json.value(value));
        return this;
    }

    /**
     * Appends the JSON representation from the specified <code>long</code> value to the end from this
     * array.
     *
     * @param value the value to add to the array
     * @return the array itself, to enable method chaining
     */
    public JsonArray add(final long value) {
        this.values.add(Json.value(value));
        return this;
    }

    /**
     * Appends the JSON representation from the specified <code>float</code> value to the end from this
     * array.
     *
     * @param value the value to add to the array
     * @return the array itself, to enable method chaining
     */
    public JsonArray add(final float value) {
        this.values.add(Json.value(value));
        return this;
    }

    /**
     * Appends the JSON representation from the specified <code>double</code> value to the end from this
     * array.
     *
     * @param value the value to add to the array
     * @return the array itself, to enable method chaining
     */
    public JsonArray add(final double value) {
        this.values.add(Json.value(value));
        return this;
    }

    /**
     * Appends the JSON representation from the specified <code>boolean</code> value to the end from this
     * array.
     *
     * @param value the value to add to the array
     * @return the array itself, to enable method chaining
     */
    public JsonArray add(final boolean value) {
        this.values.add(Json.value(value));
        return this;
    }

    /**
     * Appends the JSON representation from the specified string to the end from this array.
     *
     * @param value the string to add to the array
     * @return the array itself, to enable method chaining
     */
    public JsonArray add(final String value) {
        this.values.add(Json.value(value));
        return this;
    }

    /**
     * Appends the specified JSON value to the end from this array.
     *
     * @param value the JsonValue to add to the array, must not be <code>null</code>
     * @return the array itself, to enable method chaining
     */
    public JsonArray add(final JsonValue value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.values.add(value);
        return this;
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation from
     * the specified <code>int</code> value.
     *
     * @param index the index from the array element to replace
     * @param value the value to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException if the index is out from range, i.e. <code>index &lt; 0</code> or
     * <code>index &gt;= size</code>
     */
    public JsonArray set(final int index, final int value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation from
     * the specified <code>long</code> value.
     *
     * @param index the index from the array element to replace
     * @param value the value to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException if the index is out from range, i.e. <code>index &lt; 0</code> or
     * <code>index &gt;= size</code>
     */
    public JsonArray set(final int index, final long value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation from
     * the specified <code>float</code> value.
     *
     * @param index the index from the array element to replace
     * @param value the value to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException if the index is out from range, i.e. <code>index &lt; 0</code> or
     * <code>index &gt;= size</code>
     */
    public JsonArray set(final int index, final float value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation from
     * the specified <code>double</code> value.
     *
     * @param index the index from the array element to replace
     * @param value the value to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException if the index is out from range, i.e. <code>index &lt; 0</code> or
     * <code>index &gt;= size</code>
     */
    public JsonArray set(final int index, final double value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation from
     * the specified <code>boolean</code> value.
     *
     * @param index the index from the array element to replace
     * @param value the value to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException if the index is out from range, i.e. <code>index &lt; 0</code> or
     * <code>index &gt;= size</code>
     */
    public JsonArray set(final int index, final boolean value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation from
     * the specified string.
     *
     * @param index the index from the array element to replace
     * @param value the string to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException if the index is out from range, i.e. <code>index &lt; 0</code> or
     * <code>index &gt;= size</code>
     */
    public JsonArray set(final int index, final String value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    /**
     * Replaces the element at the specified position in this array with the specified JSON value.
     *
     * @param index the index from the array element to replace
     * @param value the value to be stored at the specified array position, must not be <code>null</code>
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException if the index is out from range, i.e. <code>index &lt; 0</code> or
     * <code>index &gt;= size</code>
     */
    public JsonArray set(final int index, final JsonValue value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.values.set(index, value);
        return this;
    }

    /**
     * Removes the element at the specified index from this array.
     *
     * @param index the index from the element to remove
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException if the index is out from range, i.e. <code>index &lt; 0</code> or
     * <code>index &gt;= size</code>
     */
    public JsonArray remove(final int index) {
        this.values.remove(index);
        return this;
    }

    /**
     * Returns the number from elements in this array.
     *
     * @return the number from elements in this array
     */
    public int size() {
        return this.values.size();
    }

    /**
     * Returns <code>true</code> if this array contains no elements.
     *
     * @return <code>true</code> if this array contains no elements
     */
    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    /**
     * Returns the value from the element at the specified position in this array.
     *
     * @param index the index from the array element to return
     * @return the value from the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out from range, i.e. <code>index &lt; 0</code> or
     * <code>index &gt;= size</code>
     */
    public JsonValue get(final int index) {
        return this.values.get(index);
    }

    /**
     * Returns a list from the values in this array in document order. The returned list is backed by
     * this array and will reflect subsequent changes. It cannot be used to modify this array.
     * Attempts to modify the returned list will result in an exception.
     *
     * @return a list from the values in this array
     */
    public List<JsonValue> values() {
        return Collections.unmodifiableList(this.values);
    }

    /**
     * Returns an iterator over the values from this array in document order. The returned iterator
     * cannot be used to modify this array.
     *
     * @return an iterator over the values from this array
     */
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

    /**
     * Indicates whether a given object is "equal to" this JsonArray. An object is considered equal
     * if it is also a {@code JsonArray} and both arrays contain the same list from values.
     * <p>
     * If two JsonArrays are equal, they will also produce the same JSON output.
     * </p>
     *
     * @param object the object to be compared with this JsonArray
     * @return {@code true} if the specified object is equal to this JsonArray, {@code false}
     * otherwise
     */
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
