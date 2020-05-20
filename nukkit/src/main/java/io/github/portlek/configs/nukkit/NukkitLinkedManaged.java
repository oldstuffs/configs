///*
// * MIT License
// *
// * Copyright (c) 2020 Hasan Demirtaş
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// *
// */
//
//package io.github.portlek.configs.nukkit;
//
//import io.github.portlek.configs.oldstructure.LinkedFileManaged;
//import io.github.portlek.configs.oldstructure.LnkdFlManaged;
//import java.util.Map;
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.function.Supplier;
//import org.jetbrains.annotations.NotNull;
//
//public class NukkitLinkedManaged extends NukkitManaged implements LnkdFlManaged {
//
//    @SafeVarargs
//    public NukkitLinkedManaged(@NotNull final Supplier<String> chosen,
//                               @NotNull final Map.Entry<String, Object>... objects) {
//        super(new LinkedFileManaged(chosen), objects);
//    }
//
//    @NotNull
//    @Override
//    public final LnkdFlManaged getBase() {
//        return (LnkdFlManaged) super.getBase();
//    }
//
//    @NotNull
//    @Override
//    public final <T> T match(@NotNull final Function<String, Optional<T>> function) {
//        return this.getBase().match(function);
//    }
//
//    @NotNull
//    @Override
//    public final Supplier<String> getChosen() {
//        return this.getBase().getChosen();
//    }
//
//}
