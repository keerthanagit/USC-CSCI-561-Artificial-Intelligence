import java.util.ArrayList;
import java.util.List;

public class Move {
    private List<Move> children = new ArrayList<Move>();
    private Move parent = null;
    private Point from = null;
    private Point to = null;
    private List<Point> hops = new ArrayList<>();
    private int value=0;
    public boolean isJump=false;
    public boolean isWin=false;

    /*public Move(Point from, Point to) {
        this.from = from;
        this.to = to;
    }*/


    public List<Move> getChildren() {
        return children;
    }
    public int getValue() {
        return this.value;
    }
    public void setValue(int value) {
        this.value=value;
    }

    public void setParent(Move parent) {
        //parent.addChild(this);
        this.parent = parent;
    }
    public Move getParent() {
        return this.parent;
    }

    public void addChild(Move child) {
        child.setParent(this);
        this.children.add(child);
    }
    public void addHop(Point hop) {
        this.hops.add(hop);
    }
    public void addHops(List<Point> hops) {
        this.hops.addAll(hops);
    }

    public Point getFromPoint() {
        return this.from;
    }

    public Point getToPoint() {
        return this.to;
    }
    public List<Point> getHops() {
        return this.hops;
    }

    public void setFromPoint(Point from) {
        this.from = from;
    }

    public void setToPoint(Point to) {
        this.to = to;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public void removeParent() {
        this.parent = null;
    }
}