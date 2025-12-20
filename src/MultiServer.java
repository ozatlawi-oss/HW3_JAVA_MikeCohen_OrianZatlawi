import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class MultiServer {
     static List<Client> clientState=new ArrayList<Client>();
    public static void main(String[] args) throws IOException {

        ServerInstance instance1=new ServerInstance(4444,"KnockKnock",clientState);
        new Thread(instance1).start();
        ServerInstance instance2=new ServerInstance(4445,"RuppinRegistrationProtocol",clientState);
        new Thread(instance2).start();

    }

}
