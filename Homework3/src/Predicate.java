import java.util.ArrayList;
import java.util.List;

public class Predicate implements Cloneable {
    boolean isNegated = false;
    String name;
    List<Argument> arguments = new ArrayList<>();

    @Override
    public Object clone() throws CloneNotSupportedException {
        Predicate predicate = (Predicate) super.clone();
        List<Argument> args = new ArrayList<>();
        for (Argument argument : this.arguments) {
            args.add((Argument) argument.clone());
        }
        predicate.setArguments(args);
        return predicate;
    }

    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    public String print() {
        String result = "";
        if (this.isNegated)
            result += "~";
        result += this.name;
        String args = "";
        for (Argument argument : this.arguments) {
            args += argument.getName() + ",";
        }
        args = args.substring(0, args.length() - 1);
        result += "(" + args + ")";
        return result;
    }
}
