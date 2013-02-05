package de.jaschastarke.minecraft.lib.permissions;

public class SimplePermissionContainerNode extends SimplePermissionContainer implements IPermissionContainer {
    private IAbstractPermission parent = null;
    private String name = null;

    public SimplePermissionContainerNode(final IAbstractPermission parent, final String name) {
        this.parent = parent;
        this.name = name;
    }
    public SimplePermissionContainerNode(final String name) {
        this.name = name;
    }
    
    public String toString() {
        return name != null ? name : super.toString();
    }
    public String getFullString() {
        return parent != null ? parent.getFullString() + SEP + toString() : toString();
    }
    public IAbstractPermission getParent() {
        return parent;
    }
}
