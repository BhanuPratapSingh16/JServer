package src.handlers;

import src.server.HttpRequest;
import src.server.HttpResponse;

public interface Handler {
    HttpResponse handle(HttpRequest request);
}
