package ru.mail.polis.radleweird.dao;

public interface Dao<T> {
    void put(String key, T object);
    T get(String key);
    void delete(String key);
}
