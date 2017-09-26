package ru.mail.polis.radleweird;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.mail.polis.KVService;
import ru.mail.polis.radleweird.dao.Dao;
import ru.mail.polis.radleweird.dao.DaoString;

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
        final int rcode = 200;
        final byte[] response = "we're online, hurrah".getBytes();
        server.createContext("/v0/status", new HttpErrorHandler(
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange httpExchange) throws IOException {
                        httpExchange.sendResponseHeaders(rcode, response.length);
                        httpExchange.getResponseBody().write(response);
                        httpExchange.close();
                    }
                }
        ));
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
