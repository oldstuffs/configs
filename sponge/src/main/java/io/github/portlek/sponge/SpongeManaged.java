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

package io.github.portlek.sponge;

import io.github.portlek.configs.files.configuration.FileConfiguration;
import io.github.portlek.configs.provided.Provided;
import io.github.portlek.configs.structure.managed.FileManaged;
import io.github.portlek.configs.structure.managed.FlManaged;
import io.github.portlek.configs.structure.managed.section.CfgSection;
import io.github.portlek.sponge.provided.SpongeItemStackProvider;
import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.item.inventory.ItemStack;

public class SpongeManaged extends SpongeSection implements FlManaged {

    @SafeVarargs
    public SpongeManaged(@NotNull final Map.Entry<String, Object>... objects) {
        this(new FileManaged(), objects);
    }

    @SafeVarargs
    public SpongeManaged(@NotNull final CfgSection managed, @NotNull final Map.Entry<String, Object>... objects) {
        super(managed);
        this.addCustomValue(ItemStack.class, new SpongeItemStackProvider());
        Arrays.stream(objects).forEach(entry -> this.addObject(entry.getKey(), entry.getValue()));
    }

    @NotNull
    @Override
    public FlManaged getBase() {
        return (FlManaged) super.getBase();
    }

    @NotNull
    @Override
    public final FileConfiguration getConfigurationSection() {
        return this.getBase().getConfigurationSection();
    }

    @NotNull
    @Override
    public final Optional<Object> pull(@NotNull final String id) {
        return this.getBase().pull(id);
    }

    @Override
    public final void setup(@NotNull final File file, @NotNull final FileConfiguration fileConfiguration) {
        this.getBase().setup(file, fileConfiguration);
    }

    @Override
    public final <T> void addCustomValue(@NotNull final Class<T> aClass, @NotNull final Provided<T> provided) {
        this.getBase().addCustomValue(aClass, provided);
    }

    @NotNull
    @Override
    public final <T> Optional<Provided<T>> getCustomValue(@NotNull final Class<T> aClass) {
        return this.getBase().getCustomValue(aClass);
    }

    @NotNull
    @Override
    public final File getFile() {
        return this.getBase().getFile();
    }

    @Override
    public final void addObject(@NotNull final String key, @NotNull final Object object) {
        this.getBase().addObject(key, object);
    }

    @Override
    public final boolean isAutoSave() {
        return this.getBase().isAutoSave();
    }

    @Override
    public final void setAutoSave(final boolean autosv) {
        this.getBase().setAutoSave(autosv);
    }

    @Override
    public final void autoSave() {
        this.getBase().autoSave();
    }

}
