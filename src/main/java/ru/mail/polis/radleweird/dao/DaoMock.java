package ru.mail.polis.radleweird.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class DaoMock implements Dao {

    Map<String, String> map = new HashMap<>();

    @Override
    public void put(String key, byte[] data) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException();
        }

        map.put(key, new String(data));
    }

    @Override
    public byte[] get(String key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException();
        }

        String data = map.get(key);
        if (data != null) {
            return data.getBytes();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void delete(String key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException();
        }

        map.remove(key);
    }
}
