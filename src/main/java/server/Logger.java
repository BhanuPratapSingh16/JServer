package server;

import java.net.Socket;

public class Logger {
    public static void log(Socket clientSocket, HttpRequest request, HttpResponse response, long time){
        System.out.println(
            "IP : " + clientSocket.getInetAddress().getHostAddress() + "\t"+
            "METHOD : " + request.getMethod() + "\t"+
            "PATH : " + request.getPath() + "\t"+
            "STATUS CODE : " + response.getStatusCode() + "\t"+
            "SIZE : " + response.getBody().length + " bytes\t" + 
            "TIME : " + time +" ms"
        );
    }
}
