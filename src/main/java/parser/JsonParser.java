package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
    public static Map<String, Object> parse(BufferedReader in, String contentLengthHeader) throws IOException {
        int contentLength = Integer.parseInt(contentLengthHeader);
        char[] contentChars = new char[contentLength];
        in.read(contentChars);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonData = mapper.readValue(new String(contentChars), Map.class);
        return jsonData;
    }
}
