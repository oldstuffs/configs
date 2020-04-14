/*******************************************************************************
 * Copyright (c) 2013, 2015 EclipseSource.
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
import java.io.ObjectInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a JSON object, a set of name/value pairs, where the names are strings and the values
 * are JSON values.
 * <p>
 * Members can be added using the <code>add(String, ...)</code> methods which accept instances of
 * {@link JsonValue}, strings, primitive numbers, and boolean values. To modify certain values of an
 * object, use the <code>set(String, ...)</code> methods. Please note that the <code>add</code>
 * methods are faster than <code>set</code> as they do not search for existing members. On the other
 * hand, the <code>add</code> methods do not prevent adding multiple members with the same name.
 * Duplicate names are discouraged but not prohibited by JSON.
 * </p>
 * <p>
 * Members can be accessed by their name using {@link #get(String)}.
 * This class also supports iterating over the members in
 * document order using an {@link #iterator()} or an enhanced for loop:
 * </p>
 * <pre>
 * for (Member member : jsonObject) {
 *   String name = member.getName();
 *   JsonValue value = member.getValue();
 *   ...
 * }
 * </pre>
 * <p>
 * Even though JSON objects are unordered by definition, instances of this class preserve the order
 * of members to allow processing in document order and to guarantee a predictable output.
 * </p>
 * <p>
 * Note that this class is <strong>not thread-safe</strong>. If multiple threads access a
 * <code>JsonObject</code> instance concurrently, while at least one of these threads modifies the
 * contents of this object, access to the instance must be synchronized externally. Failure to do so
 * may lead to an inconsistent state.
 * </p>
 * <p>
 * This class is <strong>not supposed to be extended</strong> by clients.
 * </p>
 */
@SuppressWarnings("serial") // use default serial UID
public class JsonObject extends JsonValue implements Iterable<JsonObject.Member> {

    private final List<String> names;

    private final List<JsonValue> values;

    private transient JsonObject.HashIndexTable table;

    /**
     * Creates a new empty JsonObject.
     */
    public JsonObject() {
        this.names = new ArrayList<>();
        this.values = new ArrayList<>();
        this.table = new JsonObject.HashIndexTable();
    }

    private JsonObject(final JsonObject object, final boolean unmodifiable) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        if (unmodifiable) {
            this.names = Collections.unmodifiableList(object.names);
            this.values = Collections.unmodifiableList(object.values);
        } else {
            this.names = new ArrayList<String>(object.names);
            this.values = new ArrayList<JsonValue>(object.values);
        }
        this.table = new JsonObject.HashIndexTable();
        this.updateHashIndex();
    }

    private void updateHashIndex() {
        final int size = this.names.size();
        for (int i = 0; i < size; i++) {
            this.table.add(this.names.get(i), i);
        }
    }

    /**
     * Reads a JSON object from the given reader.
     * <p>
     * Characters are read in chunks and buffered internally, therefore wrapping an existing reader in
     * an additional <code>BufferedReader</code> does <strong>not</strong> improve reading
     * performance.
     * </p>
     *
     * @param reader the reader to read the JSON object from
     * @return the JSON object that has been read
     * @throws IOException if an I/O error occurs in the reader
     * @throws ParseException if the input is not valid JSON
     * @throws UnsupportedOperationException if the input does not contain a JSON object
     * @deprecated Use {@link Json#parse(Reader)}{@link JsonValue#asObject() .asObject()} instead
     */
    @Deprecated
    public static JsonObject readFrom(final Reader reader) throws IOException {
        return JsonValue.readFrom(reader).asObject();
    }

    /**
     * Reads a JSON object from the given string.
     *
     * @param string the string that contains the JSON object
     * @return the JSON object that has been read
     * @throws ParseException if the input is not valid JSON
     * @throws UnsupportedOperationException if the input does not contain a JSON object
     * @deprecated Use {@link Json#parse(String)}{@link JsonValue#asObject() .asObject()} instead
     */
    @Deprecated
    public static JsonObject readFrom(final String string) {
        return JsonValue.readFrom(string).asObject();
    }

    /**
     * Appends a new member to the end of this object, with the specified name and the specified JSON
     * value.
     * <p>
     * This method <strong>does not prevent duplicate names</strong>. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method <code>set(name, value)</code> instead. However,
     * <strong> <em>add</em> is much faster than <em>set</em></strong> (because it does not need to
     * search for existing members). Therefore <em>add</em> should be preferred when constructing new
     * objects.
     * </p>
     *
     * @param name the name of the member to add
     * @param value the value of the member to add, must not be <code>null</code>
     * @return the object itself, to enable method chaining
     */
    public JsonObject add(final String name, final JsonValue value) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.table.add(name, this.names.size());
        this.names.add(name);
        this.values.add(value);
        return this;
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified <code>int</code> value. If this object does not contain a member with this name, a
     * new member is added at the end of the object. If this object contains multiple members with
     * this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name of the member to replace
     * @param value the value to set to the member
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final int value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Sets the value of the member with the specified name to the specified JSON value. If this
     * object does not contain a member with this name, a new member is added at the end of the
     * object. If this object contains multiple members with this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name of the member to add
     * @param value the value of the member to add, must not be <code>null</code>
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final JsonValue value) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        final int index = this.indexOf(name);
        if (index != -1) {
            this.values.set(index, value);
        } else {
            this.table.add(name, this.names.size());
            this.names.add(name);
            this.values.add(value);
        }
        return this;
    }

    int indexOf(final String name) {
        final int index = this.table.get(name);
        if (index != -1 && name.equals(this.names.get(index))) {
            return index;
        }
        return this.names.lastIndexOf(name);
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified <code>long</code> value. If this object does not contain a member with this name, a
     * new member is added at the end of the object. If this object contains multiple members with
     * this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name of the member to replace
     * @param value the value to set to the member
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final long value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified <code>float</code> value. If this object does not contain a member with this name, a
     * new member is added at the end of the object. If this object contains multiple members with
     * this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name of the member to add
     * @param value the value of the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final float value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified <code>double</code> value. If this object does not contain a member with this name, a
     * new member is added at the end of the object. If this object contains multiple members with
     * this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name of the member to add
     * @param value the value of the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final double value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified <code>boolean</code> value. If this object does not contain a member with this name,
     * a new member is added at the end of the object. If this object contains multiple members with
     * this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name of the member to add
     * @param value the value of the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final boolean value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified string. If this object does not contain a member with this name, a new member is
     * added at the end of the object. If this object contains multiple members with this name, only
     * the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name of the member to add
     * @param value the value of the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final String value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Copies all members of the specified object into this object. When the specified object contains
     * members with names that also exist in this object, the existing values in this object will be
     * replaced by the corresponding values in the specified object.
     *
     * @param object the object to merge
     * @return the object itself, to enable method chaining
     */
    public JsonObject merge(final JsonObject object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        for (final JsonObject.Member member : object) {
            this.set(member.name, member.value);
        }
        return this;
    }

    /**
     * Returns the value of the member with the specified name in this object. If this object contains
     * multiple members with the given name, this method will return the last one.
     *
     * @param name the name of the member whose value is to be returned
     * @return the value of the last member with the specified name, or <code>null</code> if this
     * object does not contain a member with that name
     */
    public JsonValue get(final String name) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        final int index = this.indexOf(name);
        return index != -1 ? this.values.get(index) : null;
    }

    /**
     * Returns <code>true</code> if this object contains no members.
     *
     * @return <code>true</code> if this object contains no members
     */
    public boolean isEmpty() {
        return this.names.isEmpty();
    }

    /**
     * Returns an iterator over the members of this object in document order. The returned iterator
     * cannot be used to modify this object.
     *
     * @return an iterator over the members of this object
     */
    @Override
    public Iterator<JsonObject.Member> iterator() {
        final Iterator<String> namesIterator = this.names.iterator();
        final Iterator<JsonValue> valuesIterator = this.values.iterator();
        return new Iterator<JsonObject.Member>() {

            @Override
            public boolean hasNext() {
                return namesIterator.hasNext();
            }

            @Override
            public JsonObject.Member next() {
                final String name = namesIterator.next();
                final JsonValue value = valuesIterator.next();
                return new JsonObject.Member(name, value);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public JsonObject asObject() {
        return this;
    }

    @Override
    void write(final JsonWriter writer) throws IOException {
        writer.writeObjectOpen();
        final Iterator<String> namesIterator = this.names.iterator();
        final Iterator<JsonValue> valuesIterator = this.values.iterator();
        if (namesIterator.hasNext()) {
            writer.writeMemberName(namesIterator.next());
            writer.writeMemberSeparator();
            valuesIterator.next().write(writer);
            while (namesIterator.hasNext()) {
                writer.writeObjectSeparator();
                writer.writeMemberName(namesIterator.next());
                writer.writeMemberSeparator();
                valuesIterator.next().write(writer);
            }
        }
        writer.writeObjectClose();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.names.hashCode();
        result = 31 * result + this.values.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final JsonObject other = (JsonObject) obj;
        return this.names.equals(other.names) && this.values.equals(other.values);
    }

    private synchronized void readObject(final ObjectInputStream inputStream)
        throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        this.table = new JsonObject.HashIndexTable();
        this.updateHashIndex();
    }

    /**
     * Represents a member of a JSON object, a pair of a name and a value.
     */
    public static class Member {

        private final String name;

        private final JsonValue value;

        Member(final String name, final JsonValue value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Returns the name of this member.
         *
         * @return the name of this member, never <code>null</code>
         */
        public String getName() {
            return this.name;
        }

        /**
         * Returns the value of this member.
         *
         * @return the value of this member, never <code>null</code>
         */
        public JsonValue getValue() {
            return this.value;
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 31 * result + this.name.hashCode();
            result = 31 * result + this.value.hashCode();
            return result;
        }

        /**
         * Indicates whether a given object is "equal to" this JsonObject. An object is considered equal
         * if it is also a {@code JsonObject} and both objects contain the same members <em>in
         * the same order</em>.
         * <p>
         * If two JsonObjects are equal, they will also produce the same JSON output.
         * </p>
         *
         * @param object the object to be compared with this JsonObject
         * @return true if the specified object is equal to this JsonObject, {@code false}
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
            final JsonObject.Member other = (JsonObject.Member) object;
            return this.name.equals(other.name) && this.value.equals(other.value);
        }

    }

    static class HashIndexTable {

        private final byte[] hashTable = new byte[32]; // must be a power of two

        void add(final String name, final int index) {
            final int slot = this.hashSlotFor(name);
            if (index < 0xff) {
                // increment by 1, 0 stands for empty
                this.hashTable[slot] = (byte) (index + 1);
            } else {
                this.hashTable[slot] = 0;
            }
        }

        private int hashSlotFor(final Object element) {
            return element.hashCode() & this.hashTable.length - 1;
        }

        int get(final Object name) {
            final int slot = this.hashSlotFor(name);
            // subtract 1, 0 stands for empty
            return (this.hashTable[slot] & 0xff) - 1;
        }

    }

}
