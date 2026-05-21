package src.main.java.utils;

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

    public static String throwNotFoundError(){
        String html = "<h1>404 Not Found</h1><p>The requested resource was not found on this server.</p>";
    
        String response =
                "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + html.length() + "\r\n" +
                "\r\n" +
                html;

        return response;
    }
}
