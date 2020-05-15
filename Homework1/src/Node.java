import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Node
{
    int xc;
    int yc;
     int elevation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return xc == node.xc &&
                yc == node.yc ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xc, yc);
    }

     int maxElevationDifference;
    int pathCost;
    int actualCost;
    LinkedList<Node> neighbours;
    List<CostToNeighbour> costsToNeighbours;

    Node(int xc, int yc, int elevation, int maxElevationDifference)
    {
        this.xc=xc;
        this.yc=yc;
        this.elevation=elevation;
        this.maxElevationDifference=maxElevationDifference;
        this.neighbours=new LinkedList<Node>();
    }

    // Function to add an edge into the graph
    void addNeighbours(Node v)
    {
        if(Math.abs(this.elevation-v.elevation)<=maxElevationDifference) {
            this.neighbours.add(v);
        }
    }
    
    int getCost(Node target)
    {
        int c=0;
        for (CostToNeighbour cost: costsToNeighbours)
        {
            if(cost.source==this && cost.target==target)
                c=cost.cost;
        }
        return c;
    }

}

class CostToNeighbour
{
    Node source;
    Node target;
    int cost;
    CostToNeighbour(Node i,Node j)
    {
        this.source=i;
        this.target=j;
        if((i.xc== j.xc) ^ (i.yc==j.yc))
            this.cost=10;
        else if ((i.xc!=j.xc) && (i.yc!=j.yc))
            this.cost=14;
        else
            this.cost=0;

    }

}
