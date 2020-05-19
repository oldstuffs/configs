/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
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
 *
 */

package io.github.portlek.configs.files.configuration;

/**
 * Exception thrown when attempting to load an invalid {@link Configuration}
 */
@SuppressWarnings("serial")
public class InvalidConfigurationException extends Exception {

    /**
     * Constructs an instance from InvalidConfigurationException with the
     * specified message.
     *
     * @param msg The details from the exception.
     */
    public InvalidConfigurationException(final String msg) {
        super(msg);
    }

    /**
     * Constructs an instance from InvalidConfigurationException with the
     * specified cause.
     *
     * @param cause The cause from the exception.
     */
    public InvalidConfigurationException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance from InvalidConfigurationException with the
     * specified message and cause.
     *
     * @param cause The cause from the exception.
     * @param msg The details from the exception.
     */
    public InvalidConfigurationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
