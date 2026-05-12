package GameObject;
import java.awt.Color;
import java.awt.Graphics;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

public class Platform extends GameObject{
    
    private int x;
    private int y;
    private int width;
    private int height;

    private BufferedImage img;

    public Platform(int x, int y, int width, int height, String asset){
        super("Platform");
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try {
            img = ImageIO.read(new File(asset));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawMe(Graphics g){
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        } else {
            g.fillRect(x, y, width, height);
        }
    }

    public int getX(){ return x; }
    public int getY(){ return y; }
    public void setX(int x){ this.x = x; }
    public void setY(int y){ this.y = y; }

    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
}
