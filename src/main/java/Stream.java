public interface Stream<T> {
    T next();
    boolean eof(int n);
    T peek(int n);
    void croak(String msg);

    default boolean eof() {
        return this.eof(0);
    }

    default T peek() {
        return this.peek(0);
    }
}
