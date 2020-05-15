import java.util.ArrayList;

public class GraphFromMap
{
    Node[][] CreateMap(int[][] a,int rows, int coloumns,int maxElevationDifference)
    {

        Node[][] nodeMap=new Node[rows][coloumns];
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<coloumns;j++)
            {
                nodeMap[i][j]=new Node(i,j,a[i][j],maxElevationDifference);
            }
        }
        for(int m=0;m<rows;m++)
        {
            for(int n=0;n<coloumns;n++)
            {
                // Add neighbours
                for(int i=m+1;i>=m-1;i--)
                {
                    for(int j=n+1;j>=n-1;j--)
                    {
                        if(i>=0 && i< rows && j>=0 && j<coloumns)
                        {
                            if(!(i==m && j==n))
                            {
                                Node neighbour=new Node(i,j,nodeMap[i][j].elevation,nodeMap[i][j].maxElevationDifference);
                                nodeMap[m][n].addNeighbours(neighbour);
                            }
                        }
                    }
                }

                //Set costs to neighbours
                nodeMap[m][n].costsToNeighbours=new ArrayList<>();
                for (Node neighbour: nodeMap[m][n].neighbours)
                {
                    CostToNeighbour cost=new CostToNeighbour(nodeMap[m][n],neighbour);
                    nodeMap[m][n].costsToNeighbours.add(cost);
                }
            }
        }
        return nodeMap;
    }
}


