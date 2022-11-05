package FaFolder;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
    private int start;
    private int end;
    private List<Map<Character, Set<Integer>>> list;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<Map<Character, Set<Integer>>> getList() {
        return list;
    }

    public void setList(List<Map<Character, Set<Integer>>> list) {
        this.list = list;
    }

    public Graph(int start, int end, List<Map<Character, Set<Integer>>> list) {
        this.start = start;
        this.end = end;
        this.list = list;
    }
}
