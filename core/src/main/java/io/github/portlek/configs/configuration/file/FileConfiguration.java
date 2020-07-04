package io.github.portlek.configs.configuration.file;

import io.github.portlek.configs.configuration.Configuration;
import io.github.portlek.configs.configuration.MemoryConfiguration;
import java.io.*;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

public abstract class FileConfiguration extends MemoryConfiguration {

    public FileConfiguration() {
        super();
    }

    public FileConfiguration(final Configuration defaults) {
        super(defaults);
    }

    @SneakyThrows
    public void save(@NotNull final File file) {
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        final String data = this.saveToString();
        try (final Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write(data);
        }
    }

    public void save(@NotNull final String file) throws IOException {
        this.save(new File(file));
    }

    public abstract String saveToString();

    @SneakyThrows
    public void load(@NotNull final File file) {
        this.load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
    }

    @Deprecated
    public void load(@NotNull final InputStream stream) throws IOException {
        this.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    public void load(final Reader reader) throws IOException {
        final BufferedReader input = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);

        final StringBuilder builder = new StringBuilder();

        try {
            String line;

            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            input.close();
        }

        this.loadFromString(builder.toString());
    }

    public void load(@NotNull final String file) throws IOException {
        this.load(new File(file));
    }

    public abstract void loadFromString(String contents) throws IOException;

    @Override
    public FileConfigurationOptions options() {
        if (this.options == null) {
            this.options = new FileConfigurationOptions(this);
        }
        return (FileConfigurationOptions) this.options;
    }

}
