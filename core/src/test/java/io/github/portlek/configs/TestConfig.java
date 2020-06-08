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

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.structure.managed.FileManaged;
import io.github.portlek.configs.structure.section.ConfigSection;
import java.util.Arrays;
import java.util.List;

@Config("config")
public final class TestConfig extends FileManaged {

    @Instance
    public final TestConfig.TestSection testSection = new TestConfig.TestSection();

    @Property
    public String test = new StringBuilder()
        .append("test")
        .append('\n')
        .append("test-2")
        .toString();

    @Property
    public List<String> test_list = Arrays.asList("test",
        "test2",
        "test3");

    public static void main(final String[] args) {
        new TestConfig().load();
    }

    @Section("test-section")
    public static final class TestSection extends ConfigSection {

        @Property
        public String test = "test-2";

        @Property
        public List<String> test_list = Arrays.asList("test",
            "test2",
            "test3");

    }

}
