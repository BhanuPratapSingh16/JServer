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

    public String toHttpResponse(){
        return "HTTP/1.1 " + statusCode + " OK\r\n" +
               "Content-Type: " + contentType + "\r\n" + 
               "Content-Length: "+ body.length() + "\r\n" + 
               "\r\n" + 
               body;
    }
}
