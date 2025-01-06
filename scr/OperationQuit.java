public class OperationQuit extends Operation {

    public OperationQuit(String[] inputs) {
        super(inputs, 1, "quit");
    }

    @Override
    protected void checkCommand() throws WrongInputFormatException {
        checkVariableNum();
    }

    @Override
    public Boolean run() throws WrongInputFormatException {
        checkCommand();
        return true;
    }
}