package src.utils;

public class ContentTypeResolver {
    public static String resolve(String fileName){
        if(fileName.endsWith(".html"))  return "text/html";
        if(fileName.endsWith(".css"))   return "text/css";
        if(fileName.endsWith(".js"))   return "application/js";
        if(fileName.endsWith(".json"))   return "application/json";
        return "text/plain";
    }
}
