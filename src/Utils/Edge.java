package Utils;

public class Edge {
    private TreeNode pre;
    private TreeNode next;
    private char label;

    public Edge(TreeNode pre, TreeNode next, char label) {
        this.pre = pre;
        this.next = next;
        this.label = label;
    }

    public TreeNode getPre() {
        return pre;
    }

    public void setPre(TreeNode pre) {
        this.pre = pre;
    }

    public TreeNode getNext() {
        return next;
    }

    public void setNext(TreeNode next) {
        this.next = next;
    }

    public char getLabel() {
        return label;
    }

    public void setLabel(char label) {
        this.label = label;
    }
}
