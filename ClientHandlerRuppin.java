import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.List;

public class ClientHandlerRuppin extends Thread {
    private final Socket clientSocket;
    private List<Client> clientState;

    public ClientHandlerRuppin(Socket clientSocket, List<Client> clientState) {
        this.clientSocket = clientSocket;
        this.clientState = clientState;
    }
    @Override
    public void run() {
        try(
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));

        ){

            String inputLine, outputLine;
            RuppinRegistrationProtocol rrp = new RuppinRegistrationProtocol(clientState);

            outputLine = rrp.flowManager(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null  ) {
                outputLine = rrp.flowManager(inputLine);
                if(outputLine != null) {
                    out.println(outputLine);
                }
                if(rrp.getCompleted()){
                    return;
                }

            }
            out.close();
            in.close();

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

