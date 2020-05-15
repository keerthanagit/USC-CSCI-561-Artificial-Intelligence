import java.util.HashSet;
import java.util.Set;

import java.util.*;
public class Constants
{
    public static int[][] SINGLE_MOVE_DIRECTIONS={{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
    public static char EMPTY_SPACE='.';
    public static int INITIAL_DEPTH=0;
    public static char WHITE_PLAYER_ROLE='W';
    public static String WHITE_PLAYER_COLOUR="WHITE";
    public static String BLACK_PLAYER_COLOUR="BLACK";
    public static int MAX_BOARD_ROWS=16;
    public static int MAX_BOARD_COLUMNS=16;
    public static int CUTOFF_DEPTH = 3;
    public static int FAST_CUTOFF_DEPTH = 1;
    public static int[] WHITE_CAMP_ROOT={15,15};
    public static int[] BLACK_CAMP_ROOT={0,0};
    public static Set<Point> WHITE_CAMP=new HashSet<>(Arrays.asList(new Point(15,11),new Point(15,12),new Point(15,13),new Point(15,14),new Point(15,15),new Point(14,11),new Point(14,12),new Point(14,13),new Point(14,14),new Point(14,15),new Point(13,12),new Point(13,13),new Point(13,14),new Point(13,15),new Point(12,13),new Point(12,14),new Point(12,15),new Point(11,14),new Point(11,15)));
    public static Set<Point> BLACK_CAMP=new HashSet<>(Arrays.asList(new Point(0,0),new Point(0,1),new Point(0,2),new Point(0,3),new Point(0,4),new Point(1,0),new Point(1,1),new Point(1,2),new Point(1,3),new Point(1,4),new Point(2,0),new Point(2,1),new Point(2,2),new Point(2,3),new Point(3,0),new Point(3,1),new Point(3,2),new Point(4,0),new Point(4,1)));

}
