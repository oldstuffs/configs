/*******************************************************************************
 * Copyright (c) 2015 EclipseSource.
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

import java.io.IOException;
import java.io.Writer;

class WritingBuffer extends Writer {

    private final Writer writer;

    private final char[] buffer;

    private int fill = 0;

    WritingBuffer(final Writer writer) {
        this(writer, 16);
    }

    WritingBuffer(final Writer writer, final int bufferSize) {
        this.writer = writer;
        this.buffer = new char[bufferSize];
    }

    @Override
    public void write(final int c) throws IOException {
        if (this.fill > this.buffer.length - 1) {
            this.flush();
        }
        this.buffer[this.fill++] = (char) c;
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        if (this.fill > this.buffer.length - len) {
            this.flush();
            if (len > this.buffer.length) {
                this.writer.write(cbuf, off, len);
                return;
            }
        }
        System.arraycopy(cbuf, off, this.buffer, this.fill, len);
        this.fill += len;
    }

    @Override
    public void write(final String str, final int off, final int len) throws IOException {
        if (this.fill > this.buffer.length - len) {
            this.flush();
            if (len > this.buffer.length) {
                this.writer.write(str, off, len);
                return;
            }
        }
        str.getChars(off, off + len, this.buffer, this.fill);
        this.fill += len;
    }

    @Override
    public void flush() throws IOException {
        this.writer.write(this.buffer, 0, this.fill);
        this.fill = 0;
    }

    @Override
    public void close() throws IOException {
    }

}
