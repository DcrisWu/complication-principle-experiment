package FaFolder;

public class State {
    private int id;
    // 0 = start, 1 = mid, 2 = accept
    private int type = 1;

    public static int stateId = 0;

    public State( int type) {
        this.id = stateId++;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static int getStateId() {
        return stateId;
    }

    public static void setStateId(int stateId) {
        State.stateId = stateId;
    }
}
