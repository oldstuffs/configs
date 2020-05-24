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

package io.github.portlek.configs.obj;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.files.FileType;
import io.github.portlek.configs.structure.managed.FileManaged;
import io.github.portlek.configs.structure.managed.section.ConfigSection;
import io.github.portlek.configs.util.Replaceable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Config(
    name = "config",
    type = FileType.JSON,
    version = "1.1"
)
public final class TestConfig extends FileManaged {

    @Instance
    public final TestConfig.TestSection testSection = new TestConfig.TestSection();

    @Property
    public String test = "test";

    @Property
    public ProvidedObject provided_object = new ProvidedObject(
        UUID.fromString("9e03090a-c24b-43a3-8c29-0d47b7e3efc5"),
        "Test",
        22
    );

    @Property
    public Replaceable<String> test_replaceable = Replaceable.from("Test");

    @Property
    public List<String> test_list = new ArrayList<>();

    @Override
    public void onCreate() {
        this.addSerializableClass(ProvidedObject.class);
    }

    @Override
    public void onLoad() {
        this.setAutoSave(true);
    }

    @Section("test-section")
    public static final class TestSection extends ConfigSection {

        @Property
        public String test = "test-section > test";

        @Property
        public ProvidedObject provided_object = new ProvidedObject(
            UUID.fromString("9e03090a-c24b-43a3-8c29-0d47b7e3efc5"),
            "Test",
            33
        );

        @Property
        public Replaceable<String> test_replaceable_2 = Replaceable.from("Test-2");

    }

}
