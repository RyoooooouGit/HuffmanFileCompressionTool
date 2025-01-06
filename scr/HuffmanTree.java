import java.io.*;
import java.util.*;

public class HuffmanTree<T> implements Serializable {
    private HuffmanNode<T> root;
    private HashMap<T, HuffmanNode<T>> nodeList;

    public HuffmanTree() {
        this.nodeList = new HashMap<>();
        this.root = null;
    }

    public void addNode(T val) {
        nodeList.putIfAbsent(val, new HuffmanNode<T>(val, 0));
    }

    public void addWeight(T name) {
        HuffmanNode<T> node = nodeList.get(name);
        node.setWeight(node.getWeight() + 1);
        return;
    }

    public void setHuffmanTree() {
        PriorityQueue<HuffmanNode<T>> nodeListForGenerateTree = new PriorityQueue<>(new Comparator<HuffmanNode<T>>() {
            public int compare(HuffmanNode<T> node1, HuffmanNode<T> node2) {
                return node1.getWeight() - node2.getWeight();
            }
        });
        nodeListForGenerateTree.addAll(nodeList.values());
        int listLength = nodeList.size();

        // 反复取权最小节点生成新节点
        for (int i = 0; i < listLength - 1; i++) {
            HuffmanNode<T> nodeF, nodeS, newNode;
            int newWeight;
            nodeF = nodeListForGenerateTree.poll();
            nodeS = nodeListForGenerateTree.poll();
            newWeight = nodeF.getWeight() + nodeS.getWeight();
            newNode = new HuffmanNode<>(newWeight, nodeF, nodeS);
            nodeListForGenerateTree.add(newNode);
        }
        this.root = nodeListForGenerateTree.peek();

        // 只有一个字符的特殊情况
        if (root.getLeft() == null && root.getRight() == null) {
            HuffmanNode<T> newRoot = new HuffmanNode<>(0, root, new HuffmanNode<>(null, 0));
            root = newRoot;
        }

    }

    public String getHuffmanCode(T val) {
        return nodeList.get(val).getCode();
    }

    public void generateAllCode() {
        generateCode(root, "");
    }

    private void generateCode(HuffmanNode<T> node, String code) {
        if (node.getLeft() == null && node.getRight() == null) {
            node.setCode(code);
            return;
        }
        generateCode(node.getLeft(), code + "0");
        generateCode(node.getRight(), code + "1");
    }

    public HuffmanNode<T> getRoot() {
        return root;
    }
}
