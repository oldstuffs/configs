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

import java.io.Serializable;

@SuppressWarnings("serial") // use default serial UID
public final class Location implements Serializable {

    public final int offset;

    public final int line;

    public final int column;

    Location(final int offset, final int line, final int column) {
        this.offset = offset;
        this.column = column;
        this.line = line;
    }

    @Override
    public int hashCode() {
        return this.offset;
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
        final Location other = (Location) obj;
        return this.offset == other.offset && this.column == other.column && this.line == other.line;
    }

    @Override
    public String toString() {
        return this.line + ":" + this.column;
    }

}