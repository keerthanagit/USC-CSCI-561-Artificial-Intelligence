import java.util.*;

public class KnowledgeBase implements Cloneable {

    private List<Sentence> sentences = new ArrayList<>();

    private HashSet<String> variables = new HashSet<>();

    private HashMap<Sentence, List<String>> substitutions = new HashMap<>();

    public void addSentence(Sentence s) {
        this.sentences.add(s);
    }
    public void addAtTop(Sentence s)
    {
        this.sentences.add(0,s);
    }

    public List<Sentence> getKnowledgeBase() {
        return this.sentences;
    }

    public void standardizeKb() {
        for (int i = 0; i < this.sentences.size(); i++) {
            Sentence s = this.sentences.get(i);
            List<String> replaceSet = new ArrayList<>();
            HashSet<String> sentenceVariables = new HashSet<>();
            for (int j = 0; j < s.getTerms().size(); j++) {
                Predicate p = s.getTerms().get(j);
                for (int k = 0; k < p.arguments.size(); k++) {
                    Argument a = p.arguments.get(k);
                    if (a.getType() == ArgumentType.Variable && !variables.contains(a.getName()) && !sentenceVariables.contains(a.getName())) {
                        variables.add(a.getName());
                        sentenceVariables.add(a.getName());
                    } else if (a.getType() == ArgumentType.Variable && variables.contains(a.getName()) && !sentenceVariables.contains(a.getName()) && !replaceSet.contains(a.getName())) {
                        replaceSet.add(a.getName());
                    }
                }
            }
            if (!replaceSet.isEmpty())
                substitutions.put(s, replaceSet);
        }
        replaceVariables();
        Collections.sort(this.sentences, new Comparator<Sentence>() {
            public int compare(Sentence a1, Sentence a2) {
                return a1.getTerms().size() - a2.getTerms().size(); // assumes you want biggest to smallest
            }
        });
    }

    public void replaceVariables() {
        for (int i = 0; i < this.sentences.size(); i++) {
            Sentence s = this.sentences.get(i);
            if (substitutions.containsKey(s)) {
                for (String var : substitutions.get(s)) {
                    String temp = var + i;
                    //Get a new variable
                    /*while (variables.contains(temp)) {
                        int charValue = temp.charAt(0);
                        if (charValue != 122)
                            temp = String.valueOf((char) (charValue + 1));
                        else
                            temp = String.valueOf((char) 97);
                    }
                    variables.add(temp);*/
                    //Replace the old variable with the new variable in the entire sentence
                    for (Predicate p : s.getTerms()) {
                        for (Argument a : p.arguments) {
                            if (a.getType() == ArgumentType.Variable && a.getName().equals(var))
                                a.setName(temp);
                        }
                    }
                }
            }

        }

    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public void print() {
        for (Sentence s : this.sentences) {
            System.out.println(s.print());
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        KnowledgeBase kb = (KnowledgeBase) super.clone();
        List<Sentence> sents = new ArrayList<>();
        for (Sentence s : this.sentences) {
            sents.add((Sentence) s.clone());
        }
        kb.setSentences(sents);
        return kb;
    }

}
