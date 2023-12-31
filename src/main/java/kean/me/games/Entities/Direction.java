package kean.me.games.Entities;

public enum Direction {
    NORTH(0,-1),
    SOUTH(0,1),
    WEST(-1,0),
    EAST(1,0);


    public final int x;
    public final int y;
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Direction getOpposite(){
        if (y == -1) return SOUTH;
        if (y == 1) return NORTH;
        if (x == -1) return EAST;
        if (x == 1) return WEST;
        return null;
    }
}
