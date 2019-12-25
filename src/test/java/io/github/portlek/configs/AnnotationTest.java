package io.github.portlek.configs;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.Test;

class AnnotationTest {

    @Test
    void create() {
        MockBukkit.mock();
        MockBukkit.load(MyPlugin.class);
        MockBukkit.unload();
    }

}