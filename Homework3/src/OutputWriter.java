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
        System.out.println(lines);
        this.bufferedWriter.write(lines.trim());
        this.bufferedWriter.close();
        this.fileWriter.close();
    }

}

