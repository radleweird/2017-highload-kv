package ru.mail.polis.radleweird.dao;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public class DaoSimple implements Dao {

    private final File dir;

    public DaoSimple(File dir) {
        this.dir = dir;
    }

    @Override
    public void put(@NotNull String key, @NotNull byte[] object) throws IOException {
        throwIfEmpty(key);
        Path path = Paths.get(dir.getPath(), key);

        Files.write(path, object);
    }

    @Override
    @NotNull
    public byte[] get(@NotNull String key) throws IOException {
        throwIfEmpty(key);
        Path path = Paths.get(dir.getPath(), key);

        if (Files.notExists(path)) {
            throw new NoSuchElementException();
        }

        return Files.readAllBytes(path);
    }

    @Override
    public void delete(@NotNull String key) throws IOException {
        throwIfEmpty(key);
        Path path = Paths.get(dir.getPath(), key);

        Files.deleteIfExists(path);
    }

    private void throwIfEmpty(String key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
