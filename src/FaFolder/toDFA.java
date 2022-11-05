package FaFolder;


import RE.ReParseToTree;
import Utils.MyHashSet;
import Utils.Tree;

import java.util.*;

public class toDFA {
    public DfaGraph Nfa2Dfa(Graph nfa) {
        List<Map<Character, Set<Integer>>> list = nfa.getList();
        List<Set<Integer>> epsilonClosure = new ArrayList<>();
        // tranChar 表示字符转换表
        Set<Character> tranChar = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            tranChar.addAll(list.get(i).keySet());
            // HashSet存的是改状态的epsilon的闭包
            // 用queue来实现该算法
            HashSet<Integer> hashSet = new HashSet<>();
            Queue<Integer> queue = new LinkedList<>();
            // queue一开始存入起始状态
            queue.add(i);
            while (!queue.isEmpty()) {
                Integer poll = queue.poll();
                hashSet.add(poll);
                if (list.get(poll).containsKey('\0')) {
                    queue.addAll(list.get(poll).get('\0'));
                }
            }
            // 将闭包后的HashSet加入到epsilonClosure中
            epsilonClosure.add(hashSet);
        }
        tranChar.remove('\0');
        Set<Integer> dfaAcceptBeforeMini = new HashSet<>();
        Set<Integer> dfaNotAcceptBeforeMini = new HashSet<>();
        List<Set<Integer>> dfaState = new ArrayList<>();
        Map<Integer, Map<Character, Integer>> move = new HashMap<>();
        int start = nfa.getStart();
        // 将NFA的初始状态的epsilon闭包加入到dfaState中，作为初始状态
        dfaState.add(epsilonClosure.get(start));
        // 将初始状态加入
        dfaNotAcceptBeforeMini.add(0);
        Queue<Integer> dfaStateQueue = new ArrayDeque<>();
        dfaStateQueue.add(0);
        while (!dfaStateQueue.isEmpty()) {
            Integer statePoll = dfaStateQueue.poll();
            for (Character character : tranChar) {
                // integers存储的是DFA state经过character转换后的集合的闭包
                HashSet<Integer> integers = new HashSet<>();
                Set<Integer> integerSet = dfaState.get(statePoll);
                for (Integer integer : integerSet) {
                    if (list.get(integer).containsKey(character)) {
                        for (Integer tranSta : list.get(integer).get(character)) {
                            integers.addAll(epsilonClosure.get(tranSta));
                        }
                    }
                }
                if (!integers.isEmpty()) {
                    boolean ifAdd = true;
                    for (int j = 0; j < dfaState.size(); j++) {
                        // 假设存在相同的元素
                        boolean isContain = true;
                        // 如果长度不一样，就直接isContain = false
                        if (dfaState.get(j).size() != integers.size()) {
                            isContain = false;
                        } else {
                            // 如果存在不同元素的话，就isContain = false，直接break
                            for (Integer integer : dfaState.get(j)) {
                                if (!integers.contains(integer)) {
                                    isContain = false;
                                    break;
                                }
                            }
                        }
                        // 如果这个集合已经出现过
                        if (isContain) {
                            ifAdd = false;
                            move.putIfAbsent(statePoll, new HashMap<>());
                            move.get(statePoll).putIfAbsent(character, j);
                        }
                    }
                    // 如果没有出现过，就将这个状态插入到dfaState中
                    if (ifAdd) {
                        dfaState.add(integers);
                        // 如果这个集合包含了nfa的终止状态，这个集合就是Accept集合
                        if (integers.contains(nfa.getEnd())) {
                            dfaAcceptBeforeMini.add(dfaState.size() - 1);
                        } else {
                            // 否则加入非终止集合
                            dfaNotAcceptBeforeMini.add(dfaState.size() - 1);
                        }
                        move.putIfAbsent(statePoll, new HashMap<>());
                        move.get(statePoll).putIfAbsent(character, dfaState.size() - 1);
                        dfaStateQueue.add(dfaState.size() - 1);
                    }
                }
            }
        }
        // minimize DFA
        // 最小化后的DFA非接收状态集合
        Set<Integer> dfaNotAcceptState = new HashSet<>(dfaNotAcceptBeforeMini);
        // 首先minimize非接收状态
        boolean needRefresh = true;
        while (needRefresh) {
            needRefresh = false;
            List<Integer> moveList = move.keySet().stream().toList();
            for (int i = 0; i < moveList.size(); i++) {
                for (int j = i + 1; j < moveList.size(); j++) {
                    Integer integer1 = moveList.get(i);
                    Integer integer2 = moveList.get(j);
                    if (!move.containsKey(integer1) || !move.containsKey(integer2)) {
                        continue;
                    }
                    // 如果是两种类型的状态，就直接跳过
                    if ((dfaAcceptBeforeMini.contains(integer1) && dfaNotAcceptBeforeMini.contains(integer2))
                            || (dfaAcceptBeforeMini.contains(integer2) && dfaNotAcceptBeforeMini.contains(integer1))) {
                        continue;
                    }
                    // 如果是在同一类型中，就判断是否对于每个转换字符char，转换后的结果都一样
                    // 假设是都一样的
                    boolean judge = true;
                    for (Character character : tranChar) {
                        if (!Objects.equals(move.get(integer1).get(character), move.get(integer2).get(character))) {
                            judge = false;
                            break;
                        }
                    }
                    // 如果有可以合并的DFA状态
                    if (judge) {
                        needRefresh = true;
                        dfaNotAcceptState.remove(integer2);
                        move.remove(integer2);
                        // 将转换后的状态，改为integer1
                        for (Integer integer : move.keySet()) {
                            for (Character character : tranChar) {
                                if (move.get(integer).get(character).equals(integer2)) {
                                    move.get(integer).put(character, integer1);
                                }
                            }
                        }
                    }
                }
            }
        }
        // 最小化后的接收状态集合
        Set<Integer> dfaAcceptState = new HashSet<>();
        // 不能转换的终止状态
        Set<Integer> notTran = new HashSet<>();
        for (Integer integer : dfaAcceptBeforeMini) {
            if (!move.containsKey(integer)) {
                notTran.add(integer);
            }else {
                dfaAcceptState.add(integer);
            }
        }
        // 如果没有转换出去的状态超过1个，就minimize
        if (notTran.size() > 1) {
            // 从没有转换出去的状态取一个代表
            Integer stand = notTran.stream().toList().get(0);
            dfaAcceptState.add(stand);
            // 然后在notTran中移除stand，再遍历一遍move中，如果转换后的状态在notTran中，就替换为stand
            notTran.remove(stand);
            for (Integer integer : move.keySet()) {
                for (Character character : tranChar) {
                    if (notTran.contains(move.get(integer).get(character))) {
                        move.get(integer).put(character, stand);
                    }
                }
            }
        }
        return new DfaGraph(0, dfaAcceptState, move);
    }

    public static void main(String[] args) {
        ReParseToTree reParseToTree = new ReParseToTree("(a|b)*ab(a|b)");
        Tree parse = reParseToTree.parse();
        toNFA toNFA = new toNFA();
        Graph graph = toNFA.ReToNFA(parse.getRoot());
        toDFA toDFA = new toDFA();
        DfaGraph dfaGraph = toDFA.Nfa2Dfa(graph);
        System.out.println(dfaGraph);
    }
}

/*
class MyHashSet<E> extends HashSet<E> {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set<?>)) return false;
        Set<?> o1 = (Set<?>) o;
        if (this.size() != o1.size()) return false;
        boolean judge = true;
        for (Object o2 : o1) {
            if (!this.contains(02)) return false;
        }
        return true;
    }
}*/
