import java.util.*;

public class Minimax_Old
{
    public Board minimaxDecision(Board board) throws CloneNotSupportedException
    {
        Board max = new Board();

        for (Board neighbour : getMoves((Board) board.clone(), true))
        {
            Board result = minValue((Board)neighbour.clone(), 0);
            if (result.getScore() >= max.getScore())
            {
                max = result;
            }
            //max = minValue(neighbour, 0);
        }
        return max;
    }

    public Board maxValue(Board board, int depth) throws CloneNotSupportedException
    {
        //System.out.println("In Max, Depth: " + depth);
        if (cutOffTest(board, depth))
            return evaluatedValue(board);
        depth++;
        Board v = new Board();
        v.setScore(Integer.MIN_VALUE);
        for (Board neighbour : getMoves((Board)board.clone(), true))
        {
            Board b = minValue((Board)neighbour.clone(), depth);
            if (v.getScore() < b.getScore())
                v = (Board)b.clone();
        }
        return v;
    }

    public Board minValue(Board board, int depth) throws CloneNotSupportedException
    {
        //System.out.println("In Min, Depth: " + depth);
        if (cutOffTest(board, depth))
            return evaluatedValue(board);
        depth++;
        Board v = new Board();
        v.setScore(Integer.MAX_VALUE);
        for (Board neighbour : getMoves((Board)board.clone(), false))
        {
            Board b = maxValue((Board)neighbour.clone(), depth);
            if (v.getScore() > b.getScore())
                v = (Board)b.clone();
        }
        return v;
    }

    public boolean cutOffTest(Board board, int depth)
    {
        return depth == 2;
    }

    public Board evaluatedValue(Board board)
    {
        return board;
    }

    private List<Board> getSingleMoves(Board board, boolean isMax) throws CloneNotSupportedException
    {
        List<Board> moves = new ArrayList<>();
        char role = board.playerMap.get(isMax).getRole();
        Set<Point> points = board.getPointsByRole(role);
        Iterator<Point> itr = points.iterator();
        while (itr.hasNext())
        {
            Point from = itr.next();
            Point to = new Point(from.x, from.y);
            for (int i = 0; i < 8; i++)
            {
                to.x = from.x + Constants.SINGLE_MOVE_DIRECTIONS[i][0];
                to.y = from.x + Constants.SINGLE_MOVE_DIRECTIONS[i][1];
                if (board.getState().containsKey(to) && board.getState().get(to) == '.')
                {
                    Board newMove = (Board) board.clone();
                    HashMap<Point, Character> newState = newMove.getState();
                    newState.put(from, '.');
                    newState.put(to, role);
                    newMove.setState(newState);
                    newMove.setScore((int) Math.abs(100.0 * Math.random()));
                    moves.add(newMove);
                }
            }
        }
        return moves;
    }

    private List<Board> getJumps(Board board, boolean isMax) throws CloneNotSupportedException
    {
        List<Board> moves = new ArrayList<>();
        char role = board.playerMap.get(isMax).getRole();
        Set<Point> points = board.getPointsByRole(role);
        Iterator<Point> itr = points.iterator();
        List<Point> visited = new ArrayList<>();
        while (itr.hasNext())
        {
            Point original = itr.next();
            Point from = new Point(original.x, original.y);
            moves.addAll(jump(from, board, role, visited));
        }
        return moves;
    }

    public List<Board> jump(Point from, Board board, char role, List<Point> visited) throws CloneNotSupportedException
    {
        List<Board> moves = new ArrayList<>();
        Board newMove = (Board) board.clone();
        HashMap<Point, Character> newState = newMove.getState();
        Point to = new Point(from.x, from.y);
        for (int i = 0; i < 8; i++)
        {
            to.x = from.x + Constants.SINGLE_MOVE_DIRECTIONS[i][0];
            to.y = from.y + Constants.SINGLE_MOVE_DIRECTIONS[i][1];
            if (board.getState().containsKey(to) && board.getState().get(to) != '.')
            {
                to.x += Constants.SINGLE_MOVE_DIRECTIONS[i][0];
                to.y += Constants.SINGLE_MOVE_DIRECTIONS[i][1];
                if (board.getState().containsKey(to) && board.getState().get(to) == '.' && !visited.contains(to))
                {
                    newState.put(from, '.');
                    newState.put(to, role);
                    newMove.setState(newState);
                    newMove.setScore((int) Math.abs(100.0 * Math.random()));
                    moves.add(newMove);
                    visited.add(from);
                    moves.addAll(jump(new Point(to.x, to.y), newMove, role, visited));
                }
            }
        }
        return moves;
    }

    private List<Board> getMoves(Board board, boolean isMax) throws CloneNotSupportedException
    {

        List<Board> moves = new ArrayList<>();
        moves.addAll(getSingleMoves((Board) board.clone(), isMax));
        moves.addAll(getJumps((Board) board.clone(), isMax));
        return moves;
    }


}
