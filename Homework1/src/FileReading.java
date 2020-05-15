import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileReading {

    private FileReader fileReader;
    private BufferedReader bufferedReader;

    public FileReading(File file) throws FileNotFoundException {
        this.fileReader = new FileReader(file);
        this.bufferedReader = new BufferedReader(this.fileReader);
    }

    List<String> getLines() throws IOException {
        List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }
}
