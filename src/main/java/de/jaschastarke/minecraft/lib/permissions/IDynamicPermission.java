package de.jaschastarke.minecraft.lib.permissions;

import java.util.Collection;

public interface IDynamicPermission {
    public Collection<IAbstractPermission> getPermissions();
}
