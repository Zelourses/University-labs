package essence;

public interface HaveAble<T> {
    void in(T arg);
    void out(T arg);

    String getName();
}
