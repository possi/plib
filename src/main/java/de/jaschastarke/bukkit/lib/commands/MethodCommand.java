package de.jaschastarke.bukkit.lib.commands;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.jaschastarke.IHasName;
import de.jaschastarke.LocaleString;
import de.jaschastarke.bukkit.lib.commands.annotations.Alias;
import de.jaschastarke.bukkit.lib.commands.annotations.Description;
import de.jaschastarke.bukkit.lib.commands.annotations.IsCommand;
import de.jaschastarke.bukkit.lib.commands.annotations.NeedsPermission;
import de.jaschastarke.bukkit.lib.commands.annotations.Usages;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;

public class MethodCommand implements ICommand, IHelpDescribed {
    public static MethodCommand[] getMethodCommandsFor(final Object obj) {
        List<MethodCommand> list = new ArrayList<MethodCommand>();
        for (Method method : obj.getClass().getMethods()) {
            if (method.getAnnotation(IsCommand.class) != null) {
                list.add(new MethodCommand(obj, method));
            }
        }
        return list.toArray(new MethodCommand[list.size()]);
    }
    
    protected Object commandclass;
    protected Method method;
    protected String[] aliases;
    protected String name;
    protected String[] usages = new String[0];
    protected CharSequence description;
    protected IAbstractPermission[] permissions;
    protected IAbstractPermission[] relatedPermissions;
    
    public MethodCommand(final Object commandcls, final Method method) {
        commandclass = commandcls;
        this.method = method;
        
        initialize();
    }
    
    private void initialize() {
        IsCommand cmd = method.getAnnotation(IsCommand.class);
        if (cmd == null)
            throw new IllegalArgumentException("Given Method isn't a command");
        name = cmd.value();
        
        List<String> als = new ArrayList<String>();
        List<IAbstractPermission> perms = new ArrayList<IAbstractPermission>();
        List<IAbstractPermission> optPerms = new ArrayList<IAbstractPermission>();
        
        for (Annotation annot : method.getAnnotations()) {
            if (annot instanceof Alias) {
                als.addAll(Arrays.asList(((Alias) annot).value()));
            } else if (annot instanceof NeedsPermission) {
                if (commandclass instanceof IMethodCommandContainer) {
                    for (String permnode : ((NeedsPermission) annot).value()) {
                        IAbstractPermission perm = ((IMethodCommandContainer) commandclass).getPermission(permnode);
                        if (perm == null)
                            throw new IllegalArgumentException("Permission " + permnode + " not found in " + commandclass.getClass().getName());
                        
                        optPerms.add(perm);
                        if (!((NeedsPermission) annot).optional())
                            perms.add(perm);
                    }
                } else {
                    throw new IllegalArgumentException("Method-Commands with permission-annotation needs to be in a MethodCommandContainer");
                }
            } else if (annot instanceof Description) {
                if (((Description) annot).translate())
                    description = new LocaleString(((Description) annot).value());
                else
                    description = ((Description) annot).value();
            } else if (annot instanceof Usages) {
                usages = ((Usages) annot).value();
            }
        }
        aliases = als.toArray(new String[als.size()]);
        permissions = perms.toArray(new IAbstractPermission[perms.size()]);
        relatedPermissions = optPerms.toArray(new IAbstractPermission[optPerms.size()]);
    }
    
    private void checkPermissions(final CommandContext context) throws MissingPermissionCommandException {
        for (IAbstractPermission perm : permissions) {
            if (!context.checkPermission(perm))
                throw new MissingPermissionCommandException(perm);
        }
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
        checkPermissions(context);
        try {
            return (Boolean) method.invoke(commandclass, buildArguments(context, args, method.getParameterTypes().length));
        } catch (IllegalArgumentException e) {
            throw new IllegalCommandMethodException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalCommandMethodException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof MissingPermissionCommandException)
                throw (MissingPermissionCommandException) e.getCause();
            if (e.getCause() instanceof CommandException)
                throw (CommandException) e.getCause();
            throw new IllegalCommandMethodException(e);
        }
    }

    /**
     * @param minArguments The count of required arguments by the Method (including the command context), assuming every
     * argument is optional (so filled with nulls)
     */
    protected static Object[] buildArguments(final CommandContext context, final Object[] args, final int minArguments) {
        int length = Math.max(minArguments, args.length + 1);
        Object[] newArgs = new Object[length];
        newArgs[0] = context;
        System.arraycopy(args, 0, newArgs, 1, args.length);
        return newArgs;
    }
    protected static Object[] buildArguments(final CommandContext context, final Object[] args) {
        return buildArguments(context, args, 0);
    }

    @Override
    public IAbstractPermission[] getRequiredPermissions() {
        return permissions;
    }
    @Override
    public String[] getUsages() {
        return usages;
    }
    @Override
    public CharSequence getDescription() {
        return description;
    }
    @Override
    public String getPackageName() {
        return commandclass instanceof IHasName ? ((IHasName) commandclass).getName() : null;
    }
}
