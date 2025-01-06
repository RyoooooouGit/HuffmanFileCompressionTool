import java.io.*;
import java.util.*;

public class OperationZip extends OperationFile {
    private double time = 0;
    private FileNode rootFileNode;

    public OperationZip(String[] inputs, Scanner inputScn) {
        super(inputs, 3, "zip targetFile.huffman sourceFile", inputScn);
    }

    @Override
    protected void printInformation() throws IOException {
        System.out.println("压缩已结束");
        System.out.printf("压缩用时: %.2fs\n", time);
        long fileLength = rootFileNode.getAllLength();
        if (fileLength == 0) {
            System.out.println("压缩率: 空文件不存在压缩率");
        } else {
            long zipRate = (100 * destFile.length()) / fileLength;
            System.out.printf("压缩率: %2d%%\n", zipRate);
        }
    }

    @Override
    public Boolean run() throws WrongInputFormatException, IOException {

        // 检测格式正误
        destFile = new File(inputs[1]);
        sourceFile = new File(inputs[2]);
        checkCommand();

        // 处理覆盖问题
        if (checkCover()) {
            long stime = System.currentTimeMillis();
            System.out.print("当前压缩进度:  0.0%");
            FileOutputStream fw = new FileOutputStream(destFile);
            BufferedOutputStream bos = new BufferedOutputStream(fw);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            rootFileNode = new FileNode(sourceFile);

            // 压缩前检测文件夹结构
            rootFileNode.generateFileTree();
            long allLength = rootFileNode.getAllLength();
            long progressInOne = 2 * allLength / 1000;
            long progressAll = rootFileNode.generateHuffmanTree(0, progressInOne, allLength);
            oos.writeObject(sourceFile);
            oos.writeObject(rootFileNode);

            // 正式对文件处理压缩
            rootFileNode.zipFile(bos, progressAll, progressInOne, allLength);

            System.out.println("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b当前压缩进度: 100.0%");
            bos.flush();
            oos.close();
            bos.close();
            fw.close();
            long etime = System.currentTimeMillis();
            time = ((double) (etime - stime)) / 1000;

            // 输出压缩信息
            printInformation();
        }
        return false;
    }
}
