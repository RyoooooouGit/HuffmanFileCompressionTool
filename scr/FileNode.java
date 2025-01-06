import java.io.*;
import java.util.*;

public class FileNode implements Serializable {
    private File file;
    private long fileLength;
    private ArrayList<FileNode> sonNodeList;
    private HuffmanTree<Byte> huffmanTree;

    public FileNode(File file) {
        this.file = file;
        sonNodeList = new ArrayList<>();
    }

    // 生成文件夹的树状结构
    public void generateFileTree() throws IOException {
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                FileNode sonNode = new FileNode(fileList[i]);
                sonNodeList.add(sonNode);
                sonNode.generateFileTree();
            }
        } else {
            fileLength = file.length();
        }
    }

    // 对每个文件生成对应的Huffman树
    public long generateHuffmanTree(long progressAll, long progressInOne, long allLength) throws IOException {
        if (file.isDirectory()) {
            for (FileNode sonNode : sonNodeList) {
                progressAll = sonNode.generateHuffmanTree(progressAll, progressInOne, allLength);
            }
        } else {
            huffmanTree = new HuffmanTree<>();
            FileInputStream fr = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fr);
            if (fileLength != 0) {
                int tempchar, nowRand = 0;
                long now = 0;
                long num1 = 0, num2 = 0;
                ArrayList<Long> randIndex = new ArrayList<>();
                Random rand = new Random();
                int RANDAMOUNT = 1000;
                Boolean ifPrint;
                for (int i = 0; i < RANDAMOUNT; i++) {
                    randIndex.add(rand.nextLong(fileLength));
                }
                randIndex.sort(Comparator.naturalOrder());
                while ((tempchar = bis.read()) != -1) {

                    // 计算进度
                    progressAll++;
                    ifPrint = false;
                    if (progressInOne != 0) {
                        if (progressAll % progressInOne == 0) {
                            num1 = (progressAll / progressInOne) / 10;
                            num2 = (progressAll / progressInOne) % 10;
                            ifPrint = true;
                        }
                    } else {
                        long temp = (progressAll * 500) / allLength;
                        num1 = temp / 10;
                        num2 = temp % 10;
                        ifPrint = true;
                    }
                    if (ifPrint)
                        System.out.printf("\b\b\b\b\b%2d.%1d%%", num1, num2);

                    // 生成节点，更新权重
                    huffmanTree.addNode((byte) tempchar);
                    if (nowRand < RANDAMOUNT && now == randIndex.get(nowRand)) {
                        while (nowRand < RANDAMOUNT - 1 &&
                                randIndex.get(nowRand + 1).equals(randIndex.get(nowRand)))
                            nowRand++;
                        nowRand++;
                        huffmanTree.addWeight((byte) tempchar);
                    }
                    now++;

                }
                bis.close();
                fr.close();
                huffmanTree.setHuffmanTree();
            }
        }
        return progressAll;
    }

    // 正式对文件处理压缩
    public long zipFile(BufferedOutputStream bos, long progressAll, long progressInOne, long allLength)
            throws IOException {
        // 深度优先找到文件
        if (huffmanTree != null) {
            // 判断文件是否为空，若为空则Huffman树无根节点
            if (huffmanTree.getRoot() != null) {
                int tempchar;

                // 生成所有字符编码，后续无需再整棵树寻找
                huffmanTree.generateAllCode();

                FileInputStream fr = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fr);
                String code;
                int realCode = 0, nowCodeLength = 0;
                long num1 = 0, num2 = 0, temp;
                Boolean ifPrint;
                while ((tempchar = bis.read()) != -1) {

                    // 计算进度
                    progressAll++;
                    if (num1 != 100) {
                        ifPrint = false;
                        if (progressInOne != 0) {
                            if (progressAll % progressInOne == 0) {
                                num1 = (progressAll / progressInOne) / 10;
                                num2 = (progressAll / progressInOne) % 10;
                                ifPrint = true;
                            }
                        } else {
                            temp = (progressAll * 500) / allLength;
                            num1 = temp / 10;
                            num2 = temp % 10;
                            ifPrint = true;
                        }
                        if (ifPrint)
                            System.out.printf("\b\b\b\b\b%2d.%1d%%", num1, num2);
                    }

                    // 每八位编码进行转换
                    code = huffmanTree.getHuffmanCode((byte) tempchar);
                    for (char bit : code.toCharArray()) {
                        realCode |= (bit - 48) << (7 - nowCodeLength);
                        nowCodeLength++;
                        if (nowCodeLength == 8) {
                            bos.write((byte) realCode);
                            nowCodeLength = 0;
                            realCode = 0;
                        }
                    }
                }

                // 处理文件末尾不到八位的编码
                if (nowCodeLength != 0)
                    bos.write((byte) realCode);
                bos.flush();

                bis.close();
                fr.close();
            }
        } else {
            for (FileNode sonNode : sonNodeList) {
                progressAll = sonNode.zipFile(bos, progressAll, progressInOne, allLength);
            }
        }

        // 为显示总压缩进度，返回当前压缩字符总数量
        return progressAll;
    }

    // 正式对文件处理解压
    public long unzipFile(BufferedInputStream bis, long progressAll, long progressInOne, long allLength)
            throws IOException {
        // 此处应为文件
        if (huffmanTree != null) {
            // 判断是否为空文件
            if (huffmanTree.getRoot() != null) {
                // 此处不为空文件
                FileOutputStream fw = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fw);
                int tempchar, leftOrRight;
                HuffmanNode<Byte> nodeNow = huffmanTree.getRoot();

                // 将信息写入文件，直到文件长度与解压前等长
                long now = 0;
                long num1 = 0, num2 = 0;
                while (now < fileLength) {

                    // 计算进度
                    progressAll++;
                    if (num1 != 100) {
                        Boolean ifPrint = false;
                        if (progressInOne != 0) {
                            if (progressAll % progressInOne == 0) {
                                num1 = (progressAll / progressInOne) / 10;
                                num2 = (progressAll / progressInOne) % 10;
                                ifPrint = true;
                            }
                        } else {
                            long temp = (progressAll * 500) / allLength;
                            num1 = temp / 10;
                            num2 = temp % 10;
                            ifPrint = true;
                        }
                        if (ifPrint)
                            System.out.printf("\b\b\b\b\b%2d.%1d%%", num1, num2);
                    }

                    // 根据读入的编码再Huffman树上移动，若为叶节点则写入文件
                    tempchar = bis.read();
                    for (int i = 0; i < 8; i++) {
                        leftOrRight = (tempchar >> (7 - i)) & 1;
                        if (leftOrRight == 1) {
                            nodeNow = nodeNow.getRight();
                        } else {
                            nodeNow = nodeNow.getLeft();
                        }
                        if (nodeNow.getLeft() == null && nodeNow.getRight() == null) {
                            bos.write(nodeNow.getVal());
                            now++;
                            if (now == fileLength)
                                break;
                            nodeNow = huffmanTree.getRoot();
                        }
                    }
                }
                bos.flush();
                bos.close();
                fw.close();
            } else {
                // 此处为空文件，直接创建文件
                file.createNewFile();
            }
        }

        // 此处应为文件夹
        else {
            // 创建文件夹，对子文件进行处理
            file.mkdirs();
            for (FileNode sonNode : sonNodeList) {
                progressAll = sonNode.unzipFile(bis, progressAll, progressInOne, allLength);
            }
        }
        return progressAll;
    }

    // 调取整个文件夹中所有文件的总长度
    public long getAllLength() throws IOException {
        long result = 0;
        if (!file.isDirectory()) {
            result = fileLength;
        } else {
            for (FileNode sonNode : sonNodeList) {
                result += sonNode.getAllLength();
            }
        }
        return result;
    }

    // 正式对文件处理预览
    public void showFiles(int level, ArrayList<Boolean> isLast) {

        // 根据层级输出前缀
        for (int i = 0; i < level; i++) {
            if (i == level - 1) {
                System.out.print(isLast.get(i) ? "└── " : "├── ");
            } else {
                System.out.print(isLast.get(i) ? "    " : "│   ");
            }

        }

        // 输出文件名
        System.out.println(file.getName());

        // 对子文件进行处理
        if (huffmanTree == null) {
            int sonNum = sonNodeList.size();
            isLast.add(false);
            for (int i = 0; i < sonNum; i++) {
                FileNode sonNode = sonNodeList.get(i);
                if (i == sonNum - 1) {
                    isLast.remove(level);
                    isLast.add(true);
                }
                sonNode.showFiles(level + 1, isLast);
            }
            isLast.remove(level);
        }

    }
}
