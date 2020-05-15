import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class homework
{
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        InputReader fr = new InputReader(new File("D:\\USC\\Fall 2019\\AI\\HW\\Solutions\\Homework2\\src\\input.txt"));
        List<String> lines = fr.getLines();
        //LINE 1: Game type
        Halma halma=new Halma();
        halma.gameType=GameType.valueOf(lines.get(0));
        //LINE 2: Player colour
        Player maxPlayer=new Player(lines.get(1));
        maxPlayer.isMaxPlayer=true;
        //LINE 3: Play time
        halma.setPlayTime(Float.parseFloat(lines.get(2)));
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
        halma.board=board;

        //Get the final move
        Move finalMove=halma.playGame(maxPlayer);
        //Write output to the file
        File file = new File("D:\\USC\\Fall 2019\\AI\\HW\\Solutions\\Homework2\\src\\output.txt");
        OutputWriter ow = new OutputWriter(file);
        ow.writeMove(finalMove);
    }

    public static String getMinPlayerColour(char maxPlayerRole)
    {
        if(maxPlayerRole==Constants.WHITE_PLAYER_ROLE)
            return Constants.BLACK_PLAYER_COLOUR;
        else
            return Constants.WHITE_PLAYER_COLOUR;
    }

}
