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

            // Extract app name, path and method
            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts[1];
            String version = parts[2];

            String[] pathParts = path.substring(1).split("/");
            String appName = pathParts[0];
            String fileName = pathParts[1];

            // Parse headers
            Map<String, String> headers = new HashMap<>();
            String line;
            while (!(line = in.readLine()).isEmpty()) {
                int separator = line.indexOf(":");
                String key = line.substring(0, separator).trim();
                String value = line.substring(separator+1).trim();
                headers.put(key, value);
            }

            HttpRequest request = new HttpRequest(path, method, version, appName, fileName, headers);

            System.out.println(request.getAppName());
            System.out.println(request.getFileName());
            
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