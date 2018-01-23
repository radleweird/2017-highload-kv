package ru.mail.polis.radleweird.dao;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Dao {
    void put(@NotNull String key, @NotNull byte[] data) throws IOException;
    @NotNull
    byte[] get(@NotNull String key) throws IOException;
    void delete(@NotNull String key) throws IOException;
}
