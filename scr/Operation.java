import java.io.*;

public abstract class Operation {
    protected String kind;
    private int variableNum;
    protected String[] inputs;

    public Operation(String[] inputs, int variableNum, String kind) {
        this.inputs = inputs;
        this.variableNum = variableNum;
        this.kind = kind;
    }

    protected void checkVariableNum() throws WrongInputFormatException {
        if (inputs.length != variableNum) {
            throw new WrongInputFormatException("错误的变量数", kind);
        }
    }

    protected abstract void checkCommand() throws WrongInputFormatException;

    public abstract Boolean run()
            throws WrongInputFormatException, IOException, ClassNotFoundException;
}
