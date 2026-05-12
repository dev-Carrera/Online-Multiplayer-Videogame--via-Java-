import java.net.*;
import java.io.*;
import javax.swing.*;


public class Server {
    public static void main(String[] args) throws IOException{
        
        JFrame frame = new JFrame("Server");

		ServerScreen sc = new ServerScreen();
		frame.add(sc);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		sc.startServer();
    }
}
