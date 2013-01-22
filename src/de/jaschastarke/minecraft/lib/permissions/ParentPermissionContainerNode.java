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
    
    protected ParentPermissionContainerNode(IAbstractPermission parent, String name) {
        super(parent, name);
    }
    protected ParentPermissionContainerNode(IAbstractPermission parent, String name, PermissionDefault def) {
        super(parent, name);
        permDefault = def;
    }
    protected ParentPermissionContainerNode(IAbstractPermission parent, String name, IPermissionContainer childcontainer) {
        super(parent, name);
        container = childcontainer;
    }
    protected ParentPermissionContainerNode(IAbstractPermission parent, String name, PermissionDefault def, IPermissionContainer childcontainer) {
        super(parent, name);
        permDefault = def;
        container = childcontainer;
    }
    private void buildChildList() {
        IPermissionContainer container = this.container != null ? this.container : this;
        Map<IPermission, Boolean> list = new HashMap<IPermission, Boolean>();
        for (Field field : container.getClass().getFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                IsChildPermission ischild = field.getAnnotation(IsChildPermission.class);
                if (ischild != null) {
                    if (!IPermission.class.isAssignableFrom(field.getType()))
                        throw new IllegalArgumentException("A field that represents a child permission, has to implement interface IPermission: "+container.getClass().getName()+"."+field.getName());
                    try {
                        IPermission perm = (IPermission) field.get(container);
                        if (perm == null)
                            throw new IllegalArgumentException("The static field "+container.getClass().getName()+"."+field.getName()+" is null");
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
