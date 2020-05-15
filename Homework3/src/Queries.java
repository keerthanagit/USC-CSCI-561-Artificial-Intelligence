import java.util.ArrayList;
import java.util.List;

public class Queries {
    private List<Sentence> queries = new ArrayList<Sentence>();

    public void addQuery(Sentence query) {
        this.queries.add(query);
    }

    public List<Sentence> getQueries() {
        return this.queries;
    }
}
