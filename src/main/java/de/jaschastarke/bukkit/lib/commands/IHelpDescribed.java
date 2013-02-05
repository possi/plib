package de.jaschastarke.bukkit.lib.commands;

import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;

public interface IHelpDescribed extends ICommand {
    public String getName();
    
    /**
     * Should return a list of Permissions. Only if at least one of it is given, the command is included in help view
     */
    public IAbstractPermission[] getRequiredPermissions();
    
    /**
     * A simple usage Example, but it may be formated with special characters that gets formated by the Help. So is
     * an argument surround by [] interpreted as an optional argument and colored gray. Required parameters may be
     * marked with <> and gets colored darkred. All -p parameters may colored darkgray.
     */
    public CharSequence[] getUsages();
    
    public CharSequence getDescription();
    
    public CharSequence getPackageName();
    
    public String[] getAliases();
}
