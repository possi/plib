package de.jaschastarke.bukkit.lib.commands;

public class AliasCommand<T extends ICommand> implements ICommand {
    private T cmd;
    private String name;
    private String[] aliases;
    
    public AliasCommand(final T cmd, final String name) {
        this(cmd, name, new String[0]);
    }
    public AliasCommand(final T cmd, final String name, final String[] aliases) {
        this.cmd = cmd;
        this.name = name;
        this.aliases = aliases;
    }
    public T getOriginal() {
        return cmd;
    }
    
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String[] getAliases() {
        return aliases;
    }
    @Override
    public boolean execute(final CommandContext context, final String[] args) throws MissingPermissionCommandException, CommandException {
        return cmd.execute(context, args);
    }
}
