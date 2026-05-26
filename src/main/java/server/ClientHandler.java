package server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import utils.ContentTypeResolver;
import utils.ExceptionHandler;

public class ClientHandler implements Runnable {
    private Socket clientSocket;

    private final static List<String> extensions = List.of("css", "js", "json", "png", "jpg");

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            clientSocket.setSoTimeout(5000);
            // Read request
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());

            while (true) {
                // Parse request
                HttpRequest request = RequestParser.parse(in);

                if (request == null) { // Client disconnected
                    break;
                }

                // Locate the application
                Path appPath = ApplicationManager.findApp(request);

                // Handle resouce not found
                if (appPath == null) {
                    String response = ExceptionHandler.throwNotFoundError();
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
                    boolean isHtml = true;

                    for (String ext : extensions) {
                        if (path.endsWith(ext)) {
                            isHtml = false;
                            break;
                        }
                    }

                    if (isHtml) {
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
                            "Connection: keep-alive\r\n" +
                            "\r\n";

                    // Send response back to client
                    out.write(headers.getBytes());
                    out.write(response.getBody());
                    out.flush();
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Connection timeout");
        } catch (SocketException e) {
            System.out.println("Client disconnected");
        } catch (Exception e) {
            OutputStream out;
            try {
                out = clientSocket.getOutputStream();
                out.write(ExceptionHandler.throwInternalServerError().getBytes());
                out.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
