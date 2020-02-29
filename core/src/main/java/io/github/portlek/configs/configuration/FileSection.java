package io.github.portlek.configs.configuration;

import io.github.portlek.configs.exception.ConfigsValidationException;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.configs.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public abstract class FileSection implements ConfigurationSection {

    @NotNull
    private final File file;

    @NotNull
    private final FileType type;

    @NotNull
    private String pathprefix = "";

    private long lastloaded = 0L;

    protected FileSection(@NotNull final String name, final String path, @NotNull final FileType typ) {
        if (name.isEmpty()) {
            throw new ConfigsValidationException("Name mustn't be empty");
        }
        this.type = typ;
        if (path == null || path.isEmpty()) {
            this.file = new File(FileUtils.replaceExtensions(name) + '.' + typ.suffix);
        } else {
            this.file = new File(path.replace("\\", "/") +
                File.separator + FileUtils.replaceExtensions(name) + '.' + typ.suffix);
        }
    }

    protected FileSection(@NotNull final File fle, @NotNull final FileType typ) {
        this.file = fle;
        this.type = typ;
        if (typ != FileType.fromExtension(fle).orElse(FileType.NONE)) {
            throw new ConfigsValidationException(
                "Invalid file-extension for file type: '" + typ + '\'',
                "Extension: '" + FileUtils.getExtdidension(fle) + '\'');
        }
    }

    protected FileSection(@NotNull final File fle) {
        this.file = fle;
        this.type = FileType.fromFile(fle).orElse(FileType.NONE);
    }

    @Override
    public Object get(final String key) {
        reloadIfNeeded();
        final String finalKey;
        if (this.pathprefix.isEmpty()) {
            finalKey = key;
        } else {
            finalKey = this.pathprefix + '.' + key;
        }
        return fileData.get(finalKey);
    }

    @Override
    public synchronized void set(@NotNull final String key, @NotNull final Object value) {
        final String finalKey;
        if (this.pathprefix.isEmpty()) {
            finalKey = key;
        } else {
            finalKey = this.pathprefix + '.' + key;
        }
        fileData.insert(finalKey, value);
        this.write();
        this.lastloaded = System.currentTimeMillis();
    }

    @NotNull
    protected abstract void write(@NotNull ConfigurationSection section) throws IOException;

    @NotNull
    protected abstract Map<String, Object> readToMap() throws IOException;

    protected final boolean create() {
        return this.createFile(this.file);
    }

    private synchronized boolean createFile(@NotNull final File fle) {
        final boolean created;
        if (fle.exists()) {
            this.lastloaded = System.currentTimeMillis();
            created = false;
        } else {
            FileUtils.getAndMake(fle);
            this.lastloaded = System.currentTimeMillis();
            created = true;
        }
        return created;
    }

}
