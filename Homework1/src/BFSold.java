import java.util.*;

public class BFSold
{

    List<String> runBFS(Node[][] map, Node source,Node destination, int rows, int columns)
    {
        // Mark all the vertices as not visited(By default
        // set as false)
        boolean[][] visited = new boolean[rows][columns];

        // Create a queue for BFS
        LinkedList<Node> queue = new LinkedList<Node>();
        Node[][] parents=new Node[rows][columns];
        LinkedList<Node> path = new LinkedList<Node>();

        // Mark the current node as visited and enqueue it
        visited[source.xc][source.yc]=true;
        queue.add(source);

        //Mark the parent of source as null
        parents[source.xc][source.yc]=null;

        //Set the destination as not reached
        boolean reachedDestination=false;

        //Initialize a list of strings to gather output
        String outputString;
        List<String> outputLines=new ArrayList<>();

        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            Node current = queue.poll();
            //System.out.print("(" + current.xc + "," + current.yc + ")   ");


            if (this.isDestination(current, destination))
            {
                Node pathPointer=destination;
                do
                {
                    path.add(pathPointer);
                    pathPointer= parents[pathPointer.xc][pathPointer.yc];
                }
                while(pathPointer!=null);

                ListIterator<Node> it = path.listIterator(path.size());
                //System.out.println(path.size());
                //System.out.println("Path");
                outputString="";
                while (it.hasPrevious())
                {
                    Node n=it.previous();
                    outputString=outputString+n.yc + "," + n.xc + " ";
                    System.out.print(n.yc + "," + n.xc + " ");
                }
                System.out.print("\n");
                outputLines.add(outputString);

                reachedDestination=true;
                break;
            }

            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            for (Node next : map[current.xc][current.yc].neighbours) {
                if (!visited[next.xc][next.yc]) {
                    visited[next.xc][next.yc] = true;
                    parents[next.xc][next.yc]=current;
                    queue.add(next);
                }
            }
        }
        if(!reachedDestination)
        {
            System.out.println("FAIL");
            outputLines.add("FAIL");
        }
         return  outputLines;
    }

    private boolean isDestination(Node a, Node b)
    {
        return a.xc == b.xc && a.yc == b.yc;
    }


}
