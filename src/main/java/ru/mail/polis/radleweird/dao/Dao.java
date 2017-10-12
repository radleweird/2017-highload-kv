package ru.mail.polis.radleweird.dao;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Dao {
    void put(@NotNull String key, byte[] data) throws IOException;
    byte[] get(@NotNull String key) throws IOException;
    void delete(@NotNull String key) throws IOException;
}
