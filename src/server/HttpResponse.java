package src.server;
public class HttpResponse {
    int statusCode;
    String contentType;
    String body;

    public HttpResponse(int statusCode, String contentType, String body){
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.body = body;
    }
}
