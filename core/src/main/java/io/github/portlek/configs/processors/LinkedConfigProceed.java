//package io.github.portlek.configs.processors;
//
//import io.github.portlek.configs.annotations.LinkedConfig;
//import io.github.portlek.configs.annotations.LinkedFile;
//import io.github.portlek.configs.oldstructure.LnkdFlManaged;
//import java.util.Arrays;
//import lombok.RequiredArgsConstructor;
//import org.jetbrains.annotations.NotNull;
//
//@RequiredArgsConstructor
//public final class LinkedConfigProceed implements Runnable {
//
//    @NotNull
//    private final LnkdFlManaged managed;
//
//    @NotNull
//    private final LinkedConfig linked;
//
//    @Override
//    public void run() {
//        Arrays.stream(this.linked.files())
//            .filter(linkedFile -> linkedFile.key().equals(this.managed.getChosen().get()))
//            .findFirst()
//            .map(LinkedFile::config)
//            .map(config -> new ConfigProceed(this.managed, config))
//            .ifPresent(ConfigProceed::run);
//    }
//
//}
