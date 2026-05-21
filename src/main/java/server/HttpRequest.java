package server;

import java.util.Map;

public class HttpRequest {
    private String path;
    private String method;
    private String version;
    private String appName;
    private String fileName;
    private Map<String, String> headers;
    private Map<String, String> body;
    private Map<String, Object> jsonBody;

    public HttpRequest(String path, String method, String version, String appName, String fileName, Map<String, String> headers, Map<String, String> body, Map<String, Object> jsonBody) {
        this.path = path;
        this.method = method;
        this.version = version;
        this.appName = appName;
        this.fileName = fileName;
        this.headers = headers;
        this.body = body;
        this.jsonBody = jsonBody;
    }

    public String getPath() {
        return this.path;
    }

    public String getMethod() {
        return this.method;
    }

    public String getVersion() {
        return this.version;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public Map<String, String> getBody(){
        return this.body;
    }
    
    public Map<String, Object> getJsonBody(){
        return this.jsonBody;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(Map<String, String> body){
        this.body = body;
    }

    public void setJsonBody(Map<String, Object> jsonBody){
        this.jsonBody = jsonBody;
    }

}
