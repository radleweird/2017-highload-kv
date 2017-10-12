package ru.mail.polis.radleweird.dao;

public interface Dao {
    void put(String key, byte[] data);
    byte[] get(String key);
    void delete(String key);
}
