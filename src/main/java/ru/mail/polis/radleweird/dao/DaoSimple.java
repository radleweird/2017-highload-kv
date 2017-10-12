package ru.mail.polis.radleweird.dao;

import java.io.File;
import java.security.InvalidParameterException;

public class DaoSimple implements Dao {

    private final File dir;

    public DaoSimple(File dir) {
        this.dir = dir;
    }

    @Override
    public void put(String key, byte[] object) {
    }

    @Override
    public byte[] get(String key) {
        return null;
    }

    @Override
    public void delete(String key) {

    }
}
