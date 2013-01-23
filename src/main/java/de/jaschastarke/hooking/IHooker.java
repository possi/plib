package de.jaschastarke.hooking;

public interface IHooker<T> {
    public void register(T hook);
    public void register(T hook, Priority priority);
    public void unregister(T hook);
}
