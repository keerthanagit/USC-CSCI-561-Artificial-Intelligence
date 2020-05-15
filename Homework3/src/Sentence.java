import java.util.ArrayList;
import java.util.List;

public class Sentence implements Cloneable {
    List<Predicate> terms = new ArrayList<Predicate>();

    public void addTerm(Predicate p) {
        this.terms.add(p);
    }

    public void addTerms(List<Predicate> p) {
        this.terms.addAll(p);
    }

    public List<Predicate> getTerms() {
        return this.terms;
    }

    public boolean removeTerm(Predicate p)
    {
        return this.terms.remove(p);
    }

    private void setTerms(List<Predicate> predicates) {
        this.terms = predicates;
    }

    public String print() {
        String result = "";
        for (Predicate term : this.terms) {
            result += term.print() + " V ";
        }
        if(!result.isEmpty())
            result = result.substring(0, result.length() - 3);
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Sentence sentence = (Sentence) super.clone();
        List<Predicate> predicates = new ArrayList<>();
        for (Predicate predicate : this.terms) {
            predicates.add((Predicate) predicate.clone());
        }
        sentence.setTerms(predicates);
        return sentence;
    }
}
