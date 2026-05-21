package src.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import src.parser.FormParser;

public class RequestParser {
    public static HttpRequest parse(BufferedReader in) throws IOException {
        String requestLine = in.readLine();
    
        // Extract app name, path and method
        String[] parts = requestLine.split(" ");
        String method = parts[0];
        String path = parts[1];
        String version = parts[2];

        String[] pathParts = path.substring(1).split("/");
        String appName = pathParts[0];
        String fileName = "";
        if(pathParts.length == 2){
            fileName = pathParts[1];
        }

        if(method.equalsIgnoreCase("GET") && fileName.indexOf("?") != -1){
            fileName = fileName.split("\\?")[0];  // Extract only file name and not the form data
        }

        // Parse headers
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            int separator = line.indexOf(":");
            String key = line.substring(0, separator).trim();
            String value = line.substring(separator+1).trim();
            headers.put(key, value);
        }

        // Parse body in case of post request
        String contentLengthHeader = headers.get("Content-Length");
        Map<String, String> body = new HashMap<>();
        if(contentLengthHeader != null){
            // Parse form data
            if(headers.get("Content-Type").equals("application/x-www-form-urlencoded")){
                body = FormParser.parse(in, contentLengthHeader);
            }
        }

        // Create request object and return it
        HttpRequest request = new HttpRequest(path, method, version, appName, fileName, headers, body);
        return request;
    }
}
