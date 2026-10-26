import GameObject.*;
import Data_Structures.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.net.*;

public class ClientScreen extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener{

    // Screen Dimensions
    private final int WIDTH = 850;
    private final int HEIGHT = 650;

    // Network
    public ObjectOutputStream out;
    private ObjectInputStream in;

    // Players
    private Player p1;
    public String playerID = "";
    private Player p2;

    private PlayerAnimation playerAnimation1;
    private Thread player1Thread;

    private PlayerAnimation playerAnimation2;
    private Thread player2Thread;

    // Sync buffers — Player 1 writes these via network; Player 2 reads them.
    public String copyOfPlayer1;
    public String copyOfPlayer2;
    public String copyOfBall;

    // Ball
    private Ball ball;
    private BallAnimation ballAnimation;
    private Thread ballThread;

    // Game
    private boolean startGameQueue1;
    private boolean startGameQueue2;
    private JButton startGameButton;
    private boolean gameStarted;

    // Score
    private int player1Score;
    private int player2Score;

    public MyHashMap<Position, GameObject> platformCollisionMap = new MyHashMap<Position, GameObject>();
    public MyHashMap<Position, GameObject> ballCollisionMap = new MyHashMap<Position, GameObject>();

    // Map
    private Platform platform0 = new Platform(30, 500, 770, 50, "");
    private Platform platform1 = new Platform(30, 70, 20, 500, "");
    private Platform platform2 = new Platform(800, 70, 20, 500, "");
    private Platform platform3 = new Platform(410, 350, 20, 150, "GameObject/Assets/net.png");


    // ── Constructor ───────────────────────────────────────────────────────────

    public ClientScreen() {

        p1 = new Player(200, 255, "1");
        p2 = new Player(590, 258, "2");
        ball = new Ball(355, 185);

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

        player1Score = 0;
        player2Score = 0;

        startGameQueue1 = false;
        gameStarted = false;

        startGameButton = new JButton("Start Game");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 22));
        startGameButton.setBounds(285, 320, 280, 60);
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

        if(player1Score >= 15){
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Player 1 Wins", 285, 200);
            playerAnimation1.stopThread();
            playerAnimation2.stopThread();
            ballAnimation.stopThread();
        }
        if (player2Score >= 15) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Player 2 Wins", 285, 200);
            playerAnimation1.stopThread();
            playerAnimation2.stopThread();
            ballAnimation.stopThread();
        }

        if (gameStarted && (player1Score < 15 && player2Score < 15) ) {
            g.drawString(playerID, 10, 10);
            g.drawString("First to 25 Wins", 30, 20);
            g.drawString(player1Score + "  :  " + player2Score, 285, 20);


            platform0.drawMe(g);
            platform1.drawMe(g);
            platform2.drawMe(g);
            platform3.drawMe(g);

            p1.drawMe(g);
            p2.drawMe(g);
            ball.drawMe(g);
        }
        if(!gameStarted && player1Score == 0 && player2Score == 0){
            g.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics metrics = g.getFontMetrics();
            String title = "Random Volleyball";
            int x = (WIDTH - metrics.stringWidth(title)) / 2;

            g.drawString(title, x, 120);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    // ── Input ─────────────────────────────────────────────────────────────────

    public void actionPerformed(ActionEvent e) {
        if (startGameButton == e.getSource()) {
            if (playerID.equals("1")){ startGameQueue1 = true; }
            if (playerID.equals("2")){ startGameQueue2 = true; }
            try{
                synchronized(out){
                    out.reset();
                    out.writeObject(playerID + ":connected");
                    out.flush();
                }
            } catch(Exception ex){
                System.out.println(ex);
            }
            startGameButton.setVisible(false);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (startGameQueue1 && startGameQueue2 || gameStarted) {
            String message = null;

            if (e.getKeyCode() == KeyEvent.VK_O) {
                player1Score = 15;
                repaint();
                message = "score:15,0";
            }
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
                    synchronized(out){
                        out.reset();
                        out.writeObject(message);
                        out.flush();
                    }
                } catch(Exception ex){
                    System.out.println(ex);
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        // System.out.println("x: " + e.getX() + " | y: " + e.getY());
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}

    // ── Networking ────────────────────────────────────────────────────────────

    public void connect() {
        String hostName = "localhost";
        
        int portNumber = 1023;

        try {
            Socket socket = new Socket(hostName, portNumber);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Waiting for player id...");
            playerID = (String)in.readObject();
            p1.setID("1");
            p2.setID("2");
            

            repaint();

            while (true) {
                try{
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
                        // Sync messages from Player 1's authority simulation
                        else if(message[0].equals("syncBall")){
                            copyOfBall = message[1];
                        }
                        else if(message[0].equals("syncPlayer1")){
                            copyOfPlayer1 = message[1];
                        }
                        else if(message[0].equals("syncPlayer2")){
                            copyOfPlayer2 = message[1];
                        }
                        else if(message[0].equals("score")){
                            // Receive score update broadcast from Player 1
                            String[] parts = message[1].split(",");
                            player1Score = Integer.parseInt(parts[0]);
                            player2Score = Integer.parseInt(parts[1]);
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

    // ── Collision helpers ─────────────────────────────────────────────────────

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

    public synchronized MyArrayList<String> collision(MyHashMap<Position, GameObject> collisionMap, GameObject gameObject){
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

    // ── Score ─────────────────────────────────────────────────────────────────

    public void updateScore(String player) {
        if (player.equals("1")) {
            player1Score++;
        } else {
            player2Score++;
        }

        // Broadcast the new score to Player 2 so both screens stay in sync
        try {
            String msg = "score:" + player1Score + "," + player2Score;
            synchronized(out){
                out.reset();
                out.writeObject(msg);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void playSound() {
        try {
            File file = new File("GameObject/Assets/hitSound.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }


    
}