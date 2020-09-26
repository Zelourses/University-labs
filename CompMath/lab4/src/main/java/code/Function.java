package code;

public class Function {
    private String name;
    private DoubleFunction function;

    public Function(DoubleFunction function, String name){
        this.function = function;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DoubleFunction getFunction() {
        return function;
    }
}
