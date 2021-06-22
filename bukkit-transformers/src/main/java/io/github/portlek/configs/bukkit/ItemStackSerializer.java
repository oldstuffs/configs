/*
 * MIT License
 *
 * Copyright (c) 2021 Hasan Demirtaş
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

import io.github.portlek.bukkititembuilder.ItemStackBuilder;
import io.github.portlek.bukkititembuilder.util.ItemStackUtil;
import io.github.portlek.bukkititembuilder.util.KeyUtil;
import io.github.portlek.transformer.ObjectSerializer;
import io.github.portlek.transformer.TransformedData;
import io.github.portlek.transformer.declarations.GenericDeclaration;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents serializer of {@link ItemStackBuilder}.
 */
public final class ItemStackSerializer implements ObjectSerializer<ItemStack> {

  @NotNull
  @Override
  public Optional<ItemStack> deserialize(@NotNull final TransformedData transformedData,
                                         @Nullable final GenericDeclaration declaration) {
    return ItemStackUtil.deserialize(KeyUtil.Holder.transformedData(transformedData));
  }

  @NotNull
  @Override
  public Optional<ItemStack> deserialize(@NotNull final ItemStack field,
                                         @NotNull final TransformedData transformedData,
                                         @Nullable final GenericDeclaration declaration) {
    return this.deserialize(transformedData, declaration);
  }

  @Override
  public void serialize(@NotNull final ItemStack itemStack, @NotNull final TransformedData transformedData) {
    ItemStackUtil.serialize(ItemStackBuilder.from(itemStack), KeyUtil.Holder.transformedData(transformedData));
  }

  @Override
  public boolean supports(@NotNull final Class<?> cls) {
    return cls == ItemStack.class;
  }
}
