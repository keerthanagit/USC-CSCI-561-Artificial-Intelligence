import java.util.*;

public class UCS
{
    public String runUCS(Node[][] map, Node source,Node goal, int rows, int columns,boolean isAStar){

        source.pathCost = 0;
        source.actualCost=0;
        PriorityQueue<Node> open = new PriorityQueue<Node>(rows*columns,new Comparator<Node>(){
                    //override compare method
                    public int compare(Node i, Node j)
                    {
                        if(i.pathCost > j.pathCost)
                            return 1;
                        else if (i.pathCost < j.pathCost)
                            return -1;
                        else
                            return 0;
                    }
                }
        );

        open.add(source);
        Set<Node> closed = new HashSet<Node>();
        Node[][] parents=new Node[rows][columns];
        parents[source.xc][source.yc]=null;
        int steps=0;

        //Initialize a list of strings to gather output
        List<String> outputLines=new ArrayList<>();

        //while frontier is not empty
        while(true)
        {
            steps++;
            if(open.isEmpty())
            {
                return "FAIL\n";
            }

            Node current = open.poll();

            if (this.isDestination(current,goal))
                return this.generateOutput(current, parents);

            for (Node child : map[current.xc][current.yc].neighbours)
            {
                int cost = map[current.xc][current.yc].getCost(child);

                if(!isAStar)
                {
                    child.pathCost = current.pathCost + cost;
                    child.actualCost=current.actualCost+cost;
                }
                else
                {
                    child.actualCost = current.actualCost + cost + this.getDifferenceInElevation(current, child);
                    child.pathCost = child.actualCost + this.getEuclideanDistance(child, goal);
                }

                if(!closed.contains(child)&& !open.contains(child))
                {
                    open.add(child);
                    parents[child.xc][child.yc]=current;
                }
                else if(open.stream().anyMatch(p->p.xc == child.xc && p.yc == child.yc && p.pathCost > child.pathCost))
                {
                    Node n = open.stream().filter( p->p.xc == child.xc && p.yc == child.yc&& p.pathCost > child.pathCost).findFirst().orElse(null);
                    open.remove(n);
                    open.add(child);
                    parents[child.xc][child.yc]=current;
                }

            }
            closed.add(current);
        }

    }

    private int getEuclideanDistance(Node child, Node goal)
    {
        int x1=child.xc;
        int x2=goal.xc;
        int y1=child.yc;
        int y2=goal.yc;
        return ((int)Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)))*9;

    }

    private int getDifferenceInElevation(Node current, Node child)
    {
        return Math.abs(current.elevation-child.elevation);
    }

    private boolean isDestination(Node a, Node b)
    {
        return a.xc == b.xc && a.yc == b.yc;
    }

    private String generateOutput(Node a, Node[][] parents) {

        Node pathPointer = a;
        LinkedList<Node> path = new LinkedList<Node>();
        String outputString = "";
        do {
            path.add(pathPointer);
            pathPointer = parents[pathPointer.xc][pathPointer.yc];
        }
        while (pathPointer != null);
        ListIterator<Node> it = path.listIterator(path.size());
        while (it.hasPrevious()) {
            Node n = it.previous();
            outputString = outputString + n.yc + "," + n.xc + " ";
        }
        outputString = outputString.substring(0, outputString.length() - 1)+ "\n";

        return outputString;
    }
}