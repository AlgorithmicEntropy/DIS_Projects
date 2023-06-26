package etl.db;

public class Tuple<T1, T2> {
    private T1 t1;
    private T2 t2;

    public Tuple() {
    }

    public Tuple(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    // Getters and setters
    public T1 T1() {
        return t1;
    }

    public T2 T2() {
        return t2;
    }
}
