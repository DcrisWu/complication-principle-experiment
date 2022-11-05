package RE;

import Utils.Tree;
import Utils.TreeNode;

import java.util.*;

public class ReParseToTree {

    private Queue<Character> queue;

    public ReParseToTree(String re) {
        queue = new LinkedList<>();
        for (int i = 0; i < re.length(); i++) {
            queue.add(re.charAt(i));
        }
        queue.add('%');
    }

    public Tree parse() {
        try {
            if (queue.isEmpty()) {
                return null;
            }
            char pre = queue.poll();
            if (pre == '%') {
                System.out.println("this regular expression is null");
                return null;
            }
            Deque<Character> operator = new LinkedList<>();
            Deque<TreeNode> exp = new LinkedList<>();
            //先处理首字符，首字符只能为 ε 字母 (
            if (pre != 'ε' && !Character.isLetter(pre) && pre != '(') {
                System.out.println("not a legal regular expression");
                return null;
            } else {
                int type = 0;
                if (Character.isLetter(pre)) {
                    type = 0;
                } else {
                    type = 4;
                }
                TreeNode treeNode = new TreeNode(pre, null, null);
                if (type == 4) {
                    operator.push('(');
                } else {
                    exp.push(treeNode);
                }
            }
            while (!queue.isEmpty()) {
                Character poll = queue.poll();
                if (!Character.isLetter(poll) && poll != '|' && poll != '*' && poll != '(' && poll != ')' && poll != '%') {
                    System.out.println("not a legal regular expression");
                    return null;
                }
                if (poll == '%') {
                    while (!operator.isEmpty()) {
                        if (exp.isEmpty()) {
                            System.out.println("not a legal regular expression");
                            return null;
                        }
                        TreeNode right = exp.pop();
                        TreeNode left = exp.pop();
                        exp.push(new TreeNode(operator.pop(), left, right));
                    }
                    if (exp.size() > 1) {
                        System.out.println("not a legal regular expression");
                        return null;
                    }
                    break;
                }
                if (Character.isLetter(poll)) {
                    TreeNode treeNode = new TreeNode(poll, null, null);
                    if (pre == '(') {
                        exp.push(treeNode);
                    }
//                else if (pre == '*' || Character.isLetter(pre) || pre == ')') {
//                    operator.push('+');
//                    exp.push(treeNode);
//                }
                    else if (pre == '|') {
//                    operator.push('|');
                        exp.push(treeNode);
                    } else {
                        operator.push('+');
                        exp.push(treeNode);
                    }
//                else {
//                    System.out.println("not a legal regular expression");
//                    return null;
//                }
                } else if (poll == '(') {
                    if (pre != '|') {
                        operator.push('+');
                    }
                    operator.push('(');
                } else if (poll == ')') {
                    // ')' 前面不可以有 |
                    if (pre == '|') {
                        System.out.println("not a legal regular expression");
                        return null;
                    }
//                    if (operator.isEmpty()) {
//                        System.out.println("not a legal regular expression");
//                        return null;
//                    }
                    char op = operator.pop();
                    while (op != '(') {
                        TreeNode right = exp.pop();
                        TreeNode left = exp.pop();
                        TreeNode now = new TreeNode(op, left, right);
                        exp.push(now);
//                        if (operator.isEmpty()) {
//                            System.out.println("not a legal regular expression");
//                            return null;
//                        }
                        op = operator.pop();
                    }
                } else if (poll == '*') {
//                if(exp.isEmpty()){
//                    System.out.println("not a legal regular expression");
//                    return null;
//                }

                    if (pre == '(' || pre == '|' || pre == '*') {
                        System.out.println("not a legal regular expression");
                        return null;
                    }
                    TreeNode now = new TreeNode('*', exp.pop(), null);
                    exp.push(now);
                } else {
//                    if (pre == '|' || pre == '(') {
//                        System.out.println("not a legal regular expression");
//                        return null;
//                    }
                    operator.push(poll);
                }
                pre = poll;
            }
            return new Tree(exp.pop());
        } catch (EmptyStackException e) {
            System.out.println("not a legal regular expression");
            return null;
        }
    }


    public static void main(String[] args) {
            ReParseToTree toTree = new ReParseToTree("c(a|b)*");
            System.out.println(toTree.parse());

    }
}
