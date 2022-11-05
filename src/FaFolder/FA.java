package FaFolder;

import Utils.Edge;
import org.jgrapht.graph.DirectedPseudograph;

public class FA {
    protected State startState;
    protected DirectedPseudograph<State, Edge> transitTable;

    public FA() {
        startState = new State(0);
        transitTable = new DirectedPseudograph<State, Edge>(Edge.class);
    }

    public State getStartState() {
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public DirectedPseudograph<State, Edge> getTransitTable() {
        return transitTable;
    }

    // 将 FA merge 到 transitTable
    public void merge(FA fa) {
        //对于 fa 的每一个末端节点，添加到transitTable中
        // 先遍历每一条边
        for (Edge edge : fa.getTransitTable().edgeSet()) {
            // 获取这条边指向的节点
            transitTable.addVertex(fa.getTransitTable().getEdgeTarget(edge));
            // 获取这条边的源节点
            transitTable.addVertex(fa.getTransitTable().getEdgeSource(edge));
            transitTable.addEdge(fa.getTransitTable().getEdgeSource(edge), fa.getTransitTable().getEdgeTarget(edge), edge);
        }
    }
}
