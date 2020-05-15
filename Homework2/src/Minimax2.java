import java.util.*;

public class Minimax2 {

    int cutOffDepth;
    int pruneCount=0;

    Minimax2(int cutOffDepth)
    {
        this.cutOffDepth=cutOffDepth;
    }

    public Move alphaBetaSearch(Board board) throws CloneNotSupportedException {

        /*Move alpha=new Move();
        alpha.setValue(Integer.MIN_VALUE);
        Move beta=new Move();
        beta.setValue(Integer.MAX_VALUE);*/
        Move root = new Move();
        root.setParent(null);
        int depth = Constants.INITIAL_DEPTH+0;
        Move move = maxValue(board, depth, root/*, Integer.MIN_VALUE, Integer.MAX_VALUE*/);
        Move result=new Move();
        result.setValue(0);
        for (Move child:move.getChildren())
        {
            if(!move.isWin)
            {
                if(child.getValue()==move.getValue())
                    result=child;
            }
            else
            {
                if(child.isWin) {
                    if(result.getValue()<child.getValue())
                        result = child;
                }
            }

        }
        //System.out.println("prune count is "+pruneCount);
        return result;
    }

    public Move maxValue(Board board, int depth, Move root/*, int alpha, int beta*/) throws CloneNotSupportedException {

        if(isWinningConfiguration(board,true))
        {
            //System.out.println("MAX(Winning configuration reached): From ("+root.getFromPoint().x+","+root.getFromPoint().y+") To ("+root.getToPoint().x+","+root.getToPoint().y+") Depth - "+depth);
            return getWinningValue(board, root,true,depth);
        }
        if (cutOffTest(depth))
        {
            return evaluatedValue(board, root,true);
        }
        depth++;
        /*Move alpha=new Move();
        alpha.setValue(Integer.MIN_VALUE);
        Move beta=new Move();
        beta.setValue(Integer.MAX_VALUE);*/
        /*int alpha=Integer.MIN_VALUE;
        int beta=Integer.MAX_VALUE;*/
        Move v = new Move();
        v.setValue(Integer.MIN_VALUE);
        for (Move childMove : getMoves(board, true))
        {
            root.addChild(childMove);
            childMove.setParent(root);
            Board childBoard = board.applyMove(childMove, true);
            Move move = minValue(childBoard, depth, childMove/*,alpha,beta*/);
            if (v.getValue() <= move.getValue())
                v = move;
            /*if(v.getValue()>=beta)
            {

                if(!v.isWin)
                    v.getParent().setValue(v.getValue());
                //System.out.println("Pruned in max");
                pruneCount++;
                return v.getParent();
            }*/
            /*if(v.getValue()>alpha)
                //alpha.setValue(v.getValue());
                alpha=v.getValue();
            if(alpha>=beta)
                break;*/
        }
        if(!v.isWin)
            v.getParent().setValue(v.getValue());
        return v.getParent();
    }

    public Move minValue(Board board, int depth, Move root/*, int alpha, int beta*/) throws CloneNotSupportedException {

        if(isWinningConfiguration(board,false))
        {
            //System.out.println("MIN(Winning configuration reached): From ("+root.getFromPoint().x+","+root.getFromPoint().y+") To ("+root.getToPoint().x+","+root.getToPoint().y+") Depth - "+depth);
            return getWinningValue(board, root,false,depth);
        }
        if (cutOffTest(depth))
        {
            return evaluatedValue(board, root, false);
        }
        depth++;
        /*Move alpha=new Move();
        alpha.setValue(Integer.MIN_VALUE);
        Move beta=new Move();
        beta.setValue(Integer.MAX_VALUE);*/
       /* int alpha=Integer.MIN_VALUE;
        int beta=Integer.MAX_VALUE;*/
        Move v = new Move();
        v.setValue(Integer.MAX_VALUE);
        for (Move childMove : getMoves(board, false))
        {
            root.addChild(childMove);
            childMove.setParent(root);
            Board childBoard = board.applyMove(childMove, false);
            Move move = maxValue(childBoard, depth, childMove/*,alpha,beta*/);
            if (v.getValue() >= move.getValue())
                v = move;
            /*if(v.getValue()<=alpha)
            {
                if(!v.isWin)
                    v.getParent().setValue(v.getValue());
                //System.out.println("Pruned in min");
                pruneCount++;
                return v.getParent();
            }*/
            /*if(v.getValue()<beta)
                //beta.setValue(v.getValue());
                beta=v.getValue();
            if(alpha>=beta)
                break;*/
        }

        if(!v.isWin)
            v.getParent().setValue(v.getValue());
        return v.getParent();
    }

    private boolean cutOffTest(int depth) {
        return depth == this.cutOffDepth;
    }

    private Move evaluatedValue(Board board, Move root,boolean isMax) {
        //int score=this.getManhattanDistanceOfThePlayer(board,isMax)-this.getManhattanDistanceOfThePlayer(board,!isMax) + getBoardWeight(board, !isMax)-getBoardWeight(board, isMax);
        //int score=this.getManhattanDistanceOfThePlayerFromDestination(board,false)-this.getManhattanDistanceOfThePlayerFromDestination(board,true)+getBoardWeight(board, true)-getBoardWeight(board, false);
        //int score=this.getManhattanDistanceOfThePlayerFromBase(board,true)/*-this.getManhattanDistanceOfThePlayerFromDestination(board,false)*/+getBoardWeight(board, true)/*-getBoardWeight(board, false)*/;
        int score=(int)Math.random();
        board.setScore(score);
        root.setValue(score);
        return root;
    }

    private Move getWinningValue(Board board, Move root,boolean isMax, int depth) {

        int score=0;
        int parentscore=0;
        if(!isMax)
        {
            score=Integer.MAX_VALUE-depth;
            parentscore=Integer.MIN_VALUE+depth;
        }

        else
        {
            score=Integer.MIN_VALUE-depth;
            parentscore=Integer.MAX_VALUE+depth;
        }
        board.setScore(score);
        root.setValue(score);
        root.isWin=true;
        int count=1;
        Move temp=root.getParent();
        while(temp!=null)
        {
            if(count%2!=0)
            {
                temp.setValue(parentscore);
            }
            else
            {
                temp.setValue(score);
            }
            temp.isWin=true;
            temp=temp.getParent();
            count++;
        }
        return root;
    }

    private List<Move> getSingleMoves(Board board, Set<Point> points, boolean isMax) {
        char role = board.playerMap.get(isMax).getRole();
        List<Move> moves = new ArrayList<>();
        Iterator<Point> itr = points.iterator();
        while (itr.hasNext()) {
            Point from = itr.next();
            Point to = new Point(from.x, from.y);
            for (int i = 0; i < Constants.SINGLE_MOVE_DIRECTIONS.length; i++) {
                to.x = from.x + Constants.SINGLE_MOVE_DIRECTIONS[i][0];
                to.y = from.y + Constants.SINGLE_MOVE_DIRECTIONS[i][1];
                if (board.getState().containsKey(to) && board.getState().get(to) == Constants.EMPTY_SPACE) {
                    Move move = new Move();
                    move.setFromPoint(new Point(from.x, from.y));
                    move.setToPoint(new Point(to.x, to.y));
                    if(getDestinationCamp(role).contains(move.getFromPoint()))
                    {
                        if(getDestinationCamp(role).contains(move.getToPoint()))
                        {
                            moves.add(move);
                        }
                    }
                    else if(!getPlayerCamp(role).contains(move.getFromPoint()))
                    {
                        if(!getPlayerCamp(role).contains(move.getToPoint()))
                        {
                            moves.add(move);
                        }
                    }
                    else
                    {
                        if(!getPlayerCamp(role).contains(move.getToPoint()) && this.isFarther(move.getToPoint(), move.getFromPoint(), role))
                        {
                            moves.add(move);
                        }
                        else if(getPlayerCamp(role).contains(move.getToPoint()) && this.isFarther(move.getToPoint(), move.getFromPoint(), role))
                        {
                            moves.add(move);
                        }
                    }
                }
            }
        }
        return moves;
    }

    private List<Move> getJumps(Board board, Set<Point> points, boolean isMax) throws CloneNotSupportedException {
        List<Move> moves = new ArrayList<>();
        Iterator<Point> itr = points.iterator();
        while (itr.hasNext()) {
            Point original = itr.next();
            Point from = new Point(original.x, original.y);
            Move move = new Move();
            List<Point> visited = new ArrayList<>();
            move.setFromPoint(new Point(from.x, from.y));
            moves.addAll(jump(from, board, visited, move,isMax));
        }
        return moves;
    }

    private List<Move> jump(Point from, Board board, List<Point> visited, Move newMove, boolean isMax) throws CloneNotSupportedException {
        char role = board.playerMap.get(isMax).getRole();
        List<Move> moves = new ArrayList<>();
        Point to = new Point(from.x, from.y);
        for (int i = 0; i < Constants.SINGLE_MOVE_DIRECTIONS.length; i++)
        {
            visited.add(new Point(from.x,from.y));
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
                    if(getDestinationCamp(role).contains(move.getFromPoint()))
                    {
                        if(getDestinationCamp(role).contains(move.getToPoint()))
                        {
                            moves.add(move);
                        }
                    }
                    else if(!getPlayerCamp(role).contains(move.getFromPoint()))
                    {
                        if(!getPlayerCamp(role).contains(move.getToPoint()))
                        {
                            moves.add(move);
                        }
                    }
                    else
                    {
                        if(!getPlayerCamp(role).contains(move.getToPoint()) && this.isFarther(move.getToPoint(), move.getFromPoint(), role))
                        {
                            moves.add(move);
                        }
                        else if(getPlayerCamp(role).contains(move.getToPoint()) && this.isFarther(move.getToPoint(), move.getFromPoint(), role))
                        {
                            moves.add(move);
                        }
                    }
                    moves.addAll(jump(new Point(to.x, to.y),board.applyMove(move, isMax), visited, move, isMax));
                }
            }
        }
        return moves;
    }

    private List<Move> getMoves(Board board, boolean isMax) throws CloneNotSupportedException {
        List<Move> moves = new ArrayList<>();
        char role = board.playerMap.get(isMax).getRole();
        Set<Point> points= this.getPoints(board, isMax);
        moves.addAll(getSingleMoves(board, points,isMax));
        moves.addAll(getJumps(board, points,isMax));
        List<Move> moves2 = new ArrayList<>();
        if(!moves.isEmpty())
        {
            Iterator<Move> itr = moves.iterator();
            while (itr.hasNext()) {
                Move move=itr.next();
                if(getPlayerCamp(role).contains(move.getFromPoint()) && getPlayerCamp(role).contains(move.getToPoint()))
                {
                    itr.remove();
                    moves2.add(move);
                }
            }
            if(moves.isEmpty()) {
                moves=moves2;
            }
        }
        else
        {
            points = board.getPointsByRole(role);
            moves.addAll(getSingleMoves(board, points,isMax));
            moves.addAll(getJumps(board, points,isMax));
            moves.sort(new Comparator<Move>() {
                public int compare(Move move1, Move move2) {
                    return getManhattanDistanceOfThePoint(move1.getToPoint(), role) - getManhattanDistanceOfThePoint(move2.getToPoint(), role);
                }
            });
        }


        return moves;
    }

    private int getManhattanDistanceOfThePlayerFromDestination(Board board,boolean isMax) {
        char role = board.playerMap.get(isMax).getRole();
        Set<Point> points = board.getPointsByRole(role);
        Iterator<Point> itr = points.iterator();
        int manhattanDistance=0;
        int referenceX;
        int referenceY;
        if(role!=Constants.WHITE_PLAYER_ROLE)
        {
            referenceX=Constants.WHITE_CAMP_ROOT[0];
            referenceY=Constants.WHITE_CAMP_ROOT[1];
        }
        else
        {
            referenceX=Constants.BLACK_CAMP_ROOT[0];
            referenceY=Constants.BLACK_CAMP_ROOT[1];
        }
        while (itr.hasNext())
        {
            Point p=itr.next();
            manhattanDistance+=Math.abs(p.x-referenceX)+Math.abs(p.y-referenceY);
        }
        return manhattanDistance;
    }

    private int getManhattanDistanceOfThePlayerFromBase(Board board,boolean isMax) {
        char role = board.playerMap.get(isMax).getRole();
        Set<Point> points = board.getPointsByRole(role);
        Iterator<Point> itr = points.iterator();
        int manhattanDistance=0;
        int referenceX;
        int referenceY;
        if(role==Constants.WHITE_PLAYER_ROLE)
        {
            referenceX=Constants.WHITE_CAMP_ROOT[0];
            referenceY=Constants.WHITE_CAMP_ROOT[1];
        }
        else
        {
            referenceX=Constants.BLACK_CAMP_ROOT[0];
            referenceY=Constants.BLACK_CAMP_ROOT[1];
        }
        while (itr.hasNext())
        {
            Point p=itr.next();
            manhattanDistance+=Math.abs(p.x-referenceX)+Math.abs(p.y-referenceY);
        }
        return manhattanDistance;
    }

    private int getManhattanDistanceOfThePoint(Point p,char role) {

        int manhattanDistance=0;
        int referenceX;
        int referenceY;
        if(role!=Constants.WHITE_PLAYER_ROLE)
        {
            referenceX=Constants.WHITE_CAMP_ROOT[0];
            referenceY=Constants.WHITE_CAMP_ROOT[1];
        }
        else
        {
            referenceX=Constants.BLACK_CAMP_ROOT[0];
            referenceY=Constants.BLACK_CAMP_ROOT[1];
        }

        //manhattanDistance=Math.abs(p.x-referenceX)+Math.abs(p.y-referenceY);
        manhattanDistance=((int)Math.sqrt((p.x-referenceX)*(p.x-referenceX) + (p.y-referenceY)*(p.y-referenceY)));

        return manhattanDistance;
    }

    private boolean isFarther(Point to,Point from, char role) {
        boolean isFarther=false;
        int horizontalDistanceFrom=0, horizontalDistanceTo=0, verticalDistanceFrom=0, verticalDistanceTo=0;
        int referenceX,referenceY;
        if(role==Constants.WHITE_PLAYER_ROLE)
        {
            referenceX=Constants.WHITE_CAMP_ROOT[0];
            referenceY=Constants.WHITE_CAMP_ROOT[1];
        }
        else
        {
            referenceX=Constants.BLACK_CAMP_ROOT[0];
            referenceY=Constants.BLACK_CAMP_ROOT[1];
        }
        horizontalDistanceFrom=Math.abs(from.x-referenceX);
        horizontalDistanceTo=Math.abs(to.x-referenceX);
        verticalDistanceFrom=Math.abs(from.y-referenceY);
        verticalDistanceTo=Math.abs(to.y-referenceY);
        if(horizontalDistanceTo>horizontalDistanceFrom)
        {
            if(verticalDistanceTo>=verticalDistanceFrom)
            {
                isFarther=true;
            }
        }
        else if(horizontalDistanceTo==horizontalDistanceFrom)
        {
            if(verticalDistanceTo>verticalDistanceFrom)
            {
                isFarther=true;
            }
        }
        return isFarther;
    }

    private Set<Point> getPointsInCamp(Set<Point> allPoints,char role) {
        Set<Point> pointsInCamp=new HashSet<>();
        Set<Point> playerCamp;
        if(role==Constants.WHITE_PLAYER_ROLE)
            playerCamp = Constants.WHITE_CAMP;
        else
            playerCamp = Constants.BLACK_CAMP;
        Iterator<Point> itr = allPoints.iterator();
        while(itr.hasNext())
        {
            Point p=itr.next();
            if(playerCamp.contains(p))
                pointsInCamp.add(new Point(p.x,p.y));
        }
        return pointsInCamp;
    }

    private Set<Point> getPoints(Board board,boolean isMax) {
        char role = board.playerMap.get(isMax).getRole();
        Set<Point> points;
        Set<Point> allPoints = board.getPointsByRole(role);
        Set<Point> campPoints=this.getPointsInCamp(allPoints,role);
        if(!campPoints.isEmpty())
            points=campPoints;
        else
            points=allPoints;
        return points;
    }

    private boolean isWinningConfiguration(Board board, boolean isMax) {
        boolean isWin=false;
        int count=0;
        char role = board.playerMap.get(true).getRole();
        Set<Point> allPoints = board.getPointsByRole(role);
        Set<Point> destinationCamp;
        if(role==Constants.WHITE_PLAYER_ROLE)
            destinationCamp=Constants.BLACK_CAMP;
        else
            destinationCamp=Constants.WHITE_CAMP;


        Iterator<Point> itr = destinationCamp.iterator();
        while(itr.hasNext())
        {
            if(board.getState().get(itr.next())!=role)
                return false;
        }
        return true;

        /*Iterator<Point> itr = allPoints.iterator();
        while(itr.hasNext())
        {
            if(destinationCamp.contains(itr.next()))
                count++;
        }
        if(count==19)
            isWin=true;
        return isWin;*/
    }

    private Set<Point> getDestinationCamp(char role) {
        if(role==Constants.WHITE_PLAYER_ROLE)
            return Constants.BLACK_CAMP;
        else
            return Constants.WHITE_CAMP;
    }

    private Set<Point> getPlayerCamp(char role) {
        if(role==Constants.WHITE_PLAYER_ROLE)
            return Constants.WHITE_CAMP;
        else
            return Constants.BLACK_CAMP;
    }

    private  Map<Point, Integer> getCellWeightMap(char role) {
        Map<Point, Integer> cellWeightMap = new HashMap<>();
        Set<Point> destinationCamp = getDestinationCamp(role);
        Set<Point> baseCamp = getPlayerCamp(role);

        List<List<Point>> diagonals = getDiagonalMatrix();
        for (List<Point> diagonal : diagonals)
        {
            int mid = (diagonal.size() / 2);
            // Prioritize moves to middle
            if (diagonal.size() > 7)
            {
                for (int x = mid; x >= mid - 3; x--)
                {
                    cellWeightMap.put(diagonal.get(x), 2);
                }
                for (int x = mid; x < mid + 3; x++)
                {
                    cellWeightMap.put(diagonal.get(x), 2);
                }
            }
            else
            {
                for (Point p : diagonal)
                {
                    cellWeightMap.put(p, 2);
                }
            }
        }
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                Point p = new Point(i, j);
                if (destinationCamp.contains(p))
                    cellWeightMap.put(p, 3);
                else if (baseCamp.contains(p))
                    cellWeightMap.put(p, -1);
            }
        }
        return cellWeightMap;
    }

    private static List<List<Point>> getDiagonalMatrix() {
        List<List<Point>> diagonals = new ArrayList<>();
        //print first half
        int row =0, col;

        while(row<16)
        {
            col =0;
            int rowTemp = row;
            List<Point> diagonal=new ArrayList<>();
            while(rowTemp>=0)
            {
                diagonal.add(new Point(rowTemp,col));
                rowTemp--;
                col++;
            }
            diagonals.add(diagonal);
            row++;
        }

        //print second half
        col = 1;
        while(col<16)
        {
            int colTemp = col;
            row = 15;
            List<Point> diagonal=new ArrayList<>();
            while(colTemp<16)
            {
                diagonal.add(new Point(row,colTemp));
                row--;
                colTemp++;
            }
            diagonals.add(diagonal);
            col++;
        }
        return diagonals;
    }

    private int getBoardWeight(Board board, boolean isMax) {
        int weight=0;
        char role = board.playerMap.get(isMax).getRole();
        Map<Point, Integer> diagonalMatrix= getCellWeightMap(role);
        Set<Point> points = board.getPointsByRole(role);
        Iterator<Point> itr = points.iterator();
        while(itr.hasNext())
        {
            Point p=itr.next();
            if(diagonalMatrix.containsKey(p))
                weight+=diagonalMatrix.get(p);
        }
        return weight;
    }

}
