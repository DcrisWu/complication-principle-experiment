package FaFolder;

import RE.ReParseToTree;
import Utils.Tree;
import Utils.TreeNode;

import java.util.*;

public class toNFA {

    // 返回一个NFA的图
    public Graph ReToNFA(TreeNode treeNode) {
        List<Map<Character, Set<Integer>>> res = new ArrayList<>();
        return dfs(res, treeNode);
    }

    public Graph dfs(List<Map<Character, Set<Integer>>> graph, TreeNode treeNode) {
        Map<Character, Set<Integer>> start;
        Map<Character, Set<Integer>> end;
        if (treeNode.getVal() == '*') {
            Graph leftData = dfs(graph, treeNode.getLeft());

            int startId = graph.size();
            int endId = graph.size() + 1;

            start = new HashMap<>();
            end = new HashMap<>();

            start.putIfAbsent('\0',new HashSet<>());
            start.get('\0').add(leftData.getStart());
            start.get('\0').add(endId);

            graph.add(start);
            graph.add(end);

            graph.get(leftData.getEnd() ).putIfAbsent('\0', new HashSet<>());
            graph.get(leftData.getEnd()).get('\0').add(leftData.getStart());
            graph.get(leftData.getEnd()).get('\0').add(endId);

            leftData.setStart(startId);
            leftData.setEnd(endId);
            return leftData;

        } else if (treeNode.getVal() == '|') {
            Graph leftData = dfs(graph, treeNode.getLeft());
            Graph rightData = dfs(graph, treeNode.getRight());

            int startId = graph.size();
            int endId = graph.size()+1;

            start = new HashMap<>();
            end = new HashMap<>();

            start.putIfAbsent('\0', new HashSet<>());
            start.get('\0').add(leftData.getStart());
            start.get('\0').add(rightData.getStart());

            graph.get(leftData.getEnd()).putIfAbsent('\0', new HashSet<>());
            graph.get(leftData.getEnd()).get('\0').add(endId);
            graph.get(rightData.getEnd()).putIfAbsent('\0',new HashSet<>());
            graph.get(rightData.getEnd()).get('\0').add(endId);

            graph.add(start);
            graph.add(end);

            Graph data = new Graph(startId,endId,graph);
            return data;
        } else if (treeNode.getVal() == '+') {
            Graph leftData = dfs(graph, treeNode.getLeft());
            Graph rightData = dfs(graph, treeNode.getRight());

            int startId = leftData.getStart();
            int endId = rightData.getEnd();
            graph.get(leftData.getEnd()).putIfAbsent('\0', new HashSet<>());
            graph.get(leftData.getEnd()).get('\0').add(rightData.getStart());

            Graph data = new Graph(startId,endId,graph);
            return data;
        } else {
            int startId = graph.size();
            int endId = graph.size() + 1;

            start = new HashMap<>();
            end = new HashMap<>();

            start.putIfAbsent(treeNode.getVal(), new HashSet<>());
            start.get(treeNode.getVal()).add(endId);

            graph.add(start);
            graph.add(end);

            Graph data = new Graph(startId, endId,graph);
            return data;
        }
    }

    public static void main(String[] args) {
        ReParseToTree reParseToTree = new ReParseToTree("a|c");
        Tree parse = reParseToTree.parse();
        toNFA toNFA = new toNFA();
        Graph graph = toNFA.ReToNFA(parse.getRoot());
        System.out.println(graph.getList());
    }
}
