package ru.mail.polis.radleweird;

import com.sun.net.httpserver.HttpServer;
import ru.mail.polis.KVService;
import ru.mail.polis.radleweird.dao.Dao;
import ru.mail.polis.radleweird.dao.DaoString;

import javax.xml.ws.spi.http.HttpExchange;
import javax.xml.ws.spi.http.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class KVServiceDefault implements KVService {

    private final HttpServer server;
    private final Dao dao;

    public KVServiceDefault(int port, File dir) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.dao = new DaoString(dir);
        initStatusHandler();
        initGetPutHandler();
        initDeleteHandler();
    }

    private void initStatusHandler() {
    }

    private void initGetPutHandler() {
    }

    private void initDeleteHandler() {
    }


    @Override
    public void start() {
        server.start();
    }

    @Override
    public void stop() {
        server.stop(0);
    }
}