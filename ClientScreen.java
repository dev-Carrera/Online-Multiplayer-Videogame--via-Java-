import GameObject.*;
import Data_Structures.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.net.*;



public class ClientScreen extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener{

    // ── Vairables ──────────────────────────────────────────────────────────────

    // Screen Dimensions
    private final int WIDTH = 850;
    private final int HEIGHT = 650;

    // Network Global Variables
    private ObjectOutputStream out;
    private ObjectInputStream in;




    // Players
    private Player p1;
    private String playerID = "";


    private Player p2;


    private PlayerAnimation playerAnimation1;
    private Thread player1Thread;

    private PlayerAnimation playerAnimation2;
    private Thread player2Thread;


    // Ball
    private Ball ball;
    private BallAnimation ballAnimation;
    private Thread ballThread;




    // Game
    private boolean startGameQueue1;
    private boolean startGameQueue2;
    private JButton startGameButton;
    private boolean gameStarted;

    MyHashMap<Position, GameObject> platformCollisionMap = new MyHashMap<Position, GameObject>();
    MyHashMap<Position, GameObject> ballCollisionMap = new MyHashMap<Position, GameObject>();



    // Map
    private Platform platform0 = new Platform(30, 500, 770, 50, "");

    private Platform platform1 = new Platform(30, 70, 20, 500, "");
    private Platform platform2 = new Platform(800, 70, 20, 500, "");

    private Platform platform3 = new Platform(410, 350, 20, 150, "GameObject/Assets/net.png");





    
    // ── Constructor ────────────────────────────────────────────────────────────

    public ClientScreen() {

        p1 = new Player(200, 255, "1");
        p2 = new Player(590, 255, "2");
        ball = new Ball(461, 185, p1, p2);

        playerAnimation1 = new PlayerAnimation(this, p1, ball);
        playerAnimation2 = new PlayerAnimation(this, p2, ball);

        player1Thread = new Thread(playerAnimation1);
        player2Thread = new Thread(playerAnimation2);

        ballAnimation = new BallAnimation(this, ball);
        ballThread = new Thread(ballAnimation);


        addToCollisionMap(platformCollisionMap, platform0);
        addToCollisionMap(platformCollisionMap, platform1);
        addToCollisionMap(platformCollisionMap, platform2);
        addToCollisionMap(platformCollisionMap, platform3);

        addToCollisionMap(ballCollisionMap, ball);





        startGameQueue1 = false;
        gameStarted = false;

        startGameButton = new JButton("Start Game");
        startGameButton.setBounds(300, 300, 100, 30);
        startGameButton.setFocusable(false);
        startGameButton.addActionListener(this);
        this.add(startGameButton);



        this.setLayout((LayoutManager)null);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);


    }



    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        g.drawString(playerID, 10, 10);

        platform0.drawMe(g);
        platform1.drawMe(g);
        platform2.drawMe(g);
        platform3.drawMe(g);

        p1.drawMe(g);
        p2.drawMe(g);
        ball.drawMe(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    // ── Input ──────────────────────────────────────────────────────────────────

    public void actionPerformed(ActionEvent e) {
        if (startGameButton == e.getSource()) {
            if (playerID.equals("1")){ startGameQueue1 = true; }
            if (playerID.equals("2")){ startGameQueue2 = true; }
            try{
                out.reset();
                out.writeObject(playerID + ":connected");

            } catch(Exception ex){
                System.out.println(ex);
            }
            startGameButton.setVisible(false);
        }
    }

    public void keyPressed(KeyEvent e) {    

        if (startGameQueue1 && startGameQueue2 || gameStarted) {
            String message = null;
            if (e.getKeyCode() == 87) {
                if (playerID.equals("1")) {
                    p1.jump();
                    message = "1:jump";
                }
                if (playerID.equals("2")) {
                    p2.jump();
                    message = "2:jump";
                }
            }
            
            if (message != null) {
                try{
                    out.reset();
                    out.writeObject(message);

                } catch(Exception ex){
                    System.out.println(ex);
                }
            }
        }
        
    }


    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {}



    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        System.out.println("x: " + e.getX() + " | y: " + e.getY());
    }

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}
    // ── Networking (stub — left for you to implement) ─────────────────────────

    public void connect() {
        // String hostName = "10.210.109.242";
        String hostName = "localhost";
		int portNumber = 1023;

		try {
			Socket socket = new Socket(hostName, portNumber);

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
            
            
            System.out.println("Waiting for player id...");
            playerID = (String)in.readObject();
            p1.setID(playerID);
            if (playerID.equals("1")) {
                p2.setID("2");
            }
            else{
                p2.setID("1");
            }
            
            repaint();

			while (true) {
				try{
                    System.out.println("Waiting for input...");
					Object obj = in.readObject();
					if(obj instanceof String){
                        String[] message = ((String)obj).split(":");

                        if (message[0].equals("1")) {
                            if (message[1].equals("jump")) {
                                p1.jump();
                            }

                            if (message[1].equals("connected")) {
                                startGameQueue1 = true;
                            }
                        }
                        else if (message[0].equals("2")) {
                            if (message[1].equals("jump")) {
                                p2.jump();
                            }
                            
                            if (message[1].equals("connected")) {
                                startGameQueue2 = true;
                            }
                        }
                        else if(message[0].equals("start game")){
                            if (gameStarted == false) {
                                player1Thread.start();
                                player2Thread.start();
                                ballThread.start();
                                startGameQueue1 = false;
                                gameStarted = true;
                            }
                        }
                    }
                    

					repaint();
				} catch(Exception e){
                    System.out.println(e);
                }
			}


		} catch (Exception e) {
			System.out.println(e);
		}
    }

    public void addToCollisionMap(MyHashMap<Position, GameObject> map, GameObject gameObject){
        for(int row = gameObject.getX(); row < gameObject.getX() + gameObject.getWidth(); row++){
            for(int col = gameObject.getY(); col < gameObject.getY() + gameObject.getHeight(); col++){
                map.put(new Position(row, col), gameObject);
            }
        }
    }

    public synchronized void updateCollisionMap(MyHashMap<Position, GameObject> map, GameObject gameObject, int oldX, int oldY){

        for(int row = oldX; row < oldX + gameObject.getWidth(); row++){
            for(int col = oldY; col < oldY + gameObject.getHeight(); col++){
                map.remove(new Position(row, col));
            }
        }
    
        for(int row = gameObject.getX(); row < gameObject.getX() + gameObject.getWidth(); row++){
            for(int col = gameObject.getY(); col < gameObject.getY() + gameObject.getHeight(); col++){
                map.put(new Position(row, col), gameObject);
            }
        }
    }



    public MyArrayList<String> collision(MyHashMap<Position, GameObject> collisionMap, GameObject gameObject){

        int x = gameObject.getX();
        int y = gameObject.getY();
        int width = gameObject.getWidth();
        int height = gameObject.getHeight();


        MyArrayList<String> myCollisions = new MyArrayList<String>();
    
        // Top
        for(int row = x + 5; row < x + width - 5; row++){
            for (int col = 0; col < y + 5; col++) {
                if(collisionMap.containsKey(new Position(row, y))){
                    myCollisions.add("top");
                }
            }
        }
    
        // Bottom
        for(int row = x + 5; row < x + width - 5; row++){
            if(collisionMap.containsKey(new Position(row, y + height))){
                myCollisions.add("bottom");

                Position pos = new Position(row, y + height);

                if (collisionMap.get(pos).getType().equals("Platform")) {
                    GameObject obj = collisionMap.get(pos);
                    gameObject.setY(obj.getY() - height);
                }

            }
        }
    
        // Left
        for(int col = y + 10; col < y + height - 10; col++){
            if(collisionMap.containsKey(new Position(x, col))){
                myCollisions.add("left");
            }
        }
    
        // Right
        for(int col = y + 10; col < y + height - 10; col++){
            if(collisionMap.containsKey(new Position(x + width, col))){
                myCollisions.add("right");
            }
        }
    
        return myCollisions;
    }

     
    
}