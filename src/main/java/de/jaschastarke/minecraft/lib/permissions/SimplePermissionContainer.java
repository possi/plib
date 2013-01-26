package de.jaschastarke.minecraft.lib.permissions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SimplePermissionContainer implements IContainer {
    private IPermission[] permarray = null;
    
    public IPermission[] getPermissions() {
        if (permarray == null) {
            List<IPermission> perms = new ArrayList<IPermission>();
            for (Field field : this.getClass().getFields()) {
                try {
                    if (IPermission.class.isAssignableFrom(field.getType())) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            perms.add((IPermission) field.get(null));
                        } else {
                            perms.add((IPermission) field.get(this));
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            permarray = perms.toArray(new IPermission[perms.size()]);
        }
        return permarray;
    }
    public IPermission getPermission(final String name) {
        /*List<String> parts = Arrays.asList(name.split(Pattern.quote(SEP)));
        String rest = null;
        if (parts.size() > 1) {
            name = parts.remove(0);
            rest = StringUtil.join(parts.toArray(new String[parts.size()]), SEP);
        }*/
        for (IPermission perm : getPermissions()) {
            if (perm.toString().equals(name)) {
                /*if (rest != null && ..) {
                    return perm.getPermission(rest);
                }*/
                return perm;
            }
        }
        return null;
    }
}
