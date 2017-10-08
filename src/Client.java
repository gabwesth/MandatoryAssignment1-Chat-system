import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable {

    private static Socket clientSocket = null;
    private static PrintStream os = null;
    private static Scanner is = null;

    private static Scanner userInput = null;
    private static boolean closed = false;

    public static void main(String[] args) {

        int portNumber = 2222;
        String host = "localhost";

        setUpClient(portNumber, host);
        writeToServer();
    }

        private static void setUpClient(int portNumber,String host){

            try {
                clientSocket = new Socket(host, portNumber);
                userInput = new Scanner(new InputStreamReader(System.in));
                os = new PrintStream(clientSocket.getOutputStream());
                is = new Scanner(clientSocket.getInputStream());
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + host);
            } catch (IOException ioe){
                ioe.printStackTrace();
            }

        }

        private static void writeToServer(){

            try {
                new Thread(new Client()).start();
                while (!closed) {
                    os.println(userInput.nextLine());
                }

                os.close();
                is.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

        String responseLine = is.nextLine();

            while (responseLine != null) {
                System.out.println(responseLine);
                responseLine = is.nextLine();
                if (responseLine.equals("QUIT"))
                    break;
            }
            closed = true;
    }
}