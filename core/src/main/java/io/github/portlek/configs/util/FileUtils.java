package io.github.portlek.configs.util;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public final class FileUtils {

    private FileUtils() {
    }

    public static boolean hasChanged(@NotNull final File file, final long time) {
        return time < file.lastModified();
    }

    @NotNull
    public static String getExtension(@NotNull final File file) {
        return FileUtils.getExtension(file.getName());
    }

    @NotNull
    public static String getExtension(@NotNull final String path) {
        final String extnsn;
        if (path.lastIndexOf('.') > 0) {
            extnsn = path.substring(path.lastIndexOf('.') + 1);
        } else {
            extnsn = "";
        }
        return extnsn;
    }

    @NotNull
    public static File getAndMake(@NotNull final String name, @NotNull final String path) {
        return FileUtils.getAndMake(new File(path, name));
    }

    @NotNull
    public static File getAndMake(@NotNull final File file) {
        try {
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (final IOException ex) {
            System.err.println("Error while creating file '" + file.getName() + "'.");
            System.err.println("In: '" + FileUtils.getParentDirPath(file) + '\'');
            throw new IllegalStateException(ex);
        }
        return file;
    }

    @NotNull
    public static String getParentDirPath(@NotNull final File file) {
        return FileUtils.getParentDirPath(file.getAbsolutePath());
    }

    public static String getParentDirPath(@NotNull final String path) {
        final String pth;
        if (path.endsWith(File.separator)) {
            pth = path.substring(0, path.lastIndexOf(File.separatorChar, path.length() - 2));
        } else {
            pth = path.substring(0, path.lastIndexOf(File.separatorChar));
        }
        return pth;
    }

    @NotNull
    public static List<File> listFiles(@NotNull final File folder) {
        return FileUtils.listFiles(folder, "");
    }

    @NotNull
    public static List<File> listFiles(@NotNull final File folder, @NotNull final String extension) {
        final File[] files = folder.listFiles();
        final List<File> result;
        if (files == null) {
            result = new ArrayList<>();
        } else {
            result = Arrays.stream(files)
                .filter(file -> extension.isEmpty() || file.getName().endsWith(extension))
                .collect(Collectors.toList());
        }
        return result;
    }

    @NotNull
    public static InputStream createInputStream(@NotNull final File file) {
        try {
            return Files.newInputStream(file.toPath());
        } catch (final IOException ex) {
            System.err.println("Exception while creating InputStream from '" + file.getName() + '\'');
            System.err.println("At: '" + file.getAbsolutePath() + '\'');
            throw new IllegalStateException(ex);
        }
    }

    @NotNull
    public static OutputStream createOutputStream(@NotNull final File file) {
        try {
            return new FileOutputStream(file);
        } catch (final FileNotFoundException ex) {
            System.err.println("Exception while creating OutputStream from '" + file.getName() + '\'');
            System.err.println("At: '" + file.getAbsolutePath() + '\'');
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
    }

    @NotNull
    public static Reader createReader(@NotNull final File file) {
        try {
            return new FileReader(file);
        } catch (final FileNotFoundException ex) {
            System.err.println("Error while creating reader for '" + file.getName() + '\'');
            System.err.println("In '" + FileUtils.getParentDirPath(file) + '\'');
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
    }

    @NotNull
    public static Writer createWriter(@NotNull final File file) {
        try {
            return new FileWriter(file);
        } catch (final IOException ex) {
            System.err.println("Error while creating reader for '" + file.getName() + '\'');
            System.err.println("In '" + FileUtils.getParentDirPath(file) + '\'');
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
    }

    @NotNull
    public static void write(@NotNull final File file, @NotNull final Iterable<String> lines) {
        try {
            Files.write(file.toPath(), lines);
        } catch (final IOException ex) {
            System.err.println("Exception while writing to file '" + file.getName() + '\'');
            System.err.println("In " + FileUtils.getParentDirPath(file) + '\'');
        }
    }

    @NotNull
    public static void writeToFile(@NotNull final File file, @NotNull final InputStream input) {
        try (final FileOutputStream output = new FileOutputStream(file)) {
            if (file.exists()) {
                final byte[] data = new byte[8192];
                int count;
                while ((count = input.read(data, 0, 8192)) != -1) {
                    output.write(data, 0, count);
                }
            } else {
                Files.copy(input, file.toPath());
            }
        } catch (final IOException ex) {
            System.err.println("Exception while copying to + '" + file.getName() + '\'');
            System.err.println("In '" + FileUtils.getParentDirPath(file) + '\'');
            ex.printStackTrace();
        }
    }

    @NotNull
    public static List<String> readAllLines(@NotNull final File file) {
        final byte[] bytes = FileUtils.readAllBytes(file);
        final String strng = new String(bytes);
        return new ArrayList<>(Arrays.asList(strng.split("\n")));
    }

    @NotNull
    public static byte[] readAllBytes(@NotNull final File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (final IOException ex) {
            System.err.println("Exception while reading '" + file.getName() + '\'');
            System.err.println("In '" + FileUtils.getParentDirPath(file) + '\'');
            throw new IllegalStateException(ex);
        }
    }

    @NotNull
    public static String replaceExtensions(@NotNull final String name) {
        final String extension;
        if (name.contains(".")) {
            extension = name.replace('.' + FileUtils.getExtension(name), "");
        } else {
            extension = name;
        }
        return extension;
    }

}
