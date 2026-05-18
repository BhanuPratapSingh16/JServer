package src.server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServer{
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server started on port 8080");

        while (true) {
            Socket clientSocket = serverSocket.accept();

            System.out.println("Client connected");

            // Read request
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            String requestLine = in.readLine();

            // Parse request line
            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts[1];
            String version = parts[2];

            // Parse headers
            Map<String, String> headers = new HashMap<>();
            String line;
            while (!(line = in.readLine()).isEmpty()) {
                int separator = line.indexOf(":");
                String key = line.substring(0, separator).trim();
                String value = line.substring(separator+1).trim();
                headers.put(key, value);
            }

            HttpRequest request = new HttpRequest(path, method, version, headers);

            System.out.println("Method: "+request.method);
            System.out.println("Path: "+request.path);
            System.out.println("Version: "+request.version);

            System.out.println("Headers:");

            request.headers.forEach((key, value) -> System.out.println(key+": "+value));
            
            // Response body
            String html = "<h1>Hello from World</h1>";

            String response = "HTTP/1.1 200 OK\r\n"+"Content-Type: text/html\r\n" + "Content-Length: " + html.length() + "\r\n" + "\r\n" + html;

            OutputStream out = clientSocket.getOutputStream();
            out.write(response.getBytes());

            out.flush();
            clientSocket.close();
        }
    }
}