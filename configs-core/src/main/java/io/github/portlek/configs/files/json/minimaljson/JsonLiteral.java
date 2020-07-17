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

@SuppressWarnings("serial")
    // use default serial UID
class JsonLiteral extends JsonValue {

    private final String value;

    private final boolean isNull;

    private final boolean isTrue;

    private final boolean isFalse;

    JsonLiteral(final String value) {
        this.value = value;
        this.isNull = "null".equals(value);
        this.isTrue = "true".equals(value);
        this.isFalse = "false".equals(value);
    }

    @Override
    public boolean isBoolean() {
        return this.isTrue || this.isFalse;
    }

    @Override
    public boolean isTrue() {
        return this.isTrue;
    }

    @Override
    public boolean isFalse() {
        return this.isFalse;
    }

    @Override
    public boolean isNull() {
        return this.isNull;
    }

    @Override
    public boolean asBoolean() {
        return this.isNull ? super.asBoolean() : this.isTrue;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
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
        final JsonLiteral other = (JsonLiteral) object;
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    void write(final JsonWriter writer) throws IOException {
        writer.writeLiteral(this.value);
    }

}
