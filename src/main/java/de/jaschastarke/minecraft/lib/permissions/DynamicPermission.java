package de.jaschastarke.minecraft.lib.permissions;

import java.util.ArrayList;
import java.util.Collection;

public abstract class DynamicPermission implements IDynamicPermission {
    protected abstract void buildPermissionsToCheck(Collection<IAbstractPermission> perms);

    protected IAbstractPermission parent;
    public DynamicPermission(final IAbstractPermission parent) {
        this.parent = parent;
    }
    public DynamicPermission() {
    }
    
    @Override
    public Collection<IAbstractPermission> getPermissions() {
        ArrayList<IAbstractPermission> list = new ArrayList<IAbstractPermission>();
        if (parent != null)
            list.add(parent);
        buildPermissionsToCheck(list);
        return list;
    }

}
