import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final String protocol;
    List<Client> clientState;
    public ClientHandler(Socket clientSocket,String protocol, List<Client> clientState) {
        this.clientSocket = clientSocket;
        this.protocol=protocol;
        this.clientState=clientState;
    }

    @Override
    public void run() {
        try(
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));

                ){
            String inputLine, outputLine;
            if(protocol.equals("KnockKnock")) {

                KnockKnockProtocol kkp = new KnockKnockProtocol();

                outputLine = kkp.processInput(null);
                out.println(outputLine);

                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("q")) break;
                    outputLine = kkp.processInput(inputLine);
                    out.println(outputLine);

                }
                out.close();
                in.close();
            }
            else{
                RuppinRegistrationProtocol rp = new RuppinRegistrationProtocol(clientState);


            }

        }
        catch (IOException e){

        }
        finally{
            try{
                clientSocket.close();
            } catch (IOException e) {

            }
        }
    }
}
