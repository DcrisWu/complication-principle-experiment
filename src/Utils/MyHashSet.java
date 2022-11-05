package Utils;

import java.util.HashSet;
import java.util.Set;

public class MyHashSet<E> extends HashSet<E> {
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
}
