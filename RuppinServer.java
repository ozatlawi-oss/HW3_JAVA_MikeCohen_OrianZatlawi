import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RuppinServer {
    public static void main(String[] args) throws IOException {
        List<Client> clientState = java.util.Collections.synchronizedList(new ArrayList<Client>());

        ServerSocket serverSocket = null;
        try
        {
            serverSocket = new ServerSocket(4445);
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: 4445.");
            System.exit(1);
        }



        while(true)
        {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                ClientHandlerRuppin clientHandlerRuppin=new ClientHandlerRuppin(clientSocket,clientState);
                clientHandlerRuppin.start();

            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

        }
    }

    public static void saveToCSV(List<Client> clientState) {
        synchronized (clientState) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmm"));
            String fileName = "backup_" + date + ".txt";

            try (PrintWriter writer = new PrintWriter(new File(fileName))) {
                for (Client c : clientState) {
                    writer.print(c.toString()+",");
                }
                System.out.println("Backup created: " + fileName);
            } catch (IOException e) {
                System.err.println("File error: " + e.getMessage());
            }
        }
    }
    }

