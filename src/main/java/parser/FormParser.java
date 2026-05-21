package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
            String key = URLDecoder.decode(s.substring(0, s.indexOf("=")), StandardCharsets.UTF_8);
            String value = URLDecoder.decode(s.substring(s.indexOf("=")+1), StandardCharsets.UTF_8);
            body.put(key, value);
        }
        System.out.println(body);
        return body;
    }
}