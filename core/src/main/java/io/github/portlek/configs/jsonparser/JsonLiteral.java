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

@SuppressWarnings("serial")
    // use default serial UID
class JsonLiteral extends JsonValue {

    private final String value;

    private final boolean isNull;

    private final boolean isTrue;

    private final boolean isFalse;

    JsonLiteral(final String value) {
        super();
        this.value = value;
        this.isNull = "null".equals(value);
        this.isTrue = "true".equals(value);
        this.isFalse = "false".equals(value);
    }

    @Override
    public final boolean isBoolean() {
        return this.isTrue || this.isFalse;
    }

    @Override
    public final boolean isTrue() {
        return this.isTrue;
    }

    @Override
    public final boolean isFalse() {
        return this.isFalse;
    }

    @Override
    public final boolean isNull() {
        return this.isNull;
    }

    @Override
    public final boolean asBoolean() {
        return this.isNull ? super.asBoolean() : this.isTrue;
    }

    @Override
    public final int hashCode() {
        return this.value.hashCode();
    }

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
        final JsonLiteral other = (JsonLiteral) object;
        return this.value.equals(other.value);
    }

    @Override
    public final String toString() {
        return this.value;
    }

    @Override
    final void write(final JsonWriter writer) throws IOException {
        writer.writeLiteral(this.value);
    }

}
