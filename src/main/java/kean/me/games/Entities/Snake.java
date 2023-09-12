package kean.me.games.Entities;

import java.util.ArrayDeque;
import java.util.Queue;

public class Snake extends Entity {
    private final Queue<part> parts = new ArrayDeque<>();
    public Snake(int x, int y) {
        super(":yellow_square:",x, y);
    }
    public void grow(){
        parts.add(new part(x - direction.x,y - direction.y));
    }

    @Override
    public void move() {
        if (!isEmptySpace(x+direction.x,y+direction.y)){
            isDead = true;
            return;
        }
        int px = x,py = y;
        super.move();
        for (Snake.part part : parts){
            int npx = part.x,npy = part.y;
            part.x = px;
            part.y = py;
            px = npx;
            py = npy;
        }
    }
    @Override
    public String render(int x, int y) {
        if (isDead) return null;
        if (this.x == x && this.y == y) return view;
        if (parts.stream().anyMatch(part -> part.x == x && part.y == y)) return view;
        return null;
    }

    private static class part {
        public int x,y;
        public part(int x,int y) {
            this.x = x;
            this.y = y;
        }
    }

    public boolean isEmptySpace(int x, int y){
        if (this.x == x && this.y == y) return false;
        return !parts.stream().anyMatch(part -> part.x == x && part.y == y);
    }
}
