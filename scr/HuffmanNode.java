import java.io.Serializable;

public class HuffmanNode<T> implements Serializable {
    private T val;
    private int weight;
    private HuffmanNode<T> left;
    private HuffmanNode<T> right;
    private String code;

    public HuffmanNode(T val, int weight) {
        this.val = val;
        this.weight = weight;
        this.left = null;
        this.right = null;
        this.code = null;
    }

    public HuffmanNode(int weight, HuffmanNode<T> left, HuffmanNode<T> right) {
        this.val = null;
        this.weight = weight;
        this.left = left;
        this.right = right;
    }

    public T getVal() {
        return val;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public HuffmanNode<T> getLeft() {
        return left;
    }

    public HuffmanNode<T> getRight() {
        return right;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
