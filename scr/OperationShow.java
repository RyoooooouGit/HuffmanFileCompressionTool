import java.io.*;
import java.util.*;

public class OperationShow extends OperationFile {
    private double time = 0;
    private FileNode rootFileNode;

    public OperationShow(String[] inputs) {
        super(inputs, 2, "show sourceFile.huffman");
    }

    @Override
    protected void printInformation() throws IOException {
        // 正式对文件处理预览
        rootFileNode.showFiles(0, new ArrayList<>());
        System.out.println("预览已生成");
        System.out.printf("预览用时: %.2fs\n", time);
    }

    @Override
    public Boolean run()
            throws WrongInputFormatException, IOException, ClassNotFoundException {
        // 预览前读取文件夹结构
        sourceFile = new File(inputs[1]);
        checkCommand();
        long stime = System.currentTimeMillis();
        FileInputStream fr = new FileInputStream(sourceFile);
        BufferedInputStream bis = new BufferedInputStream(fr);
        ObjectInputStream ois = new ObjectInputStream(bis);
        File file = (File) ois.readObject();
        rootFileNode = (FileNode) ois.readObject();
        ois.close();
        long etime = System.currentTimeMillis();
        time = ((double) (etime - stime)) / 1000;
        printInformation();
        return false;
    }
}
