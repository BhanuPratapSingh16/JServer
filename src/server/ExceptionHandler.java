package src.server;

import java.io.OutputStream;

public class ExceptionHandler {
    public static String throwInternalServerError(){
        String html = "<h1>500 Internal Server Error</h1><p>Invalid resource request or invalid configuration.</p>";
    
        String response =
                "HTTP/1.1 500 Internal Server Error\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + html.length() + "\r\n" +
                "\r\n" +
                html;

        return response;
    }
}
