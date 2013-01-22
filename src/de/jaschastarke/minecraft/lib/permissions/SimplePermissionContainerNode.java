package de.jaschastarke.minecraft.lib.permissions;

public class SimplePermissionContainerNode extends SimplePermissionContainer implements IPermissionContainer {
    private IAbstractPermission parent = null;
    private String name = null;

    protected SimplePermissionContainerNode(IAbstractPermission parent, String name) {
        this.parent = parent;
        this.name = name;
    }
    protected SimplePermissionContainerNode(String name) {
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
