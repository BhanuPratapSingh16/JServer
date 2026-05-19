package src.server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import src.utils.ContentTypeResolver;
import src.utils.ExceptionHandler;

public class HttpServer{
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server started on port 8080");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");
            try{
                // Read request
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                // Parse request
                HttpRequest request = RequestParser.parse(in);
    
                // Locate the application
                Path appPath = ApplicationManager.findApp(request);
    
                // Handle resouce not found
                if(appPath == null){
                    String response = ExceptionHandler.throwNotFoundError();
    
                    OutputStream out = clientSocket.getOutputStream();
                    out.write(response.getBytes());
    
                    out.flush();
                }
                // Handle application found
                else{
                    // Read user routes configuration file
                    Map<String, String> routes = ConfigManager.loadRoutes(appPath);
    
                    // Resolve the route
                    String path = request.getFileName();
                    path = "/" + path;
    
                    if(path.endsWith(".html")){
                        path = path.substring(0, path.length()-5);
                    }

                    // Load and read the file
                    String fileName;
                    if(path.endsWith(".html") || path.endsWith("/")){
                        fileName = RouteResolver.resolve(path, routes);
                    }
                    else{
                        fileName = path.substring(1);
                    }

                    String contentType = ContentTypeResolver.resolve(fileName);

                    Path filePath = appPath.resolve(fileName);
                    String fileContent = Files.readString(filePath);
    
                    // Create response object
                    HttpResponse response = new HttpResponse(200, contentType, fileContent);
                    
                    // Send response back to client
                    OutputStream out = clientSocket.getOutputStream();
    
                    out.write(response.toHttpResponse().getBytes());
                    out.flush();
                }
                clientSocket.close();
            }
            catch(Exception e){
                OutputStream out = clientSocket.getOutputStream();
                out.write(ExceptionHandler.throwInternalServerError().getBytes());
                out.flush();
                e.printStackTrace();
            }
        }
    }
}