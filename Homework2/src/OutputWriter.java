import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class OutputWriter {
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    public OutputWriter(File file) throws IOException {
        this.fileWriter = new FileWriter(file);
        this.bufferedWriter = new BufferedWriter(this.fileWriter);
    }

    void putLines(String lines) throws IOException {
        this.bufferedWriter.write(lines.trim());
        this.bufferedWriter.close();
        this.fileWriter.close();
    }
    void writeMove(Move move) throws IOException {
        String output="";
        if(!move.isJump)
        {
            output+="E "+move.getFromPoint().y+","+move.getFromPoint().x+" "+move.getToPoint().y+","+move.getToPoint().x;
        }
        else
        {
            output+="J "+move.getFromPoint().y+","+move.getFromPoint().x;
            if(move.getHops().size()!=0)
            {
                for (Point hop : move.getHops()
                ) {
                    output+=" " + hop.y + "," + hop.x;
                    output+="\nJ";
                    output+=" " + hop.y + "," + hop.x;
                }
            }
            output+=" "+move.getToPoint().y+","+move.getToPoint().x;
        }
        putLines(output);
    }
}

