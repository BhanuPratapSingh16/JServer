package src.server;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer{
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server started on port 8080");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");
            
            ClientHandler.execute(clientSocket);

            clientSocket.close();
        }
    }
}