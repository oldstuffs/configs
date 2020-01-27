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
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;

@Config(
    name = "config"
)
public final class TestConfig extends ManagedBase {

    @Value
    public String test_string = "Test String";

    @Value
    public String new_string = "New String that's migrated.";

    @Instance
    public test_section test_section = new test_section();

    @Section(path = "test-section")
    public static class test_section {

        @Value
        public String test_section_string = "Test Section String";

        @Instance
        public child1 child = new child1();

        @Section(path = "child")
        public static class child1 {

            @Value
            public String test_section_string = "Test Section String";

            @Instance
            public child2 child = new child2();

            @Section(path = "child")
            public static class child2 {

                @Value
                public String test_section_string = "Test Section String";

                @Instance
                public child3 child = new child3();

                @Section(path = "child")
                public static class child3 {

                    @Value
                    public String test_section_string = "Test Section String";

                    @Instance
                    public child4 child = new child4();

                    @Section(path = "child")
                    public static class child4 {

                        @Value
                        public String test_section_string = "Test Section String";

                    }

                }

            }

        }

    }

}
