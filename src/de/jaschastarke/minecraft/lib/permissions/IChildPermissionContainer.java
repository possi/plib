package de.jaschastarke.minecraft.lib.permissions;

import java.util.Map;

public interface IChildPermissionContainer extends IPermission {
    public Map<IPermission, Boolean> getChilds();
}
