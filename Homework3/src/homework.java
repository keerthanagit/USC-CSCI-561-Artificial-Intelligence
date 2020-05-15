import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class homework {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        //Read the input
        InputReader fr = new InputReader(new File("D:\\USC\\Fall 2019\\AI\\HW\\Solutions\\Homework3\\src\\input.txt"));
        List<String> lines = fr.getLines();
        //Line 1 : Number of queries
        int numberOfQueries = Integer.parseInt(lines.get(0).trim());
        //N lines : Queries
        Queries queries = new Queries();
        for (int i = 1; i < numberOfQueries + 1; i++)
            queries.addQuery(convertToSentence(lines.get(i)));
        // Line N+1 : Number of sentences in KB
        int kbsize = Integer.parseInt(lines.get(numberOfQueries + 1).trim());
        //K lines : Knowedge base
        KnowledgeBase kb = new KnowledgeBase();
        for (int i = numberOfQueries + 2; i < numberOfQueries + kbsize + 2; i++)
            kb.addSentence(convertToSentence(lines.get(i).trim()));
        kb.standardizeKb();
        //Form the result
        Resolution_test resolution = new Resolution_test();
        String result = "";
        for (Sentence query : queries.getQueries()) {
            result += resolution.startResolution((KnowledgeBase) kb.clone(), query).trim() + "\n";
        }
        //Write the output
        File file = new File("D:\\USC\\Fall 2019\\AI\\HW\\Solutions\\Homework3\\src\\output.txt");
        OutputWriter ow = new OutputWriter(file);
        ow.putLines(result);
    }

    private static Predicate convertToPredicate(String s) {
        Predicate p = new Predicate();
        //Begin : For predicate name
        String predicateName = null;
        String regexString1 = "(^.*?)" + Pattern.quote("(");
        Pattern pattern1 = Pattern.compile(regexString1);
        Matcher matcher1 = pattern1.matcher(s);

        while (matcher1.find()) {
            predicateName = matcher1.group(1);
            if (predicateName.startsWith("~")) {
                predicateName = predicateName.substring(1);
                p.isNegated = true;
            }
            p.name = predicateName.trim();
        }
        //End : For predicate name
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        //Begin : For arguments

        String arguments = null;
        String regexString2 = Pattern.quote("(") + "(.*?)" + Pattern.quote(")");
        Pattern pattern2 = Pattern.compile(regexString2);
        Matcher matcher2 = pattern2.matcher(s);

        while (matcher2.find()) {
            arguments = matcher2.group(1);
        }

        String[] parts = arguments.split(",");
        for (String part : parts) {
            String temp = part.trim();
            ArgumentType type;
            if (/*temp.length() == 1 && */!Character.isUpperCase(temp.charAt(0)))
                type = ArgumentType.Variable;
            else
                type = ArgumentType.Constant;
            p.arguments.add(new Argument(temp, type));

        }

        return p;
    }

    private static Sentence convertToSentence(String s) {
        Sentence sent = new Sentence();
        String[] conj = s.split("&|=>");
        for (int i = 0; i < conj.length; i++) {
            Predicate p = convertToPredicate(conj[i].trim());
            if (i != conj.length - 1)
                p.isNegated = !p.isNegated;
            sent.addTerm(p);
        }
        return sent;
    }

}
