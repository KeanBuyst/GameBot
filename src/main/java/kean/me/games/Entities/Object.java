package kean.me.games.Entities;

public abstract class Object {
    protected String view;
    protected int x,y;
    public Object(String view,int x,int y){
        this.view = view;
        this.x = x;
        this.y = y;
    }
    public String render(int x,int y){
        if (this.x == x && this.y == y) return view;
        return null;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
