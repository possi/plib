package de.jaschastarke.hooking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class HookList<T> implements Iterable<T> {
    public static class Entry<E> {
        protected Priority priority = Priority.DEFAULT;
        protected E obj;
        public Entry(final E object) {
            this.obj = object;
        }
        public Entry(final E object, final Priority priority) {
            this.obj = object;
            this.priority = priority;
        }
        public Priority getPriority() {
            return priority;
        }
        public E getValue() {
            return this.obj;
        }
    }
    private static Comparator<Entry<?>> sorter = new Comparator<Entry<?>>() {
        public int compare(final Entry<?> o1, final Entry<?> o2) {
            return new Integer(o1.getPriority().getValue()).compareTo(o2.getPriority().getValue());
        }
    };
    
    public class CustomIterator implements Iterator<T> {
        protected Iterator<Entry<T>> it;
        public CustomIterator(final Iterator<Entry<T>> parentIterator) {
            it = parentIterator;
        }
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }
        @Override
        public T next() {
            return it.next().getValue();
        }
        @Override
        public void remove() {
            it.remove();
        }
        
    }
    
    List<Entry<T>> hooks = new ArrayList<Entry<T>>();
    
    public void register(final T hook) {
        hooks.add(new Entry<T>(hook));
        this.sort();
    }
    public void register(final T hook, final Priority priority) {
        hooks.add(new Entry<T>(hook, priority));
        this.sort();
    }
    public void unregister(final T hook) {
        Iterator<Entry<T>> iterator = hooks.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getValue() == hook) {
                iterator.remove();
            }
        }
        this.sort();
    }
    protected void sort() {
        Collections.sort(hooks, sorter);
    }
    public void clear() {
        hooks.clear();
    }
    
    @Override
    public Iterator<T> iterator() {
        return new CustomIterator(hooks.iterator());
    }
}
