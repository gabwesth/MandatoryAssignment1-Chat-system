package ChatApp;

import java.util.Scanner;

/**
 * Created by gabriele on 04/10/2017.
 */
public class RunClient {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.println("Enter IP: ");
        String IP = scanner.next();
        System.out.println("Enter Host: ");
        int host = Integer.parseInt(scanner.next());
        System.out.println("Enter Username: ");
        String username = scanner.next();
        System.out.println("Trying to Connect..");
        System.out.println();

        Client client = new Client(IP,host,username);
        client.start();
    }
}
