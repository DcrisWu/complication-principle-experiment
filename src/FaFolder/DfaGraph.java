package FaFolder;

import java.util.Map;
import java.util.Set;

public class DfaGraph {
    Integer begin;
    Set<Integer> acceptStates;
    Map<Integer, Map<Character, Integer>> move;

    public DfaGraph() {
    }

    public Integer getBegin() {
        return begin;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    public Set<Integer> getAcceptStates() {
        return acceptStates;
    }

    public void setAcceptStates(Set<Integer> acceptStates) {
        this.acceptStates = acceptStates;
    }

    public Map<Integer, Map<Character, Integer>> getMove() {
        return move;
    }

    public void setMove(Map<Integer, Map<Character, Integer>> move) {
        this.move = move;
    }

    public DfaGraph(Integer begin, Set<Integer> acceptStates, Map<Integer, Map<Character, Integer>> move) {
        this.begin = begin;
        this.acceptStates = acceptStates;
        this.move = move;
    }
}
