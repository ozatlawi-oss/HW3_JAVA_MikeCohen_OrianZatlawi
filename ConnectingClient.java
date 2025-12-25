import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ConnectingClient {
    public static void main(String[] args) throws IOException {
        Socket kkSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int socketNum;
        Scanner scanner = new Scanner(System.in);

        // Corrected Port Validation Logic
        System.out.println("Enter Port: 4444 for KnockKnock, 4445 for RuppinRegistration");
        while (true) {
            if (scanner.hasNextInt()) {
                socketNum = scanner.nextInt();
                if (socketNum == 4444 || socketNum == 4445) {
                    break;
                }
            } else {
                scanner.next(); // clear invalid input
            }
            System.out.println("Invalid Port. Please enter 4444 or 4445:");
        }

        try {
            kkSocket = new Socket("127.0.0.1", socketNum);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to port: " + socketNum);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        // Main communication loop [cite: 127]
        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);

            // Handle termination for both protocols
            // 1. KnockKnock ends with "Bye." [cite: 129, 230]
            // 2. Registration ends when Server closes the connection [cite: 43]
            if (fromServer.equalsIgnoreCase("Bye.") || fromServer.contains("Registration complete")) {
                break;
            }

            fromUser = stdIn.readLine();
            if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                out.println(fromUser);
            }
        }

        out.close();
        in.close();
        stdIn.close();
        kkSocket.close();
    }
}