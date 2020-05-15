import java.io.File;
import java.io.IOException;
import java.util.List;

public class homework {
    public static void main(String[] args) throws IOException {
        FileReading fr = new FileReading(new File("D:\\USC\\Java\\AI\\Homework1\\src\\input.txt"));
        List<String> lines = fr.getLines();
        //LINE 1 : Algorithm
        String algorithm = lines.get(0);
        //LINE 2 : Rows and Columns
        String[] rowsAndColumns = lines.get(1).split(" ");
        int columns = Integer.parseInt(rowsAndColumns[0]);
        int rows = Integer.parseInt(rowsAndColumns[1]);
        //LINE 3 : Source coordinates
        String[] sourceCoordinates = lines.get(2).split(" ");
        int sy = Integer.parseInt(sourceCoordinates[0]);
        int sx = Integer.parseInt(sourceCoordinates[1]);
        //LINE 4 : Difference in Elevation
        int maxElevationDifference = Integer.parseInt(lines.get(3));
        //LINE 5 : Number of target sites
        int numberOfTargetSites = Integer.parseInt(lines.get(4));
        // Next N lines : Target sites
        int[][] targets = new int[numberOfTargetSites][2];
        for (int i = 0; i < numberOfTargetSites; i++) {
            String[] destinationCoordinates = lines.get(i + 5).split(" ");
            targets[i][0] = Integer.parseInt(destinationCoordinates[1]);
            targets[i][1] = Integer.parseInt(destinationCoordinates[0]);
        }
        // Next H line : Elevation map
        int[][] elevationMap = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            String[] elevations = lines.get(i + 5 + numberOfTargetSites).split("\\s+");
            for (int j = 0; j < columns; j++) {
                elevationMap[i][j] = Integer.parseInt(elevations[j]);
                //elevationMap[i][j]=1;
            }
        }

        GraphFromMap nodeMap = new GraphFromMap();
        Node[][] map = nodeMap.CreateMap(elevationMap, rows, columns, maxElevationDifference);
        File file = new File("D:\\USC\\Java\\AI\\Homework1\\src\\output.txt");
        FileWriting fw = new FileWriting(file);

        String output="";
        for (int i = 0; i < numberOfTargetSites; i++) {
            switch (algorithm) {
                case "BFS": {
                    BFS bfs = new BFS();
                    output += bfs.runBFS(map, map[sx][sy], map[targets[i][0]][targets[i][1]], rows, columns);
                    break;
                }
                case "UCS": {
                    UCS ucs = new UCS();
                    output += ucs.runUCS(map, map[sx][sy], map[targets[i][0]][targets[i][1]], rows, columns, false);
                    break;
                }
                case "A*": {
                    UCS ucs = new UCS();
                    output += ucs.runUCS(map, map[sx][sy], map[targets[i][0]][targets[i][1]], rows, columns, true);
                    break;

                }

            }
        }
        fw.putLines(output);
    }
}
