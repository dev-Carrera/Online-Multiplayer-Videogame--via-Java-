
import java.io.*;
import java.net.*;

public class ServerThread implements Runnable{
    private Socket socket;
    private Manager manager;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username = "";
    

    public ServerThread(Socket socket, Manager manager) {
        this.socket = socket;
        this.manager = manager;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
		    
		} catch(IOException e) {
			System.out.println(e);
		}
    }

    @Override
    public void run(){

        int playerID = manager.playerCount();
        try{
            out.writeObject("" + playerID);
        } catch(Exception ex){
            System.out.println(ex);
        }
        
        while (true) {
            try{
                System.out.println("Waiting for input...");

                Object obj = in.readObject();
                if(obj instanceof String){
                    // Code

                    String message = (String)obj;
                    if (message.contains("connected")) {
                        manager.ready(message);
                    }
                    else{
                        manager.broadcast(message, this);
                    }
                }
				
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    public void send(Object obj){
        try {
            out.reset();
            out.writeObject(obj);

        } catch(IOException e) {
			System.out.println(e);
		}
    }






    public String getUsername() {
        return username;
    }


}
