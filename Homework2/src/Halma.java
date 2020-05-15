public class Halma
{
    public GameType gameType;
    private float playTime;
    Board board;

    public void setPlayTime(float playTime)
    {
        this.playTime=playTime;
    }


    public Move playGame(Player maxPlayer) throws CloneNotSupportedException
    {
        int cutOffDepth;
        if(this.playTime<4.0)
            cutOffDepth=Constants.FAST_CUTOFF_DEPTH;
        else
            cutOffDepth=Constants.CUTOFF_DEPTH;

        AlphaBetaPruning game=new AlphaBetaPruning(cutOffDepth);
        return game.alphaBetaSearch(board);
    }


}
