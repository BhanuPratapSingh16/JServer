package src.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import src.utils.ContentTypeResolver;
import src.utils.ExceptionHandler;

public class ClientHandler {
    public static void execute(Socket clientSocket) throws IOException {
        try {
            // Read request
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Parse request
            HttpRequest request = RequestParser.parse(in);

            // Locate the application
            Path appPath = ApplicationManager.findApp(request);

            // Handle resouce not found
            if (appPath == null) {
                String response = ExceptionHandler.throwNotFoundError();

                OutputStream out = clientSocket.getOutputStream();
                out.write(response.getBytes());

                out.flush();
            }
            // Handle application found
            else {
                // Read user routes configuration file
                Map<String, String> routes = ConfigManager.loadRoutes(appPath);

                // Resolve the route
                String path = request.getFileName();
                path = "/" + path;

                if (path.endsWith(".html")) {
                    path = path.substring(0, path.length() - 5);
                }

                // Load and read the file
                String fileName;
                if (path.endsWith(".html") || path.endsWith("/")) {
                    fileName = RouteResolver.resolve(path, routes);
                } else {
                    fileName = path.substring(1);
                }

                String contentType = ContentTypeResolver.resolve(fileName);

                Path filePath = appPath.resolve(fileName);
                byte[] body = Files.readAllBytes(filePath);

                // Create response object
                HttpResponse response = new HttpResponse(200, contentType, body);

                String headers = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + response.getContentType() + "\r\n" +
                        "Content-Length: " + response.getBody().length + "\r\n" +
                        "\r\n";

                // Send response back to client
                OutputStream out = clientSocket.getOutputStream();
                out.write(headers.getBytes());
                out.write(response.getBody());
                out.flush();
            }
        } catch (SocketException e) {
            System.out.println("Client disconnected");
        } catch (Exception e) {
            OutputStream out = clientSocket.getOutputStream();
            out.write(ExceptionHandler.throwInternalServerError().getBytes());
            out.flush();
            e.printStackTrace();
        }
    }
}
