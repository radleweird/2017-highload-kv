package ru.mail.polis.radleweird.dao;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.integration.CacheLoader;
import org.cache2k.integration.ExceptionInformation;
import org.cache2k.integration.ExceptionPropagator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public class DaoSimple implements Dao {

    private final File dir;
    private final Cache<String, ByteBuffer> cache;

    public DaoSimple(File dir) {
        this.dir = dir;
        this.cache = new Cache2kBuilder<String, ByteBuffer>() {}
                .loader(new CacheLoader<String, ByteBuffer>() {
                    @Override
                    public ByteBuffer load(String key) throws Exception {
                        Path path = Paths.get(dir.getPath(), key);
                        if (Files.notExists(path)) {
                            /* Workaround:
                               загрузка выполняется в background, нельзя бросить переключение
                               и вернуть управление в метод DaoSimple.put
                             */
                            return null;
                        }
                        return ByteBuffer.wrap(Files.readAllBytes(path));
                    }
                })
                .exceptionPropagator(new ExceptionPropagator<String>() {
                    @Override
                    public RuntimeException propagateException(String key, ExceptionInformation exceptionInformation) {
                        return new NoSuchElementException();
                    }
                })
                // По умолчанию cache2k не разрешает хранить null-данные
                .permitNullValues(true)
                // Сохраняем навсегда
                .eternal(true)
                // Максимальная вместительность
                .entryCapacity(5000)
                .build();
    }

    @Override
    public void put(@NotNull String key, @NotNull byte[] object) throws IOException {
        throwIfEmpty(key);
        Path path = Paths.get(dir.getPath(), key);

        Files.write(path, object);
        cache.put(key, ByteBuffer.wrap(object));
    }

    @Override
    @NotNull
    public byte[] get(@NotNull String key) {
        throwIfEmpty(key);

        ByteBuffer byteBuffer = cache.get(key);
        if (byteBuffer != null) {
            return byteBuffer.array();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void delete(@NotNull String key) throws IOException {
        throwIfEmpty(key);
        Path path = Paths.get(dir.getPath(), key);

        Files.deleteIfExists(path);
        cache.remove(key);
    }

    private void throwIfEmpty(String key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
