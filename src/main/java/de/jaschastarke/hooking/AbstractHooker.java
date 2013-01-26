package de.jaschastarke.hooking;

public abstract class AbstractHooker<T> implements IHooker<T> {
    protected HookList<T> hooks = new HookList<T>();
    
    @Override
    public void register(final T hook) {
        hooks.register(hook);
    }

    @Override
    public void register(final T hook, final Priority priority) {
        hooks.register(hook, priority);
    }

    @Override
    public void unregister(final T hook) {
        hooks.unregister(hook);
    }

    public void clearHooks() {
        hooks.clear();
    }
}
