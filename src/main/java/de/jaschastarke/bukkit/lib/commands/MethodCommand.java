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
import de.jaschastarke.bukkit.lib.commands.parser.TabCompletion;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;
import de.jaschastarke.utils.ArrayUtil;
import de.jaschastarke.utils.ClassDescriptorStorage;
import de.jaschastarke.utils.ClassDescriptorStorage.ClassDescription;
import de.jaschastarke.utils.DocComment;

public class MethodCommand implements ICommand, IHelpDescribed, ITabComplete {
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
    protected List<TabCompletion> completer = null;
    
    public MethodCommand(final Object commandcls, final Method method) {
        commandclass = commandcls;
        this.method = method;
        
        initialize();
    }
    
    public void setDescription(final ClassDescriptorStorage cds) {
        if (description == null) {
            ClassDescription cls = cds.getClassFor(commandclass);
            if (cls != null) {
                DocComment el = cls.getElDocComment(method.getName());
                if (el != null)
                    description = el.getDescription();
            }
        }
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
            return (Boolean) method.invoke(commandclass, buildArguments(context, args, method.getParameterTypes()));
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

    public List<TabCompletion> getCompleter() {
        if (completer == null) {
            if (commandclass instanceof IMethodCommandContainer) {
                completer = ((IMethodCommandContainer) commandclass).getTabCompleter(this);
            }
            if (completer == null) {
                completer = new ArrayList<TabCompletion>();
                for (String u : getUsages()) {
                    TabCompletion tmp = TabCompletion.forUsageLine(u);
                    if (tmp != null)
                        completer.add(tmp);
                }
            }
        }
        return completer;
    }
    
    @Override
    public List<String> tabComplete(final CommandContext context, final String[] args) {
        for (IAbstractPermission perm : permissions) {
            if (!context.checkPermission(perm))
                return null;
        }
        List<String> hints = new ArrayList<String>();
        for (TabCompletion c : getCompleter()) {
            List<String> tmpHints = c.tabComplete(context, args);
            if (tmpHints != null)
                hints.addAll(tmpHints);
        }
        return hints;
    }

    /**
     * @param classes The list of required arguments by the Method (including the command context), assuming every
     * argument is optional (so filled with nulls)
     */
    protected static Object[] buildArguments(final CommandContext context, final Object[] args, final Class<?>[] classes) throws CommandException {
        if (classes == null) {
            Object[] newArgs = new Object[args.length + 1];
            newArgs[0] = context;
            System.arraycopy(args, 0, newArgs, 1, args.length);
            return newArgs;
        } else {
            Object[] newArgs = new Object[classes.length];
            int newArgsCopyOffset = 0;
            int argsCopyLength = args.length;
            
            if (classes.length > 0 && classes[0].isAssignableFrom(CommandContext.class)) {
                newArgs[0] = context;
                newArgsCopyOffset = 1;
            }
            if (classes.length > 0) {
                int l = classes.length - 1;
                if (classes[l].isArray() && (classes[l].getComponentType().equals(String.class) || classes[l].getComponentType().equals(Object.class))) {
                    argsCopyLength = classes.length - newArgsCopyOffset - 1;
                    if (classes[l].getComponentType().equals(String.class)) {
                        String[] subArgs = new String[args.length - argsCopyLength];
                        for (int i = 0; i < subArgs.length; i++) {
                            subArgs[i] = args[argsCopyLength + i] != null ? args[argsCopyLength + i].toString() : null;
                        }
                        newArgs[newArgs.length - 1] = subArgs;
                    } else {
                        newArgs[newArgs.length - 1] = ArrayUtil.getRange(args, argsCopyLength);
                    }
                }
            }
            
            if (argsCopyLength + newArgsCopyOffset > newArgs.length)
                throw new CommandException("Too much arguments");
            
            System.arraycopy(args, 0, newArgs, newArgsCopyOffset, argsCopyLength);
            return newArgs;
        }
    }
    protected static Object[] buildArguments(final CommandContext context, final Object[] args) throws CommandException {
        return buildArguments(context, args, null);
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
    public CharSequence getPackageName() {
        return commandclass instanceof IHasName ? ((IHasName) commandclass).getName()
                : (commandclass instanceof IHelpDescribed ? ((IHelpDescribed) commandclass).getPackageName() : null);
    }
    public Method getMethod() {
        return method;
    }
}
