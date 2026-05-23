package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager{
    public static Map<String, String> loadRoutes(Path appFolder) throws IOException{
        Map<String, String> routes = new HashMap<>();

        // Load routes configuration file
        Path configPath = appFolder.resolve("routes.conf");

        // Read all the lines
        List<String> lines = Files.readAllLines(configPath);

        // Iterate through each line
        for(String line:lines){
            if(line.isEmpty()){
                continue;
            }
            
            String[] parts = line.split("=");
            if(parts.length != 2){
                throw new RuntimeException("Invalid configuration in routes.conf in line "+line);
            }

            String key = parts[0];  // Path
            if(key.endsWith(".html")){
                key = key.substring(0, key.length()-5);
            }

            String value = parts[1];  // Html file to be served

            if(!value.contains(".")){
                value += ".html";
            }

            routes.put(key, value); // Add the route to map
        }

        return routes;
    }
}