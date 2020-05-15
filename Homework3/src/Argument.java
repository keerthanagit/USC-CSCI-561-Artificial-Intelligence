enum ArgumentType {
    Constant,
    Variable
}

public class Argument implements Cloneable {
    private String argumentName;
    private ArgumentType argumentType;

    Argument(String name, ArgumentType type) {
        this.argumentName = name;
        this.argumentType = type;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Argument argument = (Argument) super.clone();
        return argument;
    }

    public String getName() {
        return this.argumentName;
    }

    public void setName(String n) {
        this.argumentName = n;
    }

    public ArgumentType getType() {
        return this.argumentType;
    }

    public void setType(ArgumentType type) {
        this.argumentType = type;
    }
}
