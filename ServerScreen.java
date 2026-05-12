import Data_Structures.MyArrayList;

import javax.swing.*;
import java.awt.*;


import java.io.*;
import java.net.*;

public class ServerScreen extends JPanel {
    private Manager manager;
    private String ipAddress = "localhost";

    public ServerScreen(){
        manager = new Manager(this);
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            System.out.println("Could not find IP address for this host");
        }

        setLayout(null);
        setFocusable(true);
    }

    public Dimension getPreferredSize() {
		return new Dimension(800,600);
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        g.drawString("Server IP: " + ipAddress, 50, 50);
        g.drawString("Connected Players: ", 50, 80);
        for (int i = 0; i < manager.playerCount(); i++) {
            g.drawString("player " + (i + 1), 50, 110 + (i*20));
        }
	}

    public void startServer(){
        int portNumber = 1023;

		try {
			ServerSocket server = new ServerSocket(portNumber);

            while (true) {
                System.out.println("Waiting for a connection");
                Socket clientSocket = server.accept();

                ServerThread st = new ServerThread(clientSocket, manager);
                manager.addThread(st);

                Thread thread = new Thread(st);
                thread.start();
                repaint();
            }
		} catch(IOException e) {
			System.err.println("Couldn't get I/O for the connection");
			System.exit(1);
		}
    }

}
