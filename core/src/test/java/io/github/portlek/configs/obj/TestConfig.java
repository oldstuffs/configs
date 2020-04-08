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

package io.github.portlek.configs.obj;

import io.github.portlek.configs.ConfigSection;
import io.github.portlek.configs.FileManaged;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.configs.util.Replaceable;
import java.util.UUID;

@Config(
    name = "config",
    type = FileType.JSON,
    version = "1.1"
)
public final class TestConfig extends FileManaged {

    @Instance
    public final TestConfig.TestSection testSection = new TestConfig.TestSection();

    @Value
    public String test = "test";

    @Value
    public ProvidedObject provided_object = new ProvidedObject(
        UUID.fromString("9979fabf-909a-46be-b92c-376b48ecf15a"),
        "Test",
        22
    );

    @Value
    public Replaceable<String> test_replaceable = Replaceable.of("Test");

    @Section(path = "test-section")
    public static final class TestSection extends ConfigSection {

        @Value
        public String test = "test-section > test";

        @Value
        public ProvidedObject provided_object = new ProvidedObject(
            UUID.fromString("9e03090a-c24b-43a3-8c29-0d47b7e3efc5"),
            "Test",
            33
        );

        @Value
        public Replaceable<String> test_replaceable_2 = Replaceable.of("Test-2");

    }

}