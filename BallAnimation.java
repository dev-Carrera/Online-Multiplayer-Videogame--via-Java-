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

            if (screen.playerID.equals("1")) {
                // Player 1 is authority: run ball physics then broadcast
                int ballX = ball.getX();
                int ballY = ball.getY();

                ball.gravity();
                ball.movement();

                screen.updateCollisionMap(screen.ballCollisionMap, ball, ballX, ballY);

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
                    

                    // Score check — use saved Y before collision snap
                    if (ballY > 450) {
                        if (ballX > 420) {
                            screen.updateScore("1");
                        } else if (ballX < 420) {
                            screen.updateScore("2");
                        }
                    }
                } else {
                    ball.setGrounded(false);
                }

                // Broadcast ball position
                try {
                    String msg = "syncBall:" + ball.getX() + "," + ball.getY();
                    synchronized(screen.out){
                        screen.out.reset();
                        screen.out.writeObject(msg);
                        screen.out.flush();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

            } else {
                // Player 2 just receives ball position from network
                if (screen.copyOfBall != null) {
                    String[] parts = screen.copyOfBall.split(",");
                    ball.setX(Integer.parseInt(parts[0]));
                    ball.setY(Integer.parseInt(parts[1]));
                }
            }

            try {
                Thread.sleep(30);
            } catch (Exception e) {
                System.out.println(e);
            }

            screen.repaint();
        }
    }

    public void updateBall(Ball ball) {
        this.ball = ball;
    }
}