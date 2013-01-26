package de.jaschastarke.hooking;

/**
 * The Boolean Hooker works more than a "Not-Hooker"
 * 
 * Iterates all registered Hooks and stops at the first that disagree the default value.
 */
public class BooleanHooker extends AbstractHooker<BooleanHooker.Check> {
    public interface Check {
        boolean test();
    }
    protected boolean def;
    public BooleanHooker(final boolean defaultValue) {
        def = defaultValue;
    }
    public boolean test() {
        for (Check c : hooks) {
            boolean ret = c.test();
            if (ret != def)
                return ret;
        }
        return def;
    }
}
