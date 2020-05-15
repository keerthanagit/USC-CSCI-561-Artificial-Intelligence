import java.util.*;

public class Board implements Cloneable {
    private HashMap<Point, Character> state = new HashMap<>();
    private int score = 0;
    private List<Player> players = new ArrayList<>();
    public final HashMap<Boolean, Player> playerMap = new HashMap<>();

    @Override
    public Object clone() throws CloneNotSupportedException {
        Board board = (Board) super.clone();
        HashMap<Point, Character> map = new HashMap<>();
        map.putAll(this.state);
        board.setState(map);
        return board;
    }

    public HashMap<Point, Character> getState() {
        return this.state;
    }

    public void setState(HashMap<Point, Character> state) {
        this.state = state;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addPlayer(Player p) {
        this.players.add(p);
    }

    public void identifyPlayers() {
        /*for (Player p : this.players) {
            //p.setPawnPositions(this.state);
        }*/
        for (Player p : players) {
            if (p.isMaxPlayer)
                playerMap.put(true, p);
            else
                playerMap.put(false, p);

        }
    }


    public Set<Point> getPointsByRole(char role) {
        Set<Point> points = new HashSet<>();
        for (Map.Entry<Point, Character> entry : state.entrySet()) {
            if (entry.getValue() == role) {
                points.add(entry.getKey());
            }
        }
        return points;
    }

    public Board applyMove(Move move,boolean isMax) throws CloneNotSupportedException {
        Board b=(Board)this.clone();
        HashMap<Point, Character> newState = b.getState();
        //char value=move.getFromPoint();
        newState.put(move.getFromPoint(), '.');
        newState.put(move.getToPoint(), this.playerMap.get(isMax).getRole());
        b.setState(newState);
        return b;
    }

}
