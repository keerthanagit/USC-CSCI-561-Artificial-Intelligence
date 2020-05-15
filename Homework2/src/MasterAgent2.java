import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MasterAgent2 {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        int turn = 0;
        float playTime = Float.MAX_VALUE;
        long usedTime;
        Board b = new Board();
        String otherPlayerColour = "";
        while (!isBoardWin(b) && playTime > 0) {
            if (turn % 2 == 0) {

                long start = System.currentTimeMillis();
                InputReader fr = new InputReader(new File("D:\\USC\\Fall 2019\\AI\\HW\\Solutions\\Homework2\\src\\gameInput.txt"));
                List<String> lines = fr.getLines();
                //LINE 1: Game type
                Halma1 halma1 = new Halma1();
                halma1.gameType = GameType.valueOf(lines.get(0));
                //LINE 2: Player colour
                Player maxPlayer = new Player(lines.get(1));
                System.out.println("MAX("+lines.get(1)+") is playing");
                maxPlayer.isMaxPlayer = true;
                //LINE 3: Play time
                halma1.setPlayTime(Float.parseFloat(lines.get(2)));
                playTime = Float.parseFloat(lines.get(2));
                //Next 16 lines
                Board board = new Board();
                HashMap<Point, Character> state = new HashMap<>();
                for (int i = 0; i < Constants.MAX_BOARD_ROWS; i++) {
                    char[] chars = lines.get(3 + i).toCharArray();
                    for (int j = 0; j < Constants.MAX_BOARD_COLUMNS; j++) {
                        state.put(new Point(i, j), chars[j]);
                    }
                }
                board.setState(state);
                //Form the max player
                Player minPlayer = new Player(getMinPlayerColour(maxPlayer.getRole()));
                otherPlayerColour = getMinPlayerColour(maxPlayer.getRole());
                board.addPlayer(maxPlayer);
                board.addPlayer(minPlayer);
                board.identifyPlayers();
                halma1.board = board;

                //Get the final move
                Move finalMove = halma1.playGame(maxPlayer);
                //Write output to the file
                File file = new File("D:\\USC\\Fall 2019\\AI\\HW\\Solutions\\Homework2\\src\\gameOutput.txt");
                OutputWriter ow = new OutputWriter(file);
                ow.writeMove(finalMove);
                usedTime = System.currentTimeMillis() - start;
                b = halma1.board.applyMove(finalMove, true);
                turn++;
            }
            else
            {
                long start = System.currentTimeMillis();
                InputReader fr = new InputReader(new File("D:\\USC\\Fall 2019\\AI\\HW\\Solutions\\Homework2\\src\\gameInput.txt"));
                List<String> lines = fr.getLines();
                //LINE 1: Game type
                Halma2 halma2 = new Halma2();
                halma2.gameType = GameType.valueOf(lines.get(0));
                //LINE 2: Player colour
                Player maxPlayer = new Player(lines.get(1));
                System.out.println("MIN("+lines.get(1)+") is playing");
                maxPlayer.isMaxPlayer = true;
                //LINE 3: Play time
                halma2.setPlayTime(Float.parseFloat(lines.get(2)));
                playTime = Float.parseFloat(lines.get(2));
                //Next 16 lines
                Board board = new Board();
                HashMap<Point, Character> state = new HashMap<>();
                for (int i = 0; i < Constants.MAX_BOARD_ROWS; i++) {
                    char[] chars = lines.get(3 + i).toCharArray();
                    for (int j = 0; j < Constants.MAX_BOARD_COLUMNS; j++) {
                        state.put(new Point(i, j), chars[j]);
                    }
                }
                board.setState(state);
                //Form the max player
                Player minPlayer = new Player(getMinPlayerColour(maxPlayer.getRole()));
                otherPlayerColour = getMinPlayerColour(maxPlayer.getRole());
                board.addPlayer(maxPlayer);
                board.addPlayer(minPlayer);
                board.identifyPlayers();
                halma2.board = board;

                //Get the final move
                Move finalMove = halma2.playGame(maxPlayer);
                //Write output to the file
                File file = new File("D:\\USC\\Fall 2019\\AI\\HW\\Solutions\\Homework2\\src\\gameOutput.txt");
                OutputWriter ow = new OutputWriter(file);
                ow.writeMove(finalMove);
                //usedTime = System.currentTimeMillis() - start;
                usedTime=0;
                b = halma2.board.applyMove(finalMove, true);
                turn++;
            }
            System.out.println("Turn : "+(turn-1));
            playTime = playTime - (usedTime/1000F);
            System.out.println("Remaining play time is "+ playTime);
            modifyInputFile(b, playTime, getBoardString(b), otherPlayerColour);
        }
    }

    public static String getMinPlayerColour(char maxPlayerRole) {
        if (maxPlayerRole == Constants.WHITE_PLAYER_ROLE)
            return Constants.BLACK_PLAYER_COLOUR;
        else
            return Constants.WHITE_PLAYER_COLOUR;
    }

    public static boolean isBoardWin(Board b) {
        boolean isWin = false;
        Set<Point> whitePawns = b.getPointsByRole('W');
        Set<Point> blackPawns = b.getPointsByRole('B');
        Iterator<Point> whiteItr = whitePawns.iterator();
        Iterator<Point> blackItr = blackPawns.iterator();
        int c1 = 0, c2 = 0, c3 = 0;
        while (whiteItr.hasNext()) {
            if (Constants.BLACK_CAMP.contains(whiteItr.next()))
                c1++;
        }
        while (blackItr.hasNext()) {
            if (Constants.WHITE_CAMP.contains(blackItr.next()))
                c2++;
        }

        if (c1 == 19) {
            c3++;
            System.out.println("White wins");
        }
        if (c2 == 19) {
            c3++;
            System.out.println("Black wins");
        }
        if (c3 == 1 || c3 == 2)
            isWin = true;

        return isWin;
    }

    public static String getBoardString(Board b) {
        String board = "";
        for (int i = 0; i < Constants.MAX_BOARD_ROWS; i++) {
            for (int j = 0; j < Constants.MAX_BOARD_COLUMNS; j++) {
                board += b.getState().get(new Point(i, j));
            }
            board += "\n";
        }
        return board.substring(0, board.length() - 1);
    }

    public static void modifyInputFile(Board b, Float playTime, String board, String nextPlayerColour) throws IOException {

        FileWriter fileWriter = new FileWriter(new File("D:\\USC\\Fall 2019\\AI\\HW\\Solutions\\Homework2\\src\\gameInput.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("GAME");
        bufferedWriter.newLine();
        bufferedWriter.write(nextPlayerColour);
        bufferedWriter.newLine();
        bufferedWriter.write(String.format("%.1f", playTime));
        bufferedWriter.newLine();
        bufferedWriter.write(board);
        bufferedWriter.close();
        fileWriter.close();
    }
}
