package de.jaschastarke.hooking;

public enum Priority {
    LOWEST(-2),
    LOW(-1),
    DEFAULT(0),
    HIGH(1),
    HIGHEST(2);
    
    protected int prio;
    private Priority(final int val) {
        prio = val;
    }
    public int getValue() {
        return prio;
    }
}
