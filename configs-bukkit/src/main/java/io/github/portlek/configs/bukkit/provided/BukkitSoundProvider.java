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

package io.github.portlek.configs.bukkit.provided;

import com.cryptomorin.xseries.XSound;
import io.github.portlek.configs.bukkit.util.PlayableSound;
import io.github.portlek.configs.Provided;
import io.github.portlek.configs.CfgSection;
import io.github.portlek.configs.util.GeneralUtilities;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public final class BukkitSoundProvider implements Provided<PlayableSound> {

    @Override
    public void set(@NotNull final PlayableSound sound, @NotNull final CfgSection section,
                    @NotNull final String path) {
        final String fnlpath = GeneralUtilities.putDot(path);
        section.set(fnlpath + "sound", sound.getSound().name());
        section.set(fnlpath + "volume", sound.getVolume());
        section.set(fnlpath + "pitch", sound.getPitch());
    }

    @NotNull
    @Override
    public Optional<PlayableSound> get(@NotNull final CfgSection section, @NotNull final String path) {
        final String fnlpath = GeneralUtilities.putDot(path);
        final Optional<Double> volume = section.getDouble(fnlpath + "volume");
        final Optional<Double> pitch = section.getDouble(fnlpath + "pitch");
        final Optional<Sound> sound = section.getString(fnlpath + "sound")
            .flatMap(XSound::matchXSound)
            .map(XSound::parseSound)
            .filter(Objects::nonNull);
        if (!sound.isPresent() || !volume.isPresent() || !pitch.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new PlayableSound(sound.get(), volume.get(), pitch.get()));
    }

}
