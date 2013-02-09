package de.jaschastarke.minecraft.lib.permissions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.permissions.PermissionDefault;

public class ParentPermissionContainerNode extends SimplePermissionContainerNode implements IChildPermissionContainer {
    private Map<IPermission, Boolean> childs = null;
    private IPermissionContainer container = null;
    protected PermissionDefault permDefault = PermissionDefault.FALSE;
    
    public ParentPermissionContainerNode(final IAbstractPermission parent, final String name) {
        super(parent, name);
    }
    public ParentPermissionContainerNode(final IAbstractPermission parent, final String name, final PermissionDefault def) {
        super(parent, name);
        permDefault = def;
    }
    public ParentPermissionContainerNode(final IAbstractPermission parent, final String name, final IPermissionContainer childcontainer) {
        super(parent, name);
        container = childcontainer;
    }
    public ParentPermissionContainerNode(final IAbstractPermission parent, final String name, final PermissionDefault def, final IPermissionContainer childcontainer) {
        super(parent, name);
        permDefault = def;
        container = childcontainer;
    }
    private void buildChildList() {
        IPermissionContainer cont = this.container != null ? this.container : this;
        Map<IPermission, Boolean> list = new HashMap<IPermission, Boolean>();
        for (Field field : cont.getClass().getFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                IsChildPermission ischild = field.getAnnotation(IsChildPermission.class);
                if (ischild != null) {
                    if (!IPermission.class.isAssignableFrom(field.getType()))
                        throw new IllegalArgumentException("A field that represents a child permission, has to implement interface IPermission: " + cont.getClass().getName() + IAbstractPermission.SEP + field.getName());
                    try {
                        IPermission perm = (IPermission) field.get(cont);
                        if (perm == null)
                            throw new IllegalArgumentException("The static field " + cont.getClass().getName() + "." + field.getName() + " is null");
                        if (perm instanceof IPermissionChild)
                            ((IPermissionChild) perm).addParentPermission(this);
                        list.put(perm, ischild.value());
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            }
        }
        childs = list;
    }

    @Override
    public Map<IPermission, Boolean> getChilds() {
        if (childs == null)
            buildChildList();
        return childs;
    }
    @Override
    public PermissionDefault getDefault() {
        return permDefault;
    }

}
