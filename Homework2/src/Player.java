
public class Player
{
    public String playerColour;
    //public Set<Point> pawnPositions= new HashSet<Point>();
    private char role;
    public boolean isMaxPlayer=false;

    Player(String playerColour)
    {
        this.playerColour=playerColour;
        if(playerColour.equals("WHITE"))
            role='W';
        else
            role='B';
    }

    public char getRole()
    {
        return this.role;
    }
    /*public char getOpponentRole()
    {
        if(playerColour.equals("WHITE"))
            return 'W';
        else
            return 'B';
    }
    public void setPawnPositions(HashMap<Point,Character> state)
    {
        for (Map.Entry<Point, Character> entry : state.entrySet())
        {
            if (entry.getValue()==this.role)
            {
                pawnPositions.add(entry.getKey());
            }
        }
    }
    public Set<Point> getPawnPositions()
    {
        return pawnPositions;
    }
*/
}

