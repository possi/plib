package de.jaschastarke.minecraft.lib.permissions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SimplePermissionContainer implements IPermissionContainer {
    private IPermission[] permarray = null;
    
    public IPermission[] getPermissions() {
        if (permarray == null) {
            List<IPermission> perms = new ArrayList<IPermission>();
            for (Field field : this.getClass().getDeclaredFields()) {
                try {
                    if (field.getType().isAssignableFrom(IPermission.class)) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            perms.add((IPermission) field.get(null));
                        } else {
                                perms.add((IPermission) field.get(this));
                        }
                    }
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }
            }
            permarray = perms.toArray(new IPermission[perms.size()]);
        }
        return permarray;
    }
    public String getFullString() {
        return null;
    }
    public IAbstractPermission getParent() {
        return null;
    }
}
