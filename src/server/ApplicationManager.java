package src.server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationManager {
    public static Path findApp(String appName){
        Path appPath = Paths.get("webapps", appName);
        if(Files.exists(appPath)){
            return appPath;
        }
        return null;
    }
}
