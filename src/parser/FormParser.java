package src.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FormParser {
    public static Map<String, String> parse(BufferedReader in, String contentLengthHeader) throws IOException{
        Map<String, String> body = new HashMap<>();

        int contentLength = Integer.parseInt(contentLengthHeader);
        char[] contentChars = new char[contentLength];
        in.read(contentChars);
        
        String[] content = new String(contentChars).split("&");
        for(String s:content){
            String key = s.substring(s.indexOf("="));
            String value = s.substring(s.indexOf("=")+1);
            body.put(key, value);
        }
        return body;
    }
}