package GameObject;
public abstract class GameObject {
    
    private String type;

    public GameObject(String type){
        this.type = type;
    }

    public String getType(){ return type; }

    public abstract int getX();
    public abstract int getY();
    public abstract void setX(int x);
    public abstract void setY(int y);

    public abstract int getWidth();
    public abstract int getHeight();

}
