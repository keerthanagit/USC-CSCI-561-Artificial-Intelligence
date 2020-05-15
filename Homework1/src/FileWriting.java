import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriting {
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    public FileWriting(File file) throws IOException {
        this.fileWriter = new FileWriter(file);
        this.bufferedWriter = new BufferedWriter(this.fileWriter);
    }

    void putLines(String lines) throws IOException {
        this.bufferedWriter.write(lines.substring(0, lines.length() - 1).trim());
        this.bufferedWriter.close();
        this.fileWriter.close();
    }
}
