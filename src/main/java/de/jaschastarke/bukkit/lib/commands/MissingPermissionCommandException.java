package de.jaschastarke.bukkit.lib.commands;

import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;
import de.jaschastarke.minecraft.lib.permissions.MissingPermissionException;

public class MissingPermissionCommandException extends MissingPermissionException {
    private static final long serialVersionUID = -1332607252999006027L;

    public MissingPermissionCommandException(final IAbstractPermission permission) {
        super(permission);
    }
}
