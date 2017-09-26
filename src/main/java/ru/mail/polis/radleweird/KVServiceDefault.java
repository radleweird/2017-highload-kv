package ru.mail.polis.radleweird;

import com.sun.net.httpserver.HttpServer;
import ru.mail.polis.KVService;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class KVServiceDefault implements KVService {

    private final int STOP_DELAY = 5;

    private final HttpServer server;
    private final File dir;

    public KVServiceDefault(int port, File dir) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.dir = dir;
    }

    @Override
    public void start() {
        server.start();
    }

    @Override
    public void stop() {
        server.stop(STOP_DELAY);
    }
}
