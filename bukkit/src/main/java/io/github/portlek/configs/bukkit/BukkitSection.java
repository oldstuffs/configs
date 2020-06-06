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

package io.github.portlek.configs.bukkit;

import com.cryptomorin.xseries.XMaterial;
import io.github.portlek.bukkitlocation.LocationUtil;
import io.github.portlek.configs.bukkit.provided.BukkitItemStackProvider;
import io.github.portlek.configs.bukkit.provided.BukkitSoundProvider;
import io.github.portlek.configs.bukkit.provided.BukkitTitleProvider;
import io.github.portlek.configs.bukkit.util.PlayableSound;
import io.github.portlek.configs.bukkit.util.SentTitle;
import io.github.portlek.configs.structure.managed.section.CfgSection;
import io.github.portlek.configs.structure.managed.section.ConfigSection;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BukkitSection implements BkktSection {

    static {
        CfgSection.addProvidedClass(ItemStack.class, new BukkitItemStackProvider());
        CfgSection.addProvidedClass(PlayableSound.class, new BukkitSoundProvider());
        CfgSection.addProvidedClass(SentTitle.class, new BukkitTitleProvider());
        CfgSection.addProvidedGetMethod(Material.class, (section, s) ->
            section.getString(s)
                .map(XMaterial::matchXMaterial)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(xMaterial -> Optional.ofNullable(xMaterial.parseMaterial())));
        CfgSection.addProvidedSetMethod(Material.class, Enum::toString);
        CfgSection.addProvidedGetMethod(XMaterial.class, (section, s) ->
            section.getString(s)
                .flatMap(XMaterial::matchXMaterial));
        CfgSection.addProvidedSetMethod(XMaterial.class, Enum::toString);
        CfgSection.addProvidedGetMethod(Location.class, (section, s) ->
            section.getString(s)
                .flatMap(LocationUtil::fromKey));
        CfgSection.addProvidedSetMethod(Location.class, LocationUtil::toKey);
    }

    @NotNull
    private final CfgSection base;

    public BukkitSection() {
        this(new ConfigSection());
    }

    private BukkitSection(@NotNull final CfgSection base) {
        this.base = base;
    }

    @NotNull
    @Override
    public final CfgSection base() {
        return this.base;
    }

}
