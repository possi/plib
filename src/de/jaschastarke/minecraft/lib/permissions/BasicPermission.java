package de.jaschastarke.minecraft.lib.permissions;

import org.bukkit.permissions.PermissionDefault;

public class BasicPermission implements IPermission {
    private PermissionDefault def = PermissionDefault.FALSE;
    private IAbstractPermission parent;
    private String name;
    
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
        return parent.getFullString() + SEP + name;
    }

    @Override
    public PermissionDefault getDefault() {
        return def;
    }

}
