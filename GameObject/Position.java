package GameObject;

public class Position {

    private int x;
    private int y;
    
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode(){
        return 650 * x + y;
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Position) {
            Position otherPos = (Position)obj;
            return (x == otherPos.getX() && y == otherPos.getY());
        }
        return false;
    }

    public void setPosition(int x, int y){ this.x = x; this.y = y; }
    public int getX(){ return x; }
    public int getY(){ return y; }
}
