import java.io.*;
import java.util.*;

public abstract class OperationFile extends Operation {
    File sourceFile, destFile;
    Scanner inputScn;

    public OperationFile(String[] inputs, int variableNum, String kind, Scanner inputScn) {
        super(inputs, variableNum, kind);
        this.inputScn = inputScn;
    }

    public OperationFile(String[] inputs, int variableNum, String kind) {
        super(inputs, variableNum, kind);
    }

    // 判断格式
    @Override
    protected void checkCommand() throws WrongInputFormatException {
        checkVariableNum();
        if (!(inputs[1].endsWith(".huffman"))) {
            throw new WrongInputFormatException("我无法处理的" + inputs[1] + "文件", super.kind);
        }
        if (!sourceFile.exists()) {
            throw new WrongInputFormatException("无法找到的文件" + sourceFile.getName(), super.kind);
        }
    }

    // 判断覆盖
    protected Boolean checkCover() throws IOException {
        if (destFile.exists()) {
            System.out.println("文件" + destFile.getName() + "已存在，是否覆盖原文件？ (Y/N): ");
            String yesOrNoInput;
            while (true) {
                yesOrNoInput = inputScn.nextLine();
                if (yesOrNoInput.equalsIgnoreCase("Y")) {
                    destFile.delete();
                    return true;
                } else if (!yesOrNoInput.equalsIgnoreCase("N")) {
                    System.out.println("请重新输入：");
                } else {
                    System.out.println("不覆盖原文件，操作已停止");
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    protected abstract void printInformation() throws IOException;
}