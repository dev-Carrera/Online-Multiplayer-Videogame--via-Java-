import Data_Structures.MyArrayList;
import GameObject.Ball;
import GameObject.Player;

public class PlayerAnimation implements Runnable{

    private ClientScreen screen;
    private Player player;
    private Ball ball;

    public PlayerAnimation(ClientScreen screen, Player player, Ball ball){
        this.screen = screen;
        this.player = player;
        this.ball = ball;
    }



    public void run(){

        while (true) {

            player.movement();
            player.gravity();

            MyArrayList<String> platformCollisions = screen.collision(screen.platformCollisionMap, player);

            if (platformCollisions.contains("left")) {
                player.changeDirection(5);
            }

            if (platformCollisions.contains("right")) {
                player.changeDirection(-5);
            }

            if (platformCollisions.contains("bottom")) {
                player.setGrounded(true);
            } else {
                player.setGrounded(false);
            }


            MyArrayList<String> ballCollision = screen.collision(screen.ballCollisionMap, player);

            if (ballCollision.contains("top") || ballCollision.contains("left") || ballCollision.contains("right")) {
                ball.setGrounded(true);
                ball.bounce();
                ball.changeDirection(player.getHitBallDirection());
            }



            screen.repaint();

            try {
                Thread.sleep(30);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

    }

}
