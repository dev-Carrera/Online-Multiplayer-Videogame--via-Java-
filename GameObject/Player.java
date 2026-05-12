package GameObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.imageio.ImageIO;
import java.io.File;

public class Player extends GameObject implements Serializable{

    private String id;
    private int hitBallDirection;

    private int x;
    private int y;
    private int width;
    private int height;

    private BufferedImage img;

    // Physics Component Variables
    private final int gravity = 1;
    private final int jumpPower = -16;
    private final int maxFallSpeed = 12;

    private int yVelocity = 0;
    private boolean grounded = false;

    private int xDirection;



    public Player(int x, int y, String id) {
        super("Player");
        this.x = x;
        this.y = y;
        width = 50;
        height = 50;

        xDirection = -5;

        try {
            if (id.equals("1")) {
                img = ImageIO.read(new File("GameObject/Assets/player1.png"));
                hitBallDirection = 4;
            }
            else if (id.equals("2")) {
                img = ImageIO.read(new File("GameObject/Assets/player2.png"));
                hitBallDirection = -4;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawMe(Graphics g){
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        } else {
            g.setColor(Color.black);
            g.fillRect(x, y, width, height);
        }
    }



    public void gravity(){
        if (grounded == false) {
            yVelocity += gravity;

            if (yVelocity >= maxFallSpeed) {
                yVelocity = maxFallSpeed;
            }

            y += yVelocity;
        }
    }

    public void jump(){
        if (grounded) {
            yVelocity = jumpPower;
            grounded = false;
        }
    }

    public void setGrounded(boolean grounded){ 
        this.grounded = grounded;
    }


    public void movement(){
        x += xDirection;
    }

    public void changeDirection(int direction){
        xDirection = direction;
    }

    public void setID(String id){ this.id = id; }
    public String getID(){ return id; }
    
    public int getHitBallDirection(){ return hitBallDirection; }

    public synchronized int getX(){ return x; }
    public synchronized int getY(){ return y; }
    public synchronized void setX(int x){ 
        this.x = x; 
    }
    public synchronized void setY(int y){ 
        this.y = y; 
    }

    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
   
}