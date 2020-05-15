import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Minimax {
    public Move minimaxDecision(Board board) throws CloneNotSupportedException {
        Move max = new Move();
        max.setValue(Integer.MIN_VALUE);
        Move root = new Move();
        root.setParent(null);
        int depth = Constants.INITIAL_DEPTH+1;
        for (Move childMove : getMoves((Board) board.clone(), true)) {
            root.addChild(childMove);
            childMove.setParent(root);
            Board childBoard = ((Board) board.clone()).applyMove(childMove, false);
            Move move = minValue((Board) childBoard.clone(), depth, childMove);
            if (move.getValue() > max.getValue())
                max = move;
        }
        return max;
    }


    public Move maxValue(Board board, int depth, Move root) throws CloneNotSupportedException {
        if (cutOffTest(board, depth))
            return evaluatedValue(board, root);
        depth++;
        Move v = new Move();
        v.setValue(Integer.MIN_VALUE);
        for (Move childMove : getMoves((Board) board.clone(), true)) {
            root.addChild(childMove);
            childMove.setParent(root);
            Board childBoard = ((Board) board.clone()).applyMove(childMove, false);
            Move move = minValue((Board) childBoard.clone(), depth, childMove);
            if (v.getValue() < move.getValue())
                v = move;
        }
        v.getParent().setValue(v.getValue());
        return v.getParent();
    }

    public Move minValue(Board board, int depth, Move root) throws CloneNotSupportedException {
        if (cutOffTest(board, depth))
            return evaluatedValue(board, root);
        depth++;
        Move v = new Move();
        v.setValue(Integer.MAX_VALUE);
        for (Move childMove : getMoves((Board) board.clone(), false)) {
            root.addChild(childMove);
            childMove.setParent(root);
            Board childBoard = ((Board) board.clone()).applyMove(childMove, false);
            Move move = maxValue((Board) childBoard.clone(), depth, childMove);
            if (v.getValue() > move.getValue())
                v = move;
        }
        v.getParent().setValue(v.getValue());
        return v.getParent();
    }

    public boolean cutOffTest(Board board, int depth) {
        return depth == Constants.CUTOFF_DEPTH;
    }

    public Move evaluatedValue(Board board, Move root) {
        int score = (int) Math.abs(100.0 * Math.random());
        board.setScore(score);
        root.setValue(score);
        return root;
    }

    private List<Move> getSingleMoves(Board board, boolean isMax) throws CloneNotSupportedException {
        List<Move> moves = new ArrayList<>();
        char role = board.playerMap.get(isMax).getRole();
        Set<Point> points = board.getPointsByRole(role);
        Iterator<Point> itr = points.iterator();
        while (itr.hasNext()) {
            Point from = itr.next();
            Point to = new Point(from.x, from.y);
            for (int i = 0; i < Constants.SINGLE_MOVE_DIRECTIONS.length; i++) {
                to.x = from.x + Constants.SINGLE_MOVE_DIRECTIONS[i][0];
                to.y = from.y + Constants.SINGLE_MOVE_DIRECTIONS[i][1];
                if (board.getState().containsKey(to) && board.getState().get(to) ==Constants.EMPTY_SPACE) {
                    Move move = new Move();
                    move.setFromPoint(new Point(from.x, from.y));
                    move.setToPoint(new Point(to.x, to.y));
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    private List<Move> getJumps(Board board, boolean isMax) throws CloneNotSupportedException {
        List<Move> moves = new ArrayList<>();
        char role = board.playerMap.get(isMax).getRole();
        Set<Point> points = board.getPointsByRole(role);
        Iterator<Point> itr = points.iterator();
        List<Point> visited = new ArrayList<>();
        while (itr.hasNext()) {
            Point original = itr.next();
            Point from = new Point(original.x, original.y);
            Move move = new Move();
            move.setFromPoint(new Point(from.x, from.y));
            moves.addAll(jump(from, board, visited, move));
        }
        return moves;
    }

    public List<Move> jump(Point from, Board board, List<Point> visited, Move newMove) throws CloneNotSupportedException {
        List<Move> moves = new ArrayList<>();
        Point to = new Point(from.x, from.y);
        for (int i = 0; i < Constants.SINGLE_MOVE_DIRECTIONS.length; i++) {
            to.x = from.x + Constants.SINGLE_MOVE_DIRECTIONS[i][0];
            to.y = from.y + Constants.SINGLE_MOVE_DIRECTIONS[i][1];
            if (board.getState().containsKey(to) && board.getState().get(to) != Constants.EMPTY_SPACE) {
                to.x += Constants.SINGLE_MOVE_DIRECTIONS[i][0];
                to.y += Constants.SINGLE_MOVE_DIRECTIONS[i][1];
                if (board.getState().containsKey(to) && board.getState().get(to) == Constants.EMPTY_SPACE && !visited.contains(to)) {
                    Move move = new Move();
                    move.setFromPoint(new Point(newMove.getFromPoint().x, newMove.getFromPoint().y));
                    if(newMove.getHops().size()!=0)
                        move.addHops(newMove.getHops());
                    if(newMove.getToPoint()!=null)
                        move.addHop(newMove.getToPoint());
                    move.setToPoint(new Point(to.x, to.y));
                    move.isJump=true;
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    private List<Move> getMoves(Board board, boolean isMax) throws CloneNotSupportedException {
        List<Move> moves = new ArrayList<>();
        moves.addAll(getSingleMoves((Board) board.clone(), isMax));
        moves.addAll(getJumps((Board) board.clone(), isMax));
        return moves;
    }

}
