import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MasterAgent {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        long startTs = System.currentTimeMillis();
        InputReader fr = new InputReader(new File("D:\\USC\\Fall 2019\\AI\\HW\\Solutions\\Homework2\\src\\input.txt"));
        List<String> lines = fr.getLines();
        //LINE 1: Game type
        Halma1 halma1=new Halma1();
        halma1.gameType=GameType.valueOf(lines.get(0));
        //LINE 2: Player colour
        Player maxPlayer=new Player(lines.get(1));
        maxPlayer.isMaxPlayer=true;
        //LINE 3: Play time
        halma1.setPlayTime(Float.parseFloat(lines.get(2)));
        //Next 16 lines
        Board board=new Board();
        HashMap<Point,Character> state= new HashMap<>();
        for(int i=0;i<Constants.MAX_BOARD_ROWS;i++)
        {
            char[] chars=lines.get(3+i).toCharArray();
            for(int j=0;j<Constants.MAX_BOARD_COLUMNS;j++)
            {
                state.put(new Point(i,j),chars[j]);
            }
        }
        board.setState(state);
        //Form the max player
        Player minPlayer=new Player(getMinPlayerColour(maxPlayer.getRole()));
        board.addPlayer(maxPlayer);
        board.addPlayer(minPlayer);
        board.identifyPlayers();
        halma1.board=board;
        ///////////////////////////
        //////////////////////////
        Halma2 halma2=new Halma2();
        halma2.gameType=GameType.valueOf(lines.get(0));
        Player minPlayer2=new Player(lines.get(1));
        Player maxPlayer2=new Player(getMinPlayerColour(minPlayer2.getRole()));
        maxPlayer2.isMaxPlayer=true;
        halma2.setPlayTime(Float.parseFloat(lines.get(2)));
        Board board2=new Board();
        board2.addPlayer(maxPlayer2);
        board2.addPlayer(minPlayer2);
        Board b=board;
        Move m=halma1.playGame(maxPlayer);
        b=halma1.board.applyMove(m,true);
        System.out.print("Player ply: ");
        System.out.println("Turn 0");
        System.out.println("Move is ("+m.getFromPoint().x+","+m.getFromPoint().y+") to ("+m.getToPoint().x+","+m.getToPoint().y+")");

        int turn=1;
        long temp = System.currentTimeMillis()-startTs;
        float rem=Float.parseFloat(lines.get(2))-(temp/1000F);

        while(!isBoardWin(b) && rem<Float.parseFloat(lines.get(2)) )
        {
            if(turn==98)
            {
                System.out.println("Turn is 98");
            }
            long temp1 = System.currentTimeMillis();

            Board btemp=new Board();
            if(turn%2==0)
            {
                System.out.print("Player ply :");
                Halma1 h;
                Player max,min;
                h=halma1;
                max=maxPlayer;
                min=minPlayer;
                btemp.setState(b.getState());
                btemp.addPlayer(min);
                btemp.addPlayer(max);
                btemp.identifyPlayers();
                h.gameType=GameType.valueOf(lines.get(0));
                h.setPlayTime(Float.parseFloat(lines.get(2)));
                h.board=btemp;

                Move move=h.playGame(max);
                b=h.board.applyMove(move,true);
                System.out.println("Move is ("+move.getFromPoint().x+","+move.getFromPoint().y+") to ("+move.getToPoint().x+","+move.getToPoint().y+")");
            }
            else
            {
                System.out.print("Opponent ply :");
                Halma2 h;
                Player max,min;
                h=halma2;
                max=maxPlayer2;
                min=minPlayer2;
                btemp.setState(b.getState());
                btemp.addPlayer(min);
                btemp.addPlayer(max);
                btemp.identifyPlayers();
                h.gameType=GameType.valueOf(lines.get(0));
                h.setPlayTime(Float.parseFloat(lines.get(2)));
                h.board=btemp;

                Move move=h.playGame(max);
                b=h.board.applyMove(move,true);
                System.out.println("Move is ("+move.getFromPoint().x+","+move.getFromPoint().y+") to ("+move.getToPoint().x+","+move.getToPoint().y+")");

            }

            System.out.println("");
            for(int i=0;i<Constants.MAX_BOARD_ROWS;i++)
            {
                for(int j=0;j<Constants.MAX_BOARD_COLUMNS;j++)
                {
                    System.out.print(b.getState().get(new Point(i,j)));
                }
                System.out.println("");
            }
            System.out.println("Turn "+turn);
            turn++;

            long temp2=System.currentTimeMillis()-temp1;
            rem = rem-(temp2/1000F);
            //System.out.println("Remaining time is "+rem);
        }
        long temp2=System.currentTimeMillis()-startTs;
        System.out.println("Total time is "+temp2);
        System.out.println("Turn "+(turn-1));
        System.out.println("GAME OVER");
        /////////////////////////////
        /////////////////////////////

        System.out.println(System.currentTimeMillis() - startTs);
    }

    public static String getMinPlayerColour(char maxPlayerRole)
    {
        if(maxPlayerRole==Constants.WHITE_PLAYER_ROLE)
            return Constants.BLACK_PLAYER_COLOUR;
        else
            return Constants.WHITE_PLAYER_COLOUR;
    }

    public static boolean isBoardWin(Board b)
    {
        boolean isWin=false;
        Set<Point> whitePawns=b.getPointsByRole('W');
        Set<Point> blackPawns=b.getPointsByRole('B');
        Iterator<Point> whiteItr=whitePawns.iterator();
        Iterator<Point> blackItr=blackPawns.iterator();
        int c1=0,c2=0,c3=0;
        while(whiteItr.hasNext())
        {
            if(Constants.BLACK_CAMP.contains(whiteItr.next()))
                c1++;
        }
        while(blackItr.hasNext())
        {
            if(Constants.WHITE_CAMP.contains(blackItr.next()))
                c2++;
        }

        if(c1==19) {
            c3++;
            System.out.println("White wins");
        }
        if(c2==19){
            c3++;
            System.out.println("Black wins");
        }
        if(c3==1 || c3==2)
            isWin=true;

        return isWin;
    }
}
