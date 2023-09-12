package kean.me.games.Entities;

public abstract class Entity extends Object {
    protected Direction direction = Direction.SOUTH;
    protected boolean isDead = false;

    public Entity(String view,int x,int y){
        super(view,x,y);
    }

    public void move(){
        x += direction.x;
        y += direction.y;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isDead() {
        return isDead;
    }

    public int nextX(){
        return x + direction.x;
    }
    public int nextY(){
        return y + direction.y;
    }
}
