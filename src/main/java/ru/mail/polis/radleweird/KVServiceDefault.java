package ru.mail.polis.radleweird;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.mail.polis.KVService;
import ru.mail.polis.radleweird.dao.Dao;
import ru.mail.polis.radleweird.dao.DaoMock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.NoSuchElementException;

public class KVServiceDefault implements KVService {

    private final HttpServer server;
    private final Dao dao;

    public KVServiceDefault(int port, File dir) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
//        this.dao = new DaoSimple(dir);
        this.dao = new DaoMock();
        initStatusHandler();
        initGetPutDeleteHandler();
    }

    private void initStatusHandler() {
        final int okCode = 200;
        final byte[] response = "we're online, hurrah".getBytes();

        server.createContext("/v0/status", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                httpExchange.sendResponseHeaders(okCode, response.length);
                httpExchange.getResponseBody().write(response);
                httpExchange.close();
            }
        });
    }

    private final String ID_QUERY = "id";

    private void initGetPutDeleteHandler() {
        server.createContext("/v0/entity", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                final int getOkCode = 200;
                final int getBadCode = 404;
                final int putOkCode = 201;
                final int deleteOkCode = 202;
                final int malformedCode = 400;

                Map<String, String> params = QueryWorker.getQueryPairs(httpExchange
                        .getRequestURI()
                        .getQuery());

                String method = httpExchange.getRequestMethod();
                try {
                    switch (method) {
                        case "GET":
                            try {
                                byte[] data = dao.get(params.get(ID_QUERY));
                                httpExchange.sendResponseHeaders(getOkCode, data.length);
                                httpExchange.getResponseBody().write(data);
                            } catch (NoSuchElementException e) {
                                httpExchange.sendResponseHeaders(getBadCode, 0);
                            }
                            break;
                        case "PUT":
                            try (InputStream inputStream = httpExchange.getRequestBody()) {
                                byte[] data = new byte[inputStream.available()];
                                inputStream.read(data);
                                dao.put(params.get(ID_QUERY), data);
                                httpExchange.sendResponseHeaders(putOkCode, 0);
                            }
                            break;
                        case "DELETE":
                            dao.delete(params.get(ID_QUERY));
                            httpExchange.sendResponseHeaders(deleteOkCode, 0);
                    }
                }
                catch (IllegalArgumentException e) {
                    httpExchange.sendResponseHeaders(malformedCode, 0);
                }
                httpExchange.close();
            }
        });
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
