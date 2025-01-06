import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner inputScn = new Scanner(System.in);
        String[] inputs;
        Boolean ifQuit = false;
        Operation op;
        while (!ifQuit) {

            // 接受指令
            inputs = inputScn.nextLine().split(" ");

            int length = inputs.length;
            if (length == 0) {
                break;
            }
            try {

                // 对不同指令进行不同操作
                switch (inputs[0]) {
                    case "quit":
                    case "q":
                        op = new OperationQuit(inputs);
                        break;
                    case "zip":
                    case "z":
                        op = new OperationZip(inputs, inputScn);
                        break;
                    case "unzip":
                    case "u":
                        op = new OperationUnzip(inputs, inputScn);
                        break;
                    case "show":
                    case "s":
                        op = new OperationShow(inputs);
                        break;
                    default:
                        throw new WrongInputFormatException("非法指令",
                                "\nquit\nzip targetFile.huffman sourceFile\nunzip sourceFile.huffman\nshow sourceFile.huffman");
                }
                ifQuit = op.run();

            } catch (ClassNotFoundException e) {
                System.out.println("无法调用类FileNode，操作已停止");
            } catch (IOException e) {
                System.out.println("文件读写出错，操作已停止");
            } catch (WrongInputFormatException e) {

                // 输入格式错误提示
                System.out.println("你的输入中含有" + e.getMessage());
                System.out.println("正确的输入格式为: " + e.type);
                System.out.println("请重新输入");

            }
        }
        inputScn.close();
    }
}

class WrongInputFormatException extends Exception {
    String type;

    WrongInputFormatException(String s, String type) {
        super(s);
        this.type = type;
    }
}