/*******************************************************************************
 * Copyright (c) 2013, 2016 EclipseSource.
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
import java.io.Reader;
import java.io.StringReader;

public class JsonParser {

    private static final int MAX_NESTING_LEVEL = 1000;

    private static final int MIN_BUFFER_SIZE = 10;

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private final JsonHandler<Object, Object> handler;

    private Reader reader;

    private char[] buffer;

    private int bufferOffset;

    private int index;

    private int fill;

    private int line;

    private int lineOffset;

    private int current;

    private StringBuilder captureBuffer;

    private int captureStart;

    private int nestingLevel;

    @SuppressWarnings("unchecked")
    public JsonParser(final JsonHandler<?, ?> handler) {
        if (handler == null) {
            throw new NullPointerException("handler is null");
        }
        this.handler = (JsonHandler<Object, Object>) handler;
        handler.parser = this;
    }

    public void parse(final String string) {
        if (string == null) {
            throw new NullPointerException("string is null");
        }
        final int bufferSize = Math.max(JsonParser.MIN_BUFFER_SIZE, Math.min(JsonParser.DEFAULT_BUFFER_SIZE, string.length()));
        try {
            this.parse(new StringReader(string), bufferSize);
        } catch (final IOException exception) {
            // StringReader does not throw IOException
            throw new RuntimeException(exception);
        }
    }

    public void parse(final Reader reader) throws IOException {
        this.parse(reader, JsonParser.DEFAULT_BUFFER_SIZE);
    }

    public void parse(final Reader reader, final int buffersize) throws IOException {
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        if (buffersize <= 0) {
            throw new IllegalArgumentException("buffersize is zero or negative");
        }
        this.reader = reader;
        this.buffer = new char[buffersize];
        this.bufferOffset = 0;
        this.index = 0;
        this.fill = 0;
        this.line = 1;
        this.lineOffset = 0;
        this.current = 0;
        this.captureStart = -1;
        this.read();
        this.skipWhiteSpace();
        this.readValue();
        this.skipWhiteSpace();
        if (!this.isEndOfText()) {
            throw this.error("Unexpected character");
        }
    }

    Location getLocation() {
        final int offset = this.bufferOffset + this.index - 1;
        final int column = offset - this.lineOffset + 1;
        return new Location(offset, this.line, column);
    }

    private void readValue() throws IOException {
        switch (this.current) {
            case 'n':
                this.readNull();
                break;
            case 't':
                this.readTrue();
                break;
            case 'f':
                this.readFalse();
                break;
            case '"':
                this.readString();
                break;
            case '[':
                this.readArray();
                break;
            case '{':
                this.readObject();
                break;
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                this.readNumber();
                break;
            default:
                throw this.expected("value");
        }
    }

    private void readArray() throws IOException {
        final Object array = this.handler.startArray();
        this.read();
        if (++this.nestingLevel > JsonParser.MAX_NESTING_LEVEL) {
            throw this.error("Nesting too deep");
        }
        this.skipWhiteSpace();
        if (this.readChar(']')) {
            this.nestingLevel--;
            this.handler.endArray(array);
            return;
        }
        do {
            this.skipWhiteSpace();
            this.handler.startArrayValue(array);
            this.readValue();
            this.handler.endArrayValue(array);
            this.skipWhiteSpace();
        } while (this.readChar(','));
        if (!this.readChar(']')) {
            throw this.expected("',' or ']'");
        }
        this.nestingLevel--;
        this.handler.endArray(array);
    }

    private void readObject() throws IOException {
        final Object object = this.handler.startObject();
        this.read();
        if (++this.nestingLevel > JsonParser.MAX_NESTING_LEVEL) {
            throw this.error("Nesting too deep");
        }
        this.skipWhiteSpace();
        if (this.readChar('}')) {
            this.nestingLevel--;
            this.handler.endObject(object);
            return;
        }
        do {
            this.skipWhiteSpace();
            this.handler.startObjectName(object);
            final String name = this.readName();
            this.handler.endObjectName(object, name);
            this.skipWhiteSpace();
            if (!this.readChar(':')) {
                throw this.expected("':'");
            }
            this.skipWhiteSpace();
            this.handler.startObjectValue(object, name);
            this.readValue();
            this.handler.endObjectValue(object, name);
            this.skipWhiteSpace();
        } while (this.readChar(','));
        if (!this.readChar('}')) {
            throw this.expected("',' or '}'");
        }
        this.nestingLevel--;
        this.handler.endObject(object);
    }

    private String readName() throws IOException {
        if (this.current != '"') {
            throw this.expected("name");
        }
        return this.readStringInternal();
    }

    private void readNull() throws IOException {
        this.handler.startNull();
        this.read();
        this.readRequiredChar('u');
        this.readRequiredChar('l');
        this.readRequiredChar('l');
        this.handler.endNull();
    }

    private void readTrue() throws IOException {
        this.handler.startBoolean();
        this.read();
        this.readRequiredChar('r');
        this.readRequiredChar('u');
        this.readRequiredChar('e');
        this.handler.endBoolean(true);
    }

    private void readFalse() throws IOException {
        this.handler.startBoolean();
        this.read();
        this.readRequiredChar('a');
        this.readRequiredChar('l');
        this.readRequiredChar('s');
        this.readRequiredChar('e');
        this.handler.endBoolean(false);
    }

    private void readRequiredChar(final char ch) throws IOException {
        if (!this.readChar(ch)) {
            throw this.expected("'" + ch + "'");
        }
    }

    private void readString() throws IOException {
        this.handler.startString();
        this.handler.endString(this.readStringInternal());
    }

    private String readStringInternal() throws IOException {
        this.read();
        this.startCapture();
        while (this.current != '"') {
            if (this.current == '\\') {
                this.pauseCapture();
                this.readEscape();
                this.startCapture();
            } else if (this.current < 0x20) {
                throw this.expected("valid string character");
            } else {
                this.read();
            }
        }
        final String string = this.endCapture();
        this.read();
        return string;
    }

    private void readEscape() throws IOException {
        this.read();
        switch (this.current) {
            case '"':
            case '/':
            case '\\':
                this.captureBuffer.append((char) this.current);
                break;
            case 'b':
                this.captureBuffer.append('\b');
                break;
            case 'f':
                this.captureBuffer.append('\f');
                break;
            case 'n':
                this.captureBuffer.append('\n');
                break;
            case 'r':
                this.captureBuffer.append('\r');
                break;
            case 't':
                this.captureBuffer.append('\t');
                break;
            case 'u':
                final char[] hexChars = new char[4];
                for (int i = 0; i < 4; i++) {
                    this.read();
                    if (!this.isHexDigit()) {
                        throw this.expected("hexadecimal digit");
                    }
                    hexChars[i] = (char) this.current;
                }
                this.captureBuffer.append((char) Integer.parseInt(new String(hexChars), 16));
                break;
            default:
                throw this.expected("valid escape sequence");
        }
        this.read();
    }

    private void readNumber() throws IOException {
        this.handler.startNumber();
        this.startCapture();
        this.readChar('-');
        final int firstDigit = this.current;
        if (!this.readDigit()) {
            throw this.expected("digit");
        }
        if (firstDigit != '0') {
            while (this.readDigit()) {
            }
        }
        this.readFraction();
        this.readExponent();
        this.handler.endNumber(this.endCapture());
    }

    private boolean readFraction() throws IOException {
        if (!this.readChar('.')) {
            return false;
        }
        if (!this.readDigit()) {
            throw this.expected("digit");
        }
        while (this.readDigit()) {
        }
        return true;
    }

    private boolean readExponent() throws IOException {
        if (!this.readChar('e') && !this.readChar('E')) {
            return false;
        }
        if (!this.readChar('+')) {
            this.readChar('-');
        }
        if (!this.readDigit()) {
            throw this.expected("digit");
        }
        while (this.readDigit()) {
        }
        return true;
    }

    private boolean readChar(final char ch) throws IOException {
        if (this.current != ch) {
            return false;
        }
        this.read();
        return true;
    }

    private boolean readDigit() throws IOException {
        if (!this.isDigit()) {
            return false;
        }
        this.read();
        return true;
    }

    private void skipWhiteSpace() throws IOException {
        while (this.isWhiteSpace()) {
            this.read();
        }
    }

    private void read() throws IOException {
        if (this.index == this.fill) {
            if (this.captureStart != -1) {
                this.captureBuffer.append(this.buffer, this.captureStart, this.fill - this.captureStart);
                this.captureStart = 0;
            }
            this.bufferOffset += this.fill;
            this.fill = this.reader.read(this.buffer, 0, this.buffer.length);
            this.index = 0;
            if (this.fill == -1) {
                this.current = -1;
                this.index++;
                return;
            }
        }
        if (this.current == '\n') {
            this.line++;
            this.lineOffset = this.bufferOffset + this.index;
        }
        this.current = this.buffer[this.index++];
    }

    private void startCapture() {
        if (this.captureBuffer == null) {
            this.captureBuffer = new StringBuilder();
        }
        this.captureStart = this.index - 1;
    }

    private void pauseCapture() {
        final int end = this.current == -1 ? this.index : this.index - 1;
        this.captureBuffer.append(this.buffer, this.captureStart, end - this.captureStart);
        this.captureStart = -1;
    }

    private String endCapture() {
        final int start = this.captureStart;
        final int end = this.index - 1;
        this.captureStart = -1;
        if (this.captureBuffer.length() > 0) {
            this.captureBuffer.append(this.buffer, start, end - start);
            final String captured = this.captureBuffer.toString();
            this.captureBuffer.setLength(0);
            return captured;
        }
        return new String(this.buffer, start, end - start);
    }

    private ParseException expected(final String expected) {
        if (this.isEndOfText()) {
            return this.error("Unexpected end from input");
        }
        return this.error("Expected " + expected);
    }

    private ParseException error(final String message) {
        return new ParseException(message, this.getLocation());
    }

    private boolean isWhiteSpace() {
        return this.current == ' ' || this.current == '\t' || this.current == '\n' || this.current == '\r';
    }

    private boolean isDigit() {
        return this.current >= '0' && this.current <= '9';
    }

    private boolean isHexDigit() {
        return this.current >= '0' && this.current <= '9'
            || this.current >= 'a' && this.current <= 'f'
            || this.current >= 'A' && this.current <= 'F';
    }

    private boolean isEndOfText() {
        return this.current == -1;
    }

}
