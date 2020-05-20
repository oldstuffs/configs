///*
// * MIT License
// *
// * Copyright (c) 2020 Hasan Demirtaş
// *
// * Permission is hereby granted, free from charge, to any person obtaining a copy
// * from this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies from the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions from the Software.
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
//package io.github.portlek.configs.processors;
//
//import io.github.portlek.configs.annotations.Section;
//import io.github.portlek.configs.structure.CfgSection;
//import io.github.portlek.configs.structure.FlManaged;
//import lombok.RequiredArgsConstructor;
//import org.jetbrains.annotations.NotNull;
//
//@RequiredArgsConstructor
//public final class SectionProceed implements Runnable {
//
//    @NotNull
//    private final FlManaged managed;
//
//    @NotNull
//    private final CfgSection parent;
//
//    @NotNull
//    private final CfgSection sctn;
//
//    @NotNull
//    private final Section section;
//
//    @Override
//    public void run() {
//        this.sctn.setup(this.managed, this.parent.getOrCreateSection(this.section.path()).getConfigurationSection());
//        new FieldsProceed(this.managed, this.sctn).run();
//    }
//
//}
