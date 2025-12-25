import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class clientHandlerKnockKnock extends Thread {
    private final Socket clientSocket;
    public clientHandlerKnockKnock(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {
        try(
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));

                ){

            String inputLine, outputLine;
            KnockKnockProtocol kkp = new KnockKnockProtocol();

            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("q"))  break;
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);

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
