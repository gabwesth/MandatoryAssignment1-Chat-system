package ChatApp;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by gabriele on 04/10/2017.
 */
public class Client  {

     String IP;
     String username;
     int host;

    Scanner scanner = new Scanner(System.in);
    Scanner input;
    PrintWriter output;

    String sMessage, rMessage,acceptence;

    Thread thread;

    public static void main(String[] args) {

        Client client = new Client("localhost", 7777, "gab");
        client.start();


    }

    public Client(String IP, int host, String username) {
        this.IP = IP;
        this.host = host;
        this.username = username;
    }

    public void start() {
        connectToServer();
        if (acceptence.equals("J_OK")){
        runReceivingThread();
        chat();
        }else {
            System.out.println("NO J_OK message from SERVER");
        }
    }


    private void connectToServer() {
        try {

            InetAddress inetAddress = InetAddress.getByName(IP);
            Socket connection = new Socket(inetAddress, host);

            input = new Scanner(connection.getInputStream());
            output = new PrintWriter(connection.getOutputStream(), true);

            output.println("JOIN " + username + ", " + IP + ":" + host);

             acceptence = input.nextLine();
             System.out.println(acceptence);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void chat() {
        sMessage = scanner.nextLine();
        while (!sMessage.equals("QUIT")){

            output.println("DATA "+username+": "+sMessage);
            sMessage = scanner.nextLine();
        }

        output.println("QUIT");
    }

    private void runReceivingThread(){
        thread = new Thread(()->{
            while (true){
             if(input.hasNextLine()){
                 System.out.println(input.nextLine());
             }}}
        );
        thread.start();
    }

}
