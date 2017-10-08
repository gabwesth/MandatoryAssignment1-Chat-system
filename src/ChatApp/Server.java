//package ChatApp;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Scanner;
//import java.util.regex.PatternSyntaxException;
//
///**
// * Created by gabriele on 04/10/2017.
// */
//public class Server {
//
//    String user, ip, port;
//
//    Socket connection;
//
//    String joinM,msg,text;
//
//    Scanner input;
//    PrintWriter output;
//
//    public Server(Socket connection){
//        this.connection = connection;
//        try{
//            input = new Scanner(connection.getInputStream());
//            output = new PrintWriter(connection.getOutputStream(), true);
//        }catch (IOException ioe){
//            ioe.printStackTrace();
//        }
//    }
//
//
//    public void run(){
//        try {
//            input = new Scanner(connection.getInputStream());
//            output = new PrintWriter(connection.getOutputStream(), true);
//
//            String join = input.nextLine();
//            System.out.println(join);
//
//            checkJoin(join);
//            startChat();
//            connection.close();
//
//        }catch (IOException ioe){
//            ioe.printStackTrace();
//        }
//    }
//
//    private void checkJoin(String join){
//        String[] splitArray = join.split(", ");
//        String one = splitArray[0];
//        String[] divideOne = one.split(" ");
//        String two = splitArray[1];
//        String[] divideTwo = two.split(":");
//
//        joinM = divideOne[0];
//        user = divideOne[1];
//        ip = divideTwo[0];
//        port = divideTwo[1];
//
//        if(joinM.equals("JOIN")){
//            output.println("J_OK");
//            System.out.println("new user: " + user);
//
//
//
//        }else{
//            output.println("J_ER MissingJoin: Wrong Protocol Statemenet");
//        }
//    }
//
//    private void startChat(){
//        do{
//            msg = input.nextLine();
//            String[] divide = msg.split(": ");
//            text = divide[1];
//            if(msg.startsWith("DATA")) {
//                System.out.println(user + " > " + text);
//                output.println("DATA " + user + ": " + text);
//            }else{
//                output.println(user + ": " + text);
//            }
//
//        }while(!msg.equals("QUIT"));
//        output.println("bye bye!");
//    }
//
//}
