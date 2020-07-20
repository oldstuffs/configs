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
import java.util.Arrays;

public class PrettyPrint extends WriterConfig {

    private final char[] indentChars;

    protected PrettyPrint(final char[] indentChars) {
        this.indentChars = indentChars;
    }

    public static PrettyPrint singleLine() {
        return new PrettyPrint(null);
    }

    public static PrettyPrint indentWithSpaces(final int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number is negative");
        }
        final char[] chars = new char[number];
        Arrays.fill(chars, ' ');
        return new PrettyPrint(chars);
    }

    public static PrettyPrint indentWithTabs() {
        return new PrettyPrint(new char[]{'\t'});
    }

    @Override
    protected JsonWriter createWriter(final Writer writer) {
        return new PrettyPrint.PrettyPrintWriter(writer, this.indentChars);
    }

    private static class PrettyPrintWriter extends JsonWriter {

        private final char[] indentChars;

        private int indent;

        private PrettyPrintWriter(final Writer writer, final char[] indentChars) {
            super(writer);
            this.indentChars = indentChars;
        }

        @Override
        protected void writeArrayOpen() throws IOException {
            this.indent++;
            this.writer.write('[');
            this.writeNewLine();
        }

        @Override
        protected void writeArrayClose() throws IOException {
            this.indent--;
            this.writeNewLine();
            this.writer.write(']');
        }

        @Override
        protected void writeArraySeparator() throws IOException {
            this.writer.write(',');
            if (!this.writeNewLine()) {
                this.writer.write(' ');
            }
        }

        @Override
        protected void writeObjectOpen() throws IOException {
            this.indent++;
            this.writer.write('{');
            this.writeNewLine();
        }

        @Override
        protected void writeObjectClose() throws IOException {
            this.indent--;
            this.writeNewLine();
            this.writer.write('}');
        }

        @Override
        protected void writeMemberSeparator() throws IOException {
            this.writer.write(':');
            this.writer.write(' ');
        }

        @Override
        protected void writeObjectSeparator() throws IOException {
            this.writer.write(',');
            if (!this.writeNewLine()) {
                this.writer.write(' ');
            }
        }

        private boolean writeNewLine() throws IOException {
            if (this.indentChars == null) {
                return false;
            }
            this.writer.write('\n');
            for (int i = 0; i < this.indent; i++) {
                this.writer.write(this.indentChars);
            }
            return true;
        }

    }

}
