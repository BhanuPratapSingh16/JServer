package src.server;

import java.util.Map;

public class HttpRequest {
    String path;
    String method;
    String version;
    Map<String, String> headers;

    public HttpRequest(String path, String method, String version, Map<String, String> headers){
        this.path = path;
        this.method = method;
        this.version = version;
        this.headers = headers;
    }
}
