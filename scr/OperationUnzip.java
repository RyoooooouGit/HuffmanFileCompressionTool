import java.io.*;
import java.util.*;

public class OperationUnzip extends OperationFile {
    private double time = 0;
    private FileNode rootFileNode;

    public OperationUnzip(String[] inputs, Scanner inputScn) {
        super(inputs, 2, "unzip sourceFile.huffman", inputScn);
    }

    @Override
    protected void printInformation() throws IOException {
        System.out.println("解压已结束");
        System.out.printf("解压用时: %.2fs\n", time);
    }

    @Override
    public Boolean run()
            throws WrongInputFormatException, IOException, ClassNotFoundException {

        // 检测格式正误
        sourceFile = new File(inputs[1]);
        checkCommand();

        // 处理覆盖问题
        FileInputStream fr = new FileInputStream(sourceFile);
        BufferedInputStream bis = new BufferedInputStream(fr);
        ObjectInputStream ois = new ObjectInputStream(bis);
        destFile = (File) ois.readObject();
        if (checkCover()) {
            long stime = System.currentTimeMillis();
            System.out.print("当前解压进度:  0.0%");
            rootFileNode = (FileNode) ois.readObject();
            // 正式对文件处理解压
            long allLength = sourceFile.length();
            rootFileNode.unzipFile(bis, 0, allLength / 1000, allLength);

            System.out.println("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b当前解压进度: 100.0%");
            long etime = System.currentTimeMillis();
            time = ((double) (etime - stime)) / 1000;

            // 输出压缩信息
            printInformation();
        }
        ois.close();
        bis.close();
        fr.close();
        return false;
    }
}
