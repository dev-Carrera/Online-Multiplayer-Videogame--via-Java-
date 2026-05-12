import Data_Structures.MyArrayList;
import GameObject.Ball;
import GameObject.Player;

public class BallAnimation implements Runnable{

    private ClientScreen screen;
    private Ball ball;
    
    public BallAnimation(ClientScreen screen, Ball ball){
        this.screen = screen;
        this.ball = ball;
    }

    public void run(){
        while (true) {

            
            int ballX = ball.getX();
            int ballY = ball.getY();

            ball.gravity();
            ball.movement();

            MyArrayList<String> ballCollisions = screen.collision(screen.platformCollisionMap, ball);

            if (ballCollisions.contains("left")) {
                ball.changeDirection(4);
            }
            if (ballCollisions.contains("right")) {
                ball.changeDirection(-4);
            }
            if (ballCollisions.contains("bottom")) {
                ball.setGrounded(true);
                ball.bounce();
            } else {
                ball.setGrounded(false);
            }

            // screen.updateCollisionMap(screen.ballCollisionMap, ball, ballX, ballY);

            try {
                Thread.sleep(30);
            } catch (Exception e) {
                System.out.println(e);
            }

            screen.repaint();
        }
    }
}
