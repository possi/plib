package de.jaschastarke.bukkit.lib.commands;

import java.util.List;

import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;

public class AliasHelpedCommand<T extends IHelpDescribed> extends AliasCommand<T> implements IHelpDescribed, ITabCommand {
    public AliasHelpedCommand(final T cmd, final String name) {
        super(cmd, name);
    }
    public AliasHelpedCommand(final T cmd, final String name, final String[] aliases) {
        super(cmd, name, aliases);
    }

    @Override
    public IAbstractPermission[] getRequiredPermissions() {
        return getOriginal().getRequiredPermissions();
    }

    @Override
    public CharSequence[] getUsages() {
        return getOriginal().getUsages();
    }

    @Override
    public CharSequence getDescription() {
        return getOriginal().getDescription();
    }

    @Override
    public CharSequence getPackageName() {
        return getOriginal().getPackageName();
    }
    
    @Override
    public List<String> tabComplete(final CommandContext context, final String[] args) {
        ICommand o = getOriginal();
        if (o instanceof ITabCommand)
            return ((ITabCommand) o).tabComplete(context, args);
        return null;
    }
}
