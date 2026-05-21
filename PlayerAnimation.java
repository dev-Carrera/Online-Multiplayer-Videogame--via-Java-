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

            if (screen.playerID.equals("1")) {
                // Player 1 is authority: run physics then broadcast position
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

                // Send this player's position to server
                try {
                    String tag = player.getID().equals("1") ? "syncPlayer1" : "syncPlayer2";
                    String msg = tag + ":" + player.getX() + "," + player.getY();
                    synchronized(screen.out){
                        screen.out.reset();
                        screen.out.writeObject(msg);
                        screen.out.flush();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

            } else {

                // Player 2 is receiver: just apply positions from network
                String copy = player.getID().equals("1") ? screen.copyOfPlayer1 : screen.copyOfPlayer2;
                if (copy != null) {
                    String[] parts = copy.split(",");
                    player.setX(Integer.parseInt(parts[0]));
                    player.setY(Integer.parseInt(parts[1]));
                }
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