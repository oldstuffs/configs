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

import org.jetbrains.annotations.Nullable;

/**
 * A handler for parser events. Instances of this class can be given to a {@link JsonParser}. The
 * parser will then call the methods of the given handler while reading the input.
 * <p>
 * The default implementations of these methods do nothing. Subclasses may override only those
 * methods they are interested in. They can use {@code getLocation()} to access the current
 * character position of the parser at any point. The {@code start*} methods will be called
 * while the location points to the first character of the parsed element. The {@code end*}
 * methods will be called while the location points to the character position that directly follows
 * the last character of the parsed element. Example:
 * </p>
 *
 * <pre>
 * ["lorem ipsum"]
 *  ^            ^
 *  startString  endString
 * </pre>
 * <p>
 * Subclasses that build an object representation of the parsed JSON can return arbitrary handler
 * objects for JSON arrays and JSON objects in {@link #startArray()} and {@link #startObject()}.
 * These handler objects will then be provided in all subsequent parser events for this particular
 * array or object. They can be used to keep track the elements of a JSON array or object.
 * </p>
 *
 * @param <A> The type of handlers used for JSON arrays
 * @param <O> The type of handlers used for JSON objects
 * @see JsonParser
 */
public abstract class JsonHandler<A, O> {

    /**
     * Indicates the end of a {@code null} literal in the JSON input. This method will be called
     * after reading the last character of the literal.
     */
    public void endNull() {
    }

    /**
     * Indicates the end of a boolean literal ({@code true} or {@code false}) in the JSON
     * input. This method will be called after reading the last character of the literal.
     *
     * @param value the parsed boolean value
     */
    public void endBoolean(final boolean value) {
    }

    /**
     * Indicates the end of a string in the JSON input. This method will be called after reading the
     * closing double quote character ({@code '"'}).
     *
     * @param string the parsed string
     */
    public void endString(final String string) {
    }

    /**
     * Indicates the end of a number in the JSON input. This method will be called after reading the
     * last character of the number.
     *
     * @param string the parsed number string
     */
    public void endNumber(final String string) {
    }

    @Nullable
    public A startArray() {
        return null;
    }

    /**
     * Indicates the end of an array in the JSON input. This method will be called after reading the
     * closing square bracket character ({@code ']'}).
     *
     * @param array the array handler returned from {@link #startArray()}, or {@code null} if not
     * provided
     */
    public void endArray(final A array) {
    }

    /**
     * Indicates the end of an array element in the JSON input. This method will be called after
     * reading the last character of the element value, just after the {@code end} method for the
     * specific element type (like {@link #endString(String) endString()}, {@link #endNumber(String)
     * endNumber()}, etc.).
     *
     * @param array the array handler returned from {@link #startArray()}, or {@code null} if not
     * provided
     */
    public void endArrayValue(final A array) {
    }

    @Nullable
    public O startObject() {
        return null;
    }

    /**
     * Indicates the end of an object in the JSON input. This method will be called after reading the
     * closing curly bracket character (<code>'}'</code>).
     *
     * @param object the object handler returned from {@link #startObject()}, or null if not provided
     */
    public void endObject(final O object) {
    }

    /**
     * Indicates the end of an object member value in the JSON input. This method will be called after
     * reading the last character of the member value, just after the {@code end} method for the
     * specific member type (like {@link #endString(String) endString()}, {@link #endNumber(String)
     * endNumber()}, etc.).
     *
     * @param object the object handler returned from {@link #startObject()}, or null if not provided
     * @param name the parsed member name
     */
    public void endObjectValue(final O object, final String name) {
    }

}
