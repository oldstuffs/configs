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
import java.io.ObjectInputStream;
import java.io.Reader;
import java.util.*;


@SuppressWarnings("serial") // use default serial UID
public class JsonObject extends JsonValue implements Iterable<JsonObject.Member> {

    public static final String OBJECT_IS_NULL = "object is null";

    public static final String NAME_IS_NULL = "name is null";

    public static final String VALUE_IS_NULL = "value is null";

    private final List<String> names;

    private final List<JsonValue> values;

    private transient JsonObject.HashIndexTable table;


    public JsonObject() {
        this.names = new ArrayList<String>();
        this.values = new ArrayList<JsonValue>();
        this.table = new JsonObject.HashIndexTable();
    }


    public JsonObject(final JsonObject object) {
        this(object, false);
    }

    private JsonObject(final JsonObject object, final boolean unmodifiable) {
        if (object == null) {
            throw new NullPointerException(JsonObject.OBJECT_IS_NULL);
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


    @Deprecated
    public static JsonObject readFrom(final Reader reader) throws IOException {
        return JsonValue.readFrom(reader).asObject();
    }


    @Deprecated
    public static JsonObject readFrom(final String string) {
        return JsonValue.readFrom(string).asObject();
    }


    public static JsonObject unmodifiableObject(final JsonObject object) {
        return new JsonObject(object, true);
    }


    public JsonObject add(final String name, final int value) {
        this.add(name, Json.value(value));
        return this;
    }


    public JsonObject add(final String name, final long value) {
        this.add(name, Json.value(value));
        return this;
    }


    public JsonObject add(final String name, final float value) {
        this.add(name, Json.value(value));
        return this;
    }


    public JsonObject add(final String name, final double value) {
        this.add(name, Json.value(value));
        return this;
    }


    public JsonObject add(final String name, final boolean value) {
        this.add(name, Json.value(value));
        return this;
    }


    public JsonObject add(final String name, final String value) {
        this.add(name, Json.value(value));
        return this;
    }


    public JsonObject add(final String name, final JsonValue value) {
        if (name == null) {
            throw new NullPointerException(JsonObject.NAME_IS_NULL);
        }
        if (value == null) {
            throw new NullPointerException(JsonObject.VALUE_IS_NULL);
        }
        this.table.add(name, this.names.size());
        this.names.add(name);
        this.values.add(value);
        return this;
    }


    public Optional<Float> getFloat(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? Optional.of(value.asFloat()) : Optional.empty();
    }


    public OptionalDouble getDouble(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? OptionalDouble.of(value.asDouble()) : OptionalDouble.empty();
    }


    public JsonObject getObject(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? value.asObject() : null;
    }


    public Optional<Boolean> getBoolean(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? Optional.of(value.asBoolean()) : Optional.empty();
    }


    public Optional<String> getString(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? Optional.of(value.asString()) : Optional.empty();
    }


    public OptionalLong getLong(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? OptionalLong.of(value.asLong()) : OptionalLong.empty();
    }


    public Optional<JsonValue> getOptional(final String name) {
        return Optional.ofNullable(this.get(name));
    }


    public OptionalInt getInt(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? OptionalInt.of(value.asInt()) : OptionalInt.empty();
    }


    public JsonObject deepMerge(final JsonObject object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        for (final JsonObject.Member member : object) {
            final String name = member.name;
            JsonValue value = member.value;
            if (value instanceof JsonObject) {
                final JsonValue existingValue = this.get(member.name);
                if (existingValue instanceof JsonObject) {
                    value = ((JsonObject) existingValue).deepMerge((JsonObject) value);
                }
            }
            this.set(name, value);
        }
        return this;
    }


    public JsonObject set(final String name, final int value) {
        this.set(name, Json.value(value));
        return this;
    }


    public JsonObject set(final String name, final long value) {
        this.set(name, Json.value(value));
        return this;
    }


    public JsonObject set(final String name, final float value) {
        this.set(name, Json.value(value));
        return this;
    }


    public JsonObject set(final String name, final double value) {
        this.set(name, Json.value(value));
        return this;
    }


    public JsonObject set(final String name, final boolean value) {
        this.set(name, Json.value(value));
        return this;
    }


    public JsonObject set(final String name, final String value) {
        this.set(name, Json.value(value));
        return this;
    }


    public JsonObject set(final String name, final JsonValue value) {
        if (name == null) {
            throw new NullPointerException(JsonObject.NAME_IS_NULL);
        }
        if (value == null) {
            throw new NullPointerException(JsonObject.VALUE_IS_NULL);
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


    public JsonObject remove(final String name) {
        if (name == null) {
            throw new NullPointerException(JsonObject.NAME_IS_NULL);
        }
        final int index = this.indexOf(name);
        if (index != -1) {
            this.table.remove(index);
            this.names.remove(index);
            this.values.remove(index);
        }
        return this;
    }


    public boolean contains(final String name) {
        return this.names.contains(name);
    }


    public JsonObject merge(final JsonObject object) {
        if (object == null) {
            throw new NullPointerException(JsonObject.OBJECT_IS_NULL);
        }
        for (final JsonObject.Member member : object) {
            this.set(member.name, member.value);
        }
        return this;
    }


    public JsonValue get(final String name) {
        if (name == null) {
            throw new NullPointerException(JsonObject.NAME_IS_NULL);
        }
        final int index = this.indexOf(name);
        return index != -1 ? this.values.get(index) : null;
    }


    public int getInt(final String name, final int defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asInt() : defaultValue;
    }


    public long getLong(final String name, final long defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asLong() : defaultValue;
    }


    public float getFloat(final String name, final float defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asFloat() : defaultValue;
    }


    public double getDouble(final String name, final double defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asDouble() : defaultValue;
    }


    public boolean getBoolean(final String name, final boolean defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asBoolean() : defaultValue;
    }


    public String getString(final String name, final String defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asString() : defaultValue;
    }


    public int size() {
        return this.names.size();
    }


    public boolean isEmpty() {
        return this.names.isEmpty();
    }


    public List<String> names() {
        return Collections.unmodifiableList(this.names);
    }


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

    int indexOf(final String name) {
        final int index = this.table.get(name);
        if (index != -1 && name.equals(this.names.get(index))) {
            return index;
        }
        return this.names.lastIndexOf(name);
    }

    private synchronized void readObject(final ObjectInputStream inputStream)
        throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        this.table = new JsonObject.HashIndexTable();
        this.updateHashIndex();
    }

    private void updateHashIndex() {
        final int size = this.names.size();
        for (int i = 0; i < size; i++) {
            this.table.add(this.names.get(i), i);
        }
    }


    public static class Member {

        private final String name;

        private final JsonValue value;

        Member(final String name, final JsonValue value) {
            this.name = name;
            this.value = value;
        }


        public String getName() {
            return this.name;
        }


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

        private final byte[] hashTable = new byte[32]; // must be a power from two

        HashIndexTable() {
        }

        HashIndexTable(final JsonObject.HashIndexTable original) {
            System.arraycopy(original.hashTable, 0, this.hashTable, 0, this.hashTable.length);
        }

        void add(final String name, final int index) {
            final int slot = this.hashSlotFor(name);
            if (index < 0xff) {
                // increment by 1, 0 stands for empty
                this.hashTable[slot] = (byte) (index + 1);
            } else {
                this.hashTable[slot] = 0;
            }
        }

        void remove(final int index) {
            for (int i = 0; i < this.hashTable.length; i++) {
                if ((this.hashTable[i] & 0xff) == index + 1) {
                    this.hashTable[i] = 0;
                } else if ((this.hashTable[i] & 0xff) > index + 1) {
                    this.hashTable[i]--;
                }
            }
        }

        int get(final Object name) {
            final int slot = this.hashSlotFor(name);
            // subtract 1, 0 stands for empty
            return (this.hashTable[slot] & 0xff) - 1;
        }

        private int hashSlotFor(final Object element) {
            return element.hashCode() & this.hashTable.length - 1;
        }

    }

}
