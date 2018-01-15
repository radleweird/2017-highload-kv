package ru.mail.polis.radleweird;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.mail.polis.KVService;
import ru.mail.polis.radleweird.dao.Dao;
import ru.mail.polis.radleweird.dao.DaoSimple;

import java.io.ByteArrayOutputStream;
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
        this.dao = new DaoSimple(dir);
        initStatusHandler();
        initGetPutDeleteHandler();
    }

    @Override
    public void start() {
        server.start();
    }

    @Override
    public void stop() {
        server.stop(0);
    }

    private static final int okCode = 200;
    private static final byte[] response = "we're online, hurrah".getBytes();

    private void initStatusHandler() {
        server.createContext("/v0/status", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                sendResponse(httpExchange, okCode, response);
                httpExchange.close();
            }
        });
    }

    private static final String QUERY_ID = "id";
    private static final int getOkCode = 200;
    private static final int getBadCode = 404;
    private static final int putOkCode = 201;
    private static final int deleteOkCode = 202;
    private static final int badRequestCode = 400;
    private static final int methodNotAllowedCode = 405;

    private void initGetPutDeleteHandler() {
        server.createContext("/v0/entity", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                final Map<String, String> params = QueryWorker.getQueryPairs(httpExchange
                        .getRequestURI()
                        .getQuery());

                final String method = httpExchange.getRequestMethod();
                try {
                    switch (method) {
                        case "GET":
                            try {
                                byte[] data = dao.get(params.get(QUERY_ID));
                                sendResponse(httpExchange, getOkCode, data);
                            } catch (NoSuchElementException e) {
                                sendResponse(httpExchange, getBadCode);
                            }
                            break;

                        case "PUT":
                            try (InputStream inputStream = httpExchange.getRequestBody()) {
                                byte[] data = readAllBytesFrom(inputStream);
                                dao.put(params.get(QUERY_ID), data);
                                sendResponse(httpExchange, putOkCode);
                            }
                            break;

                        case "DELETE":
                            dao.delete(params.get(QUERY_ID));
                            sendResponse(httpExchange, deleteOkCode);
                            break;

                        default:
                            sendResponse(httpExchange, methodNotAllowedCode);
                    }
                }
                catch (IllegalArgumentException | IOException e) {
                    sendResponse(httpExchange, badRequestCode);
                }
                httpExchange.close();
            }
        });
    }

    private void sendResponse(HttpExchange httpExchange, int code, byte[] parcel) throws IOException {
        sendResponse(httpExchange, code, parcel.length);
        httpExchange.getResponseBody().write(parcel);
    }

    private void sendResponse(HttpExchange httpExchange, int code) throws IOException {
        sendResponse(httpExchange, code, 0);
    }

    private void sendResponse(HttpExchange httpExchange, int code, int length) throws IOException {
        httpExchange.sendResponseHeaders(code, length);
    }

    private byte[] readAllBytesFrom(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        final int partSize = 1024;
        byte[] part = new byte[partSize];
        int bytesRead;
        while ((bytesRead = inputStream.read(part)) != -1) {
            byteArray.write(part, 0, bytesRead);
        }
        return byteArray.toByteArray();
    }

}
