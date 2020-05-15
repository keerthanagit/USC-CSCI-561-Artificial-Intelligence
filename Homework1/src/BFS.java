import java.util.*;

public class BFS {
    String runBFS(Node[][] map, Node source, Node destination, int rows, int columns) {
        Set<Node> explored = new HashSet<Node>();
        Queue<Node> queue = new LinkedList<Node>();
        Node[][] parents = new Node[rows][columns];
        parents[source.xc][source.yc] = null;
        if (this.isDestination(source,destination))
            return this.generateOutput(source, parents);
        queue.add(source);
        while (true) {
            if (queue.isEmpty()) {
                return "FAIL\n";
            }
            Node current = queue.poll();
            explored.add(current);
            for (Node child : map[current.xc][current.yc].neighbours) {
                if (!queue.contains(child) && !explored.contains(child)) {
                    parents[child.xc][child.yc] = current;
                    if (this.isDestination(child,destination))
                        return this.generateOutput(child, parents);
                    queue.add(child);
                }
            }
        }
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
