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

    private static Managed config;

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
        final Optional<Object> test1 = TestConfigSection.config.get("getwithdefault-test-1", "getwithdefaulttest-1");
        assertTrue(test1.isPresent(), "`getwithdefault-test-1` not found!");
        assertEquals("getwithdefaulttest-1", test1.get(), "`getwithdefault-test-1` is not equal to `getwithdefaulttest-1`!");

        final Optional<Object> test2 = TestConfigSection.config.get("getwithdefault-test-2", "getwithdefaulttest-2");
        assertTrue(test2.isPresent(), "There is not a value on `getwithdefault-test-2`!");
        assertDoesNotThrow(test2::get, "Default value can't get from as `getwithdefault-test-2`!");
        assertEquals("getwithdefaulttest-2", test2.get(), "The value that gotten from the default value is not equal to `getwithdefault-test-2`");

        final Optional<Object> test3 = TestConfigSection.config.get("getwithdefault-test-3", null);
        assertFalse(test3.isPresent(), "`getwithdefault-test-3` not found!");
        assertThrows(NoSuchElementException.class, test3::get, "Wrong exception class was thrown!");
        assertNull(test3.orElse(null), "`getwithdefault-test-3` is not null!");
        assertNotNull(test3.orElse("getwithdefaulttest-3"), "`getwithdefault-test-3` is null!");
    }

    @Test
    void getOrSetTest() {
        final Object test1 = TestConfigSection.config.getOrSet("getorset-test-1", "getorsettest-1");
        assertNotNull(test1, "`test` is null!");
        assertEquals("getorsettest-1", test1, "`getorset-test-1` is not equal to `getorsettest-1`!");

        final Object test2 = TestConfigSection.config.getOrSet("getorset-test-2", "getorsettest-2");
        assertNotNull(test2, "`getorset-test-2` is null!");
        assertEquals("getorsettest-2", test2, "`getorset-test-2` is not equal to `getorsettest-2`!");
    }

    @Test
    void setTest() {
        TestConfigSection.config.set("set-test-1", "settest-1");
        final Optional<Object> settest1 = TestConfigSection.config.get("set-test-1");
        assertTrue(settest1.isPresent(), "`set-test-1` hasn't been set!");
        assertDoesNotThrow(settest1::get, "`set-test-1` was throw an exception!");
        assertEquals("settest-1", settest1.get(), "`set-test-1` is not equal to `settest-1`");
    }

}
