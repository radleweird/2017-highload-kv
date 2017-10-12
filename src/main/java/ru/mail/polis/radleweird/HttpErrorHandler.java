package ru.mail.polis.radleweird;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HttpErrorHandler implements HttpHandler {

    private final HttpHandler essence;

    public HttpErrorHandler(HttpHandler essence) {
        this.essence = essence;
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            essence.handle(httpExchange);
        }
        //TODO
        catch (Throwable t) {

        }
    }
}

