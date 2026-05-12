import Data_Structures.MyArrayList;

public class Manager {
    
    private MyArrayList<ServerThread> threads;
    private MyArrayList<String> usernames = new MyArrayList<String>();
    private ServerScreen screen;
    private int readyCount = 0;
    
    
    public Manager(ServerScreen screen){
        this.screen = screen;
        

        threads = new MyArrayList<ServerThread>();
    }

    public void addThread(ServerThread newThread) {
        threads.add(newThread);
    }

    public void addUsername(String username) {
        usernames.add(username);
        screen.repaint();
    }

    public MyArrayList<String> getUsernames() {
        return usernames;
    }

    public void broadcast(String message, ServerThread sender){
        for (ServerThread each : threads) {
            if (each != sender) {
                each.send(message);
            }
        }
    }

    public void broadcast(String message){
        for (ServerThread each : threads) {
            each.send(message);
        }
    }



    public void ready(String messsage) {
        readyCount++;
        if (readyCount == threads.size()) {
            broadcast("start game: ");
        }
    }

    public int playerCount(){
        return threads.size();
    }
}
