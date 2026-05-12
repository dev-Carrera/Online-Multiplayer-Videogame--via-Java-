package GameObject;

import java.awt.Graphics;

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
    private final int bounceForce = -28;
    private final int maxFallSpeed = 12;

    // Player Variables
    private Player player1;
    private Player player2;
    

    public Ball(int x, int y, Player player1, Player player2){
        super("Ball");
        this.x = x;
        this.y = y;
        this.player1 = player1;
        this.player2 = player2;
        xDirection = 4;
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
        if (grounded) {
            yVelocity = bounceForce;
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







    public void drawMe(Graphics g){
        g.drawOval(x, y, width, height);
    }

    public synchronized int getX(){ return x; }
    public synchronized int getY(){ return y; }
    public synchronized void setX( int x ){ this.x = x; }
    public synchronized void setY( int y ){ this.y = y; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height; }

}
