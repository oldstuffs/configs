/*******************************************************************************
 * Copyright (c) 2015 EclipseSource.
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
import java.io.Writer;
import java.util.Arrays;


/**
 * Enables human readable JSON output by inserting whitespace between values.after commas and
 * colons. Example:
 *
 * <pre>
 * jsonValue.writeTo(writer, PrettyPrint.singleLine());
 * </pre>
 */
public class PrettyPrint extends WriterConfig {

    private final char[] indentChars;

    protected PrettyPrint(final char[] indentChars) {
        this.indentChars = indentChars;
    }

    /**
     * Print every value on a separate line. Use tabs (<code>\t</code>) for indentation.
     *
     * @return A PrettyPrint instance for wrapped mode with tab indentation
     */
    public static PrettyPrint singleLine() {
        return new PrettyPrint(null);
    }

    /**
     * Print every value on a separate line. Use the given number of spaces for indentation.
     *
     * @param number the number of spaces to use
     * @return A PrettyPrint instance for wrapped mode with spaces indentation
     */
    public static PrettyPrint indentWithSpaces(final int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number is negative");
        }
        final char[] chars = new char[number];
        Arrays.fill(chars, ' ');
        return new PrettyPrint(chars);
    }

    /**
     * Do not break lines, but still insert whitespace between values.
     *
     * @return A PrettyPrint instance for single-line mode
     */
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
