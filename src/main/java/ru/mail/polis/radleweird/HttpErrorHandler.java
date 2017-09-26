package ru.mail.polis.radleweird;

import javax.xml.ws.spi.http.HttpExchange;
import javax.xml.ws.spi.http.HttpHandler;
import java.io.IOException;

public class HttpErrorHandler extends HttpHandler {

    private final HttpHandler essence;

    public HttpErrorHandler(HttpHandler essence) {
        this.essence = essence;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            essence.handle(exchange);
        }
        //TODO
        catch (Exception e) {

        }
    }
}
