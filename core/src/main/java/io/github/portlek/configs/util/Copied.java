package io.github.portlek.configs.util;

import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class Copied implements Scalar<File> {

    @NotNull
    private final Scalar<File> fileScalar;

    @NotNull
    private final Scalar<InputStream> inputStreamScalar;

    public Copied(@NotNull final Scalar<File> fileScalar, @NotNull final Scalar<InputStream> inputStreamScalar) {
        this.fileScalar = fileScalar;
        this.inputStreamScalar = inputStreamScalar;
    }

    public Copied(@NotNull final Scalar<File> fileScalar, @NotNull final InputStream inputStreamScalar) {
        this(fileScalar, () -> inputStreamScalar);
    }

    public Copied(@NotNull final File file, @NotNull final Scalar<InputStream> inputStreamScalar) {
        this(() -> file, inputStreamScalar);
    }

    public Copied(@NotNull final File file, @NotNull final InputStream inputStream) {
        this(() -> file, () -> inputStream);
    }

    @Override
    @NotNull
    public File value() {
        final File file;

        try {
            file = fileScalar.value();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        try(final OutputStream out = new FileOutputStream(file)) {
            final InputStream inputStream = inputStreamScalar.value();
            final byte[] buf = new byte[1024];
            int len;

            while((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            inputStream.close();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }

        return file;
    }

}
