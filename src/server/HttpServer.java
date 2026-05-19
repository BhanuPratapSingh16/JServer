package src.server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HttpServer{
    public static void main(String[] args) throws Exception {
        try{
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
    
                // Create a request object
                HttpRequest request = new HttpRequest(path, method, version, appName, fileName, headers);
    
                // Locate the application
                Path appPath = ApplicationManager.findApp(appName);

                // Handle resouce not found
                if(appPath == null){
                    String html = "<h1>404 Not Found</h1><p>The requested resource was not found on this server.</p>";

                    String response =
                            "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Type: text/html\r\n" +
                            "Content-Length: " + html.length() + "\r\n" +
                            "\r\n" +
                            html;

                    OutputStream out = clientSocket.getOutputStream();
                    out.write(response.getBytes());

                    out.flush();
                }
                // Handle application found
                else{
                    // Read user routes configuration file
                    
                }

                
                clientSocket.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}