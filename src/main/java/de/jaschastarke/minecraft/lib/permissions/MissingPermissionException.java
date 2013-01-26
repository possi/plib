package de.jaschastarke.minecraft.lib.permissions;

public class MissingPermissionException extends Exception {
    private static final long serialVersionUID = 948517064535243917L;
    
    private IAbstractPermission perm;
    
    public MissingPermissionException(final IAbstractPermission permission) {
        super("Missing permission: " + permission.getFullString());
    }
    public IAbstractPermission getPermission() {
        return perm;
    }
}
