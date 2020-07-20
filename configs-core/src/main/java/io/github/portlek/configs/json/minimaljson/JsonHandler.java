/*******************************************************************************
 * Copyright (c) 2016 EclipseSource.
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

public abstract class JsonHandler<A, O> {

    JsonParser parser;

    public void startNull() {
    }

    public void endNull() {
    }

    public void startBoolean() {
    }

    public void endBoolean(final boolean value) {
    }

    public void startString() {
    }

    public void endString(final String string) {
    }

    public void startNumber() {
    }

    public void endNumber(final String string) {
    }

    public A startArray() {
        return null;
    }

    public void endArray(final A array) {
    }

    public void startArrayValue(final A array) {
    }

    public void endArrayValue(final A array) {
    }

    public O startObject() {
        return null;
    }

    public void endObject(final O object) {
    }

    public void startObjectName(final O object) {
    }

    public void endObjectName(final O object, final String name) {
    }

    public void startObjectValue(final O object, final String name) {
    }

    public void endObjectValue(final O object, final String name) {
    }

    protected Location getLocation() {
        return this.parser.getLocation();
    }

}
