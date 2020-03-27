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

package io.github.portlek.configs;

import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public final class TestConfigSection {

    private static TestConfig config;

    @BeforeAll
    static void before() {
        TestConfigSection.config = new TestConfig();
        TestConfigSection.config.load();
        TestConfigSection.config.setAutosave(true);
    }

    @Test
    void getWithoutDefaultTest() {
        final Optional<Object> test = TestConfigSection.config.get("test");
        assertTrue(test.isPresent(), "`test` not found!");
        assertEquals("test", test.get(), "`test` not equal to " + test.get() + '!');
        assertNotEquals("test1", test.get(), "`test1` equal to " + test.get() + '!');

        final Optional<Object> test2 = TestConfigSection.config.get("test1");
        assertFalse(test2.isPresent(), "There is a value on `test1`!");
        assertThrows(NoSuchElementException.class, test2::get, "Wrong exception class was thrown!");
    }

    @Test
    void getWithDefaultTest() {
        final Optional<Object> test1 = TestConfigSection.config.get("test", "test-1");
        assertTrue(test1.isPresent(), "`test` not found!");
        assertEquals("test", test1.get(), "`test` not equal to " + test1.get() + '!');
        assertNotEquals("test-1", test1.get(), "`test1` equal to " + test1.get() + '!');

        final Optional<Object> test2 = TestConfigSection.config.get("test2", "test-2");
        assertTrue(test2.isPresent(), "There is a value on `test2`!");
        assertDoesNotThrow(test2::get, "Default value can't get from as `test-2`!");
        assertEquals("test-2", test2.get(), "The value that gotten from the default value is not equal to `test-2`");

        final Optional<Object> test3 = TestConfigSection.config.get("test3", null);
        assertFalse(test3.isPresent(), "`test` found!");
        assertThrows(NoSuchElementException.class, test3::get, "Wrong exception class was thrown!");
        assertNull(test3.orElse(null), "Test3 is not null!");
    }

    @Test
    void getOrSetTest() {
        final Object test1 = TestConfigSection.config.getOrSet("test", "test-1");
        assertNotNull(test1, "`test` is null!");
        assertEquals("test", test1, "`test` is not equal to `test-1`");
        assertNotEquals("test-1", test1, "`test` is equal to `test-1`");

    }

}
