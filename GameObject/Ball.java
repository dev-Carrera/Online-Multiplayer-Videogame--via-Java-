package GameObject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Ball extends GameObject{

    // Ball Variables
    private int x;
    private int y;
    private final int width = 25;
    private final int height = 25;

    private int yVelocity = 0;
    private boolean grounded = false;
    private int xDirection;

    // Physic Variables
    private final int gravity = 1;
    private final int bounceForce = -22;
    private final int maxFallSpeed = 12;

    private BufferedImage img;


    private boolean waitDone = true;
    

    public Ball(int x, int y){
        super("Ball");
        this.x = x;
        this.y = y;
        xDirection = -4;

        try {
            img = ImageIO.read(new File("GameObject/Assets/volleyball.png"));
        } catch (Exception e) {
            // e.printStackTrace();
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

    public void bounce(){
        if (grounded && waitDone) {
            yVelocity = bounceForce;
            grounded = false;
            timer();
        }
    }

    private void timer(){
        waitDone = false;
        
        Thread timer = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e);
            }

            waitDone = true;
        });

        timer.start();
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


    public void reset(int x, int y){
        this.x = x;
        this.y = y;
        xDirection = 4;
        yVelocity = gravity;
        grounded = false;
    }




    public void drawMe(Graphics g){
        g.drawImage(img, x - 5, y - 5, width + 15, height + 15, null);
    }

    public synchronized int getX(){ return x; }
    public synchronized int getY(){ return y; }
    public synchronized void setX( int x ){ this.x = x; }
    public synchronized void setY( int y ){ this.y = y; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height; }

}
