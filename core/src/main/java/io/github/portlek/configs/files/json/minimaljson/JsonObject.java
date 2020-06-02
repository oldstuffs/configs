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

/**
 * Represents a JSON object, a set from name/value pairs, where the names are strings and the values
 * are JSON values.
 * <p>
 * Members can be added using the <code>add(String, ...)</code> methods which accept instances from
 * {@link JsonValue}, strings, primitive numbers, and boolean values. To modify certain values from an
 * object, use the <code>set(String, ...)</code> methods. Please note that the <code>add</code>
 * methods are faster than <code>set</code> as they do not search for existing members. On the other
 * hand, the <code>add</code> methods do not prevent adding multiple members with the same name.
 * Duplicate names are discouraged but not prohibited by JSON.
 * </p>
 * <p>
 * Members can be accessed by their name using {@link #get(String)}. A list from all names can be
 * obtained from the method {@link #names()}. This class also supports iterating over the members in
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
 * Even though JSON objects are unordered by definition, instances from this class preserve the order
 * from members to allow processing in document order and to guarantee a predictable output.
 * </p>
 * <p>
 * Note that this class is <strong>not thread-safe</strong>. If multiple threads access a
 * <code>JsonObject</code> instance concurrently, while at least one from these threads modifies the
 * contents from this object, access to the instance must be synchronized externally. Failure to do so
 * may lead to an inconsistent state.
 * </p>
 * <p>
 * This class is <strong>not supposed to be extended</strong> by clients.
 * </p>
 */
@SuppressWarnings("serial") // use default serial UID
public class JsonObject extends JsonValue implements Iterable<JsonObject.Member> {

    public static final String OBJECT_IS_NULL = "object is null";

    public static final String NAME_IS_NULL = "name is null";

    public static final String VALUE_IS_NULL = "value is null";

    private final List<String> names;

    private final List<JsonValue> values;

    private transient JsonObject.HashIndexTable table;

    /**
     * Creates a new empty JsonObject.
     */
    public JsonObject() {
        this.names = new ArrayList<String>();
        this.values = new ArrayList<JsonValue>();
        this.table = new JsonObject.HashIndexTable();
    }

    /**
     * Creates a new JsonObject, initialized with the contents from the specified JSON object.
     *
     * @param object the JSON object to get the initial contents from, must not be <code>null</code>
     */
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
     * Returns an unmodifiable JsonObject for the specified one. This method allows to provide
     * read-only access to a JsonObject.
     * <p>
     * The returned JsonObject is backed by the given object and reflect changes that happen to it.
     * Attempts to modify the returned JsonObject result in an
     * <code>UnsupportedOperationException</code>.
     * </p>
     *
     * @param object the JsonObject for which an unmodifiable JsonObject is to be returned
     * @return an unmodifiable view from the specified JsonObject
     */
    public static JsonObject unmodifiableObject(final JsonObject object) {
        return new JsonObject(object, true);
    }

    /**
     * Appends a new member to the end from this object, with the specified name and the JSON
     * representation from the specified <code>int</code> value.
     * <p>
     * This method <strong>does not prevent duplicate names</strong>. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method <code>set(name, value)</code> instead. However,
     * <strong> <em>add</em> is much faster than <em>set</em></strong> (because it does not need to
     * search for existing members). Therefore <em>add</em> should be preferred when constructing new
     * objects.
     * </p>
     *
     * @param name the name from the member to add
     * @param value the value from the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject add(final String name, final int value) {
        this.add(name, Json.value(value));
        return this;
    }

    /**
     * Appends a new member to the end from this object, with the specified name and the JSON
     * representation from the specified <code>long</code> value.
     * <p>
     * This method <strong>does not prevent duplicate names</strong>. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method <code>set(name, value)</code> instead. However,
     * <strong> <em>add</em> is much faster than <em>set</em></strong> (because it does not need to
     * search for existing members). Therefore <em>add</em> should be preferred when constructing new
     * objects.
     * </p>
     *
     * @param name the name from the member to add
     * @param value the value from the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject add(final String name, final long value) {
        this.add(name, Json.value(value));
        return this;
    }

    /**
     * Appends a new member to the end from this object, with the specified name and the JSON
     * representation from the specified <code>float</code> value.
     * <p>
     * This method <strong>does not prevent duplicate names</strong>. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method <code>set(name, value)</code> instead. However,
     * <strong> <em>add</em> is much faster than <em>set</em></strong> (because it does not need to
     * search for existing members). Therefore <em>add</em> should be preferred when constructing new
     * objects.
     * </p>
     *
     * @param name the name from the member to add
     * @param value the value from the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject add(final String name, final float value) {
        this.add(name, Json.value(value));
        return this;
    }

    /**
     * Appends a new member to the end from this object, with the specified name and the JSON
     * representation from the specified <code>double</code> value.
     * <p>
     * This method <strong>does not prevent duplicate names</strong>. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method <code>set(name, value)</code> instead. However,
     * <strong> <em>add</em> is much faster than <em>set</em></strong> (because it does not need to
     * search for existing members). Therefore <em>add</em> should be preferred when constructing new
     * objects.
     * </p>
     *
     * @param name the name from the member to add
     * @param value the value from the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject add(final String name, final double value) {
        this.add(name, Json.value(value));
        return this;
    }

    /**
     * Appends a new member to the end from this object, with the specified name and the JSON
     * representation from the specified <code>boolean</code> value.
     * <p>
     * This method <strong>does not prevent duplicate names</strong>. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method <code>set(name, value)</code> instead. However,
     * <strong> <em>add</em> is much faster than <em>set</em></strong> (because it does not need to
     * search for existing members). Therefore <em>add</em> should be preferred when constructing new
     * objects.
     * </p>
     *
     * @param name the name from the member to add
     * @param value the value from the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject add(final String name, final boolean value) {
        this.add(name, Json.value(value));
        return this;
    }

    /**
     * Appends a new member to the end from this object, with the specified name and the JSON
     * representation from the specified string.
     * <p>
     * This method <strong>does not prevent duplicate names</strong>. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method <code>set(name, value)</code> instead. However,
     * <strong> <em>add</em> is much faster than <em>set</em></strong> (because it does not need to
     * search for existing members). Therefore <em>add</em> should be preferred when constructing new
     * objects.
     * </p>
     *
     * @param name the name from the member to add
     * @param value the value from the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject add(final String name, final String value) {
        this.add(name, Json.value(value));
        return this;
    }

    /**
     * Appends a new member to the end from this object, with the specified name and the specified JSON
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
     * @param name the name from the member to add
     * @param value the value from the member to add, must not be <code>null</code>
     * @return the object itself, to enable method chaining
     */
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

    /**
     * Returns the <code>float</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, an empty Optional is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * <code>float</code>, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @return the value from the last member with the specified name, or an empty Optional if
     * this object does not contain a member with that name
     * @see #getFloat(String, float)
     * equivalent without Optional instantiation
     */
    public Optional<Float> getFloat(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? Optional.of(value.asFloat()) : Optional.empty();
    }

    /**
     * Returns the <code>double</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, an empty Optional is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * <code>double</code>, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @return the value from the last member with the specified name, or an empty Optional if
     * this object does not contain a member with that name
     * @see #getDouble(String, double)
     * equivalent without Optional instantiation
     */
    public OptionalDouble getDouble(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? OptionalDouble.of(value.asDouble()) : OptionalDouble.empty();
    }

    /**
     * Returns the <code>JsonObject</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, null is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * <code>int</code>, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @return the value from the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    public JsonObject getObject(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? value.asObject() : null;
    }

    /**
     * Returns the <code>boolean</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, an empty Optional is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * <code>boolean</code>, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @return the value from the last member with the specified name, or an empty Optional if
     * this object does not contain a member with that name
     * @see #getBoolean(String, boolean)
     * equivalent without Optional instantiation
     */
    public Optional<Boolean> getBoolean(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? Optional.of(value.asBoolean()) : Optional.empty();
    }

    /**
     * Returns the <code>String</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, an empty Optional is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * <code>String</code>, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @return the value from the last member with the specified name, or an empty Optional if
     * this object does not contain a member with that name
     * @see #getString(String, String)
     * equivalent without Optional instantiation
     */
    public Optional<String> getString(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? Optional.of(value.asString()) : Optional.empty();
    }

    /**
     * Returns the <code>long</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, an empty Optional is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * <code>long</code>, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @return the value from the last member with the specified name, or an empty Optional if
     * this object does not contain a member with that name
     * @see #getLong(String, long)
     * equivalent without Optional instantiation
     */
    public OptionalLong getLong(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? OptionalLong.of(value.asLong()) : OptionalLong.empty();
    }

    /**
     * Returns the value from the member with the specified name in this object. If this object contains
     * multiple members with the given name, this method will return the last one.
     *
     * @param name the name from the member whose value is to be returned
     * @return the value from the last member with the specified name, or an empty optional if this
     * object does not contain a member with that name
     * @see #get(String)
     * equivalent without Optional instantiation
     */
    public Optional<JsonValue> getOptional(final String name) {
        return Optional.ofNullable(this.get(name));
    }

    /**
     * Returns the <code>int</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, an empty Optional is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * <code>int</code>, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @return the value from the last member with the specified name, or an empty Optional if
     * this object does not contain a member with that name
     * @see #getInt(String, int)
     * equivalent without Optional instantiation
     */
    public OptionalInt getInt(final String name) {
        final JsonValue value = this.get(name);
        return value != null ? OptionalInt.of(value.asInt()) : OptionalInt.empty();
    }

    /**
     * Copies all members from the specified object into this object. When the specified object contains
     * members with names that also exist in this object, the existing values in this object will be
     * replaced by the corresponding values in the specified object, except for the case that both values
     * are JsonObjects themselves, which will trigger another merge from these objects
     *
     * @param object the object to merge
     * @return the object itself, to enable method chaining
     */
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

    /**
     * Sets the value from the member with the specified name to the JSON representation from the
     * specified <code>int</code> value. If this object does not contain a member with this name, a
     * new member is added at the end from the object. If this object contains multiple members with
     * this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name from the member to replace
     * @param value the value to set to the member
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final int value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Sets the value from the member with the specified name to the JSON representation from the
     * specified <code>long</code> value. If this object does not contain a member with this name, a
     * new member is added at the end from the object. If this object contains multiple members with
     * this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name from the member to replace
     * @param value the value to set to the member
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final long value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Sets the value from the member with the specified name to the JSON representation from the
     * specified <code>float</code> value. If this object does not contain a member with this name, a
     * new member is added at the end from the object. If this object contains multiple members with
     * this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name from the member to add
     * @param value the value from the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final float value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Sets the value from the member with the specified name to the JSON representation from the
     * specified <code>double</code> value. If this object does not contain a member with this name, a
     * new member is added at the end from the object. If this object contains multiple members with
     * this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name from the member to add
     * @param value the value from the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final double value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Sets the value from the member with the specified name to the JSON representation from the
     * specified <code>boolean</code> value. If this object does not contain a member with this name,
     * a new member is added at the end from the object. If this object contains multiple members with
     * this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name from the member to add
     * @param value the value from the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final boolean value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Sets the value from the member with the specified name to the JSON representation from the
     * specified string. If this object does not contain a member with this name, a new member is
     * added at the end from the object. If this object contains multiple members with this name, only
     * the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name from the member to add
     * @param value the value from the member to add
     * @return the object itself, to enable method chaining
     */
    public JsonObject set(final String name, final String value) {
        this.set(name, Json.value(value));
        return this;
    }

    /**
     * Sets the value from the member with the specified name to the specified JSON value. If this
     * object does not contain a member with this name, a new member is added at the end from the
     * object. If this object contains multiple members with this name, only the last one is changed.
     * <p>
     * This method should <strong>only be used to modify existing objects</strong>. To fill a new
     * object with members, the method <code>add(name, value)</code> should be preferred which is much
     * faster (as it does not need to search for existing members).
     * </p>
     *
     * @param name the name from the member to add
     * @param value the value from the member to add, must not be <code>null</code>
     * @return the object itself, to enable method chaining
     */
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

    /**
     * Removes a member with the specified name from this object. If this object contains multiple
     * members with the given name, only the last one is removed. If this object does not contain a
     * member with the specified name, the object is not modified.
     *
     * @param name the name from the member to remove
     * @return the object itself, to enable method chaining
     */
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

    /**
     * Checks if a specified member is present as a child from this object. This will not test if
     * this object contains the literal <code>null</code>, {@link JsonValue#isNull()} should be used
     * for this purpose.
     *
     * @param name the name from the member to check for
     * @return whether or not the member is present
     */
    public boolean contains(final String name) {
        return this.names.contains(name);
    }

    /**
     * Copies all members from the specified object into this object. When the specified object contains
     * members with names that also exist in this object, the existing values in this object will be
     * replaced by the corresponding values in the specified object.
     *
     * @param object the object to merge
     * @return the object itself, to enable method chaining
     */
    public JsonObject merge(final JsonObject object) {
        if (object == null) {
            throw new NullPointerException(JsonObject.OBJECT_IS_NULL);
        }
        for (final JsonObject.Member member : object) {
            this.set(member.name, member.value);
        }
        return this;
    }

    /**
     * Returns the value from the member with the specified name in this object. If this object contains
     * multiple members with the given name, this method will return the last one.
     *
     * @param name the name from the member whose value is to be returned
     * @return the value from the last member with the specified name, or <code>null</code> if this
     * object does not contain a member with that name
     */
    public JsonValue get(final String name) {
        if (name == null) {
            throw new NullPointerException(JsonObject.NAME_IS_NULL);
        }
        final int index = this.indexOf(name);
        return index != -1 ? this.values.get(index) : null;
    }

    /**
     * Returns the <code>int</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * <code>int</code>, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @param defaultValue the value to be returned if the requested member is missing
     * @return the value from the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    public int getInt(final String name, final int defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asInt() : defaultValue;
    }

    /**
     * Returns the <code>long</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * <code>long</code>, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @param defaultValue the value to be returned if the requested member is missing
     * @return the value from the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    public long getLong(final String name, final long defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asLong() : defaultValue;
    }

    /**
     * Returns the <code>float</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * <code>float</code>, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @param defaultValue the value to be returned if the requested member is missing
     * @return the value from the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    public float getFloat(final String name, final float defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asFloat() : defaultValue;
    }

    /**
     * Returns the <code>double</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * <code>double</code>, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @param defaultValue the value to be returned if the requested member is missing
     * @return the value from the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    public double getDouble(final String name, final double defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asDouble() : defaultValue;
    }

    /**
     * Returns the <code>boolean</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON <code>true</code> or <code>false</code> value, an
     * exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @param defaultValue the value to be returned if the requested member is missing
     * @return the value from the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    public boolean getBoolean(final String name, final boolean defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asBoolean() : defaultValue;
    }

    /**
     * Returns the <code>String</code> value from the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one is picked. If this
     * member's value does not represent a JSON string, an exception is thrown.
     *
     * @param name the name from the member whose value is to be returned
     * @param defaultValue the value to be returned if the requested member is missing
     * @return the value from the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    public String getString(final String name, final String defaultValue) {
        final JsonValue value = this.get(name);
        return value != null ? value.asString() : defaultValue;
    }

    /**
     * Returns the number from members (name/value pairs) in this object.
     *
     * @return the number from members in this object
     */
    public int size() {
        return this.names.size();
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
     * Returns a list from the names in this object in document order. The returned list is backed by
     * this object and will reflect subsequent changes. It cannot be used to modify this object.
     * Attempts to modify the returned list will result in an exception.
     *
     * @return a list from the names in this object
     */
    public List<String> names() {
        return Collections.unmodifiableList(this.names);
    }

    /**
     * Returns an iterator over the members from this object in document order. The returned iterator
     * cannot be used to modify this object.
     *
     * @return an iterator over the members from this object
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

    /**
     * Represents a member from a JSON object, a pair from a name and a value.
     */
    public static class Member {

        private final String name;

        private final JsonValue value;

        Member(final String name, final JsonValue value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Returns the name from this member.
         *
         * @return the name from this member, never <code>null</code>
         */
        public String getName() {
            return this.name;
        }

        /**
         * Returns the value from this member.
         *
         * @return the value from this member, never <code>null</code>
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
         * if it is also a <code>JsonObject</code> and both objects contain the same members <em>in
         * the same order</em>.
         * <p>
         * If two JsonObjects are equal, they will also produce the same JSON output.
         * </p>
         *
         * @param object the object to be compared with this JsonObject
         * @return <tt>true</tt> if the specified object is equal to this JsonObject, <code>false</code>
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
