import javax.swing.JFrame;
import java.util.Scanner;
import java.io.*;

public class Client {
    public static void main(String [] args) throws IOException{

        // Scanner kb = new Scanner(System.in);
        // System.out.print("Enter username: ");
        // String username = kb.nextLine();
        
        ClientScreen screen = new ClientScreen();
        JFrame frame  = new JFrame("Game");

        frame.add(screen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        screen.connect();
    }
}

