import java.io.IOException;
import java.net.*;
import java.util.List;

public class ServerInstance implements Runnable {
    int port;
    String protocol;
    List<Client> clientState;

    public ServerInstance(int port, String protocol, List<Client> clientState) {
        this.port = port;
        this.protocol = protocol;
        this.clientState = clientState;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket,protocol,clientState);
                Thread thread = new Thread(handler);
                thread.start();
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
