package Utils;

public class TreeNode {
    // 节点的数据类型， 0 - 26字母，1 - 'ε'，2 - '|' 、'+' ，3 - '*'， 4 - '(', 5 - ')'
    private int type;
    // 节点的数据
    private char val;
    // 左子树
    private TreeNode left;
    // 右子树
    private TreeNode right;

    public TreeNode(char val, TreeNode left, TreeNode right) {
        if (Character.isLetter(val)) {
            type = 0;
        } else if (val == 'ε') {
            type = 1;
        } else if (val == '|' || val == '+') {
            type = 2;
        } else if (val == '*') {
            type = 3;
        }else {
            type = -1;
        }
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public char getVal() {
        return val;
    }

    public void setVal(char val) {
        this.val = val;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
