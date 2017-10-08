//package ChatApp;
//
//import jdk.internal.dynalink.linker.LinkerServices;
//
//import java.io.DataInputStream;
//import java.io.PrintStream;
//import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.util.Scanner;
//
///**
// * Created by gabriele on 04/10/2017.
// */
//public class Clients implements Runnable{
//
//    String IP;
//    String username;
//    int port;
//
//    Scanner scanner = new Scanner(System.in);
//    Scanner input;
//    PrintStream output;
//    static  Socket clientSocket;
//
//    String sMessage, rMessage,acceptence;
//
//    Thread thread;
//
//    public static void main(String[] args) {
//
//          Clients client = new Clients();
////        client.start();
////
//
//    }
//
////    public Clients(String IP, int port, String username) {
////        this.IP = IP;
////        this.port = port;
////        this.username = username;
////    }
//
//    public void start() {
//        connectToServer();
//        if (acceptence.equals("J_OK")){
//            runReceivingThread();
//            chat();
//        }else {
//            System.out.println("NO J_OK message from SERVER");
//        }
//    }
//
//
//    private void connectToServer() {
//        try {
//
//            InetAddress inetAddress = InetAddress.getByName(IP);
//            clientSocket = new Socket(inetAddress, port);
//
//            input = new Scanner(clientSocket.getInputStream());
//            output = new PrintStream(clientSocket.getOutputStream(), true);
//
//            output.println("JOIN " + username + ",  " + IP + ":" + port);
//
//            acceptence = input.nextLine();
//            System.out.println(acceptence);
//
//            new Thread(new Clients()).start();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void chat() {
//        sMessage = scanner.nextLine();
//        while (!sMessage.equals("QUIT")){
//
//            output.println("DATA "+username+": "+sMessage);
//            sMessage = scanner.nextLine();
//        }
//
//        output.println("QUIT");
//    }
//
//    private void runReceivingThread(){
//        thread = new Thread(()->{
//            while (true){
//                if(input.hasNextLine()){
//                    System.out.println(input.nextLine());
//                }}}
//        );
//        thread.start();
//    }
//
//}
