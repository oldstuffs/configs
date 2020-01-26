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
import io.github.portlek.configs.annotations.Migrate;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;

@Config(
    name = "config",
    version = "1.0"
)
public final class TestConfig extends ManagedBase {

    @Value
    public String test_string = "Test String";

    /**
     * @deprecated migrated to {@link #new_string}
     */
    @Value
    @Deprecated
    public String foo = "Old Value.";

    @Value(
        migrate = @Migrate(path = "foo", before = "1.0")
    )
    public String new_string = "New String that's migrated.";

    /**
     * @deprecated migrated to {@link #test_section}
     */
    @Section
    @Deprecated
    private final Child migrated_section = new Child() {

    };

    @Section(
        migrate = @Migrate(path = "migrated-section")
    )
    private final Child test_section = new Child() {

        @Value
        public String test_section_string = "Test Section String";

        @Section
        private final Child child = new Child() {

            @Section
            private final Child child = new Child() {

                @Section
                private final Child child = new Child() {

                    @Section
                    private final Child child = new Child() {

                    };

                };

            };

        };

    };

}
