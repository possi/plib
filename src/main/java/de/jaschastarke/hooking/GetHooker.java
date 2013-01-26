package de.jaschastarke.hooking;

public class GetHooker<T> extends AbstractHooker<GetHooker.Check<T>> {
    public static interface Check<E> {
        E test();
    }
    
    protected T def = null;
    public GetHooker() {
    }
    public GetHooker(final T defaultValue) {
        def = defaultValue;
    }
    public T test() {
        for (Check<T> c : hooks) {
            T val = c.test();
            if (val != null)
                return val;
        }
        return def;
    }
}
