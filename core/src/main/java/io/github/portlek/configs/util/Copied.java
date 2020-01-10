package io.github.portlek.configs.util;

import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class Copied implements Scalar<File> {

    @NotNull
    private final File file;

    @NotNull
    private final InputStream inputStream;

    public Copied(@NotNull File file, @NotNull InputStream inputStream) {
        this.file = file;
        this.inputStream = inputStream;
    }

    @Override
    @NotNull
    public File value() {
        try (final OutputStream out = new FileOutputStream(file)) {
            final byte[] buf = new byte[1024];
            int len;

            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            inputStream.close();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }

        return file;
    }

}
