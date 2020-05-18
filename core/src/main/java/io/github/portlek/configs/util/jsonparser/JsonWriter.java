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
package io.github.portlek.configs.util.jsonparser;

import java.io.IOException;
import java.io.Writer;

class JsonWriter {

    private static final int CONTROL_CHARACTERS_END = 0x001f;

    private static final char[] QUOT_CHARS = {'\\', '"'};

    private static final char[] BS_CHARS = {'\\', '\\'};

    private static final char[] LF_CHARS = {'\\', 'n'};

    private static final char[] CR_CHARS = {'\\', 'r'};

    private static final char[] TAB_CHARS = {'\\', 't'};

    // In JavaScript, U+2028 and U+2029 characters count as line endings and must be encoded.
    // http://stackoverflow.com/questions/2965293/javascript-parse-error-on-u2028-unicode-character
    private static final char[] UNICODE_2028_CHARS = {'\\', 'u', '2', '0', '2', '8'};

    private static final char[] UNICODE_2029_CHARS = {'\\', 'u', '2', '0', '2', '9'};

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'};

    protected final Writer writer;

    JsonWriter(final Writer writer) {
        this.writer = writer;
    }

    private static char[] getReplacementChars(final char ch) {
        if (ch > '\\') {
            if (ch < '\u2028' || ch > '\u2029') {
                // The lower range contains 'a' .. 'z'. Only 2 checks required.
                return null;
            }
            return ch == '\u2028' ? JsonWriter.UNICODE_2028_CHARS : JsonWriter.UNICODE_2029_CHARS;
        }
        if (ch == '\\') {
            return JsonWriter.BS_CHARS;
        }
        if (ch > '"') {
            // This range contains '0' .. '9' and 'A' .. 'Z'. Need 3 checks to get here.
            return null;
        }
        if (ch == '"') {
            return JsonWriter.QUOT_CHARS;
        }
        if (ch > JsonWriter.CONTROL_CHARACTERS_END) {
            return null;
        }
        if (ch == '\n') {
            return JsonWriter.LF_CHARS;
        }
        if (ch == '\r') {
            return JsonWriter.CR_CHARS;
        }
        if (ch == '\t') {
            return JsonWriter.TAB_CHARS;
        }
        return new char[]{'\\', 'u', '0', '0', JsonWriter.HEX_DIGITS[ch >> 4 & 0x000f], JsonWriter.HEX_DIGITS[ch & 0x000f]};
    }

    protected void writeLiteral(final String value) throws IOException {
        this.writer.write(value);
    }

    protected void writeNumber(final String string) throws IOException {
        this.writer.write(string);
    }

    protected void writeString(final String string) throws IOException {
        this.writer.write('"');
        this.writeJsonString(string);
        this.writer.write('"');
    }

    protected void writeArrayOpen() throws IOException {
        this.writer.write('[');
    }

    protected void writeArrayClose() throws IOException {
        this.writer.write(']');
    }

    protected void writeArraySeparator() throws IOException {
        this.writer.write(',');
    }

    protected void writeObjectOpen() throws IOException {
        this.writer.write('{');
    }

    protected void writeObjectClose() throws IOException {
        this.writer.write('}');
    }

    protected void writeMemberName(final String name) throws IOException {
        this.writer.write('"');
        this.writeJsonString(name);
        this.writer.write('"');
    }

    protected void writeMemberSeparator() throws IOException {
        this.writer.write(':');
    }

    protected void writeObjectSeparator() throws IOException {
        this.writer.write(',');
    }

    protected void writeJsonString(final String string) throws IOException {
        final int length = string.length();
        int start = 0;
        for (int index = 0; index < length; index++) {
            final char[] replacement = JsonWriter.getReplacementChars(string.charAt(index));
            if (replacement != null) {
                this.writer.write(string, start, index - start);
                this.writer.write(replacement);
                start = index + 1;
            }
        }
        this.writer.write(string, start, length - start);
    }

}
