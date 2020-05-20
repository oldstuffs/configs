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
//package io.github.portlek.configs.oldstructure.managed;
//
//import io.github.portlek.configs.annotations.Config;
//import io.github.portlek.configs.files.configuration.FileConfiguration;
//import io.github.portlek.configs.processors.ConfigProceed;
//import io.github.portlek.configs.provided.Provided;
//import io.github.portlek.configs.oldstructure.managed.section.CfgSection;
//import java.io.File;
//import java.util.Optional;
//import org.jetbrains.annotations.NotNull;
//
//public interface FlManaged extends CfgSection {
//
//    @Override
//    FileConfiguration getConfigurationSection();
//
//    @NotNull
//    Optional<Object> pull(@NotNull String id);
//
//    default void load() {
//        this.onCreate();
//        new ConfigProceed(
//            this,
//            Optional.ofNullable(this.getClass().getDeclaredAnnotation(Config.class))
//                .orElseThrow(() ->
//                    new UnsupportedOperationException(this.getClass().getSimpleName() + " has not `Config` annotation!"))
//        ).run();
//        this.onLoad();
//    }
//
//    default void onCreate() {
//    }
//
//    default void onLoad() {
//    }
//
//    void setup(@NotNull File file, @NotNull FileConfiguration fileConfiguration);
//
//    <T> void addCustomValue(@NotNull Class<T> aClass, @NotNull Provided<T> provided);
//
//    @NotNull <T> Optional<Provided<T>> getCustomValue(@NotNull Class<T> aClass);
//
//    default void save() {
//        this.getConfigurationSection().save(this.getFile());
//    }
//
//    @NotNull
//    File getFile();
//
//    void addObject(@NotNull String key, @NotNull Object object);
//
//    boolean isAutoSave();
//
//    void setAutoSave(boolean autosv);
//
//    void autoSave();
//
//}