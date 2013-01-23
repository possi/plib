package de.jaschastarke.minecraft.lib.permissions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.permissions.PermissionDefault;

public class BasicPermission implements IPermission, IPermissionChild {
    private PermissionDefault def = PermissionDefault.FALSE;
    private IAbstractPermission parent;
    private String name;
    private List<IPermission> parents = new ArrayList<IPermission>();
    
    public BasicPermission(IAbstractPermission parent, String name) {
        this.parent = parent;
        this.name = name;
    }
    public BasicPermission(IAbstractPermission parent, String name, PermissionDefault defaultValue) {
        this.parent = parent;
        this.name = name;
        this.def = defaultValue;
    }
    @Override
    public IAbstractPermission getParent() {
        return parent;
    }

    @Override
    public String getFullString() {
        if (parent == null)
            return name;
        return parent.getFullString() + SEP + name;
    }

    @Override
    public PermissionDefault getDefault() {
        return def;
    }

    public String toString() {
        return name;
    }
    
    @Override
    public void addParentPermission(IPermission parent) {
        parents.add(parent);
    }
    
    public IPermission[] getParentPermissions() {
        return parents.toArray(new IPermission[parents.size()]);
    }
}
