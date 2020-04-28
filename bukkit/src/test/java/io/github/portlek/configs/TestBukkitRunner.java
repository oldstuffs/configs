package io.github.portlek.configs;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.Test;

public class TestBukkitRunner {

    @Test
    final void api() {
        MockBukkit.mock();
        MockBukkit.load(TestBukkitPlugin.class);
        MockBukkit.unload();
    }

}
