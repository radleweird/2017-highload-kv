package ru.mail.polis.radleweird.dao;

import java.io.File;

public class DaoString implements Dao<String> {

    private final File dir;

    public DaoString(File dir) {
        this.dir = dir;
    }

    @Override
    public void put(String key, String object) {

    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void delete(String key) {

    }
}
