package de.jaschastarke.hooking;

public class GenericHooker<T> extends AbstractHooker<GenericHooker.Check<T>> {
    public static interface Check<E> {
        E test(Object... objects);
    }
    
    protected T def;
    public GenericHooker(final T defaultValue) {
        def = defaultValue;
    }
    public T test(final Object... objects) {
        for (Check<T> c : hooks) {
            T val = c.test(objects);
            if (val != null)
                return val;
        }
        return def;
    }
}
