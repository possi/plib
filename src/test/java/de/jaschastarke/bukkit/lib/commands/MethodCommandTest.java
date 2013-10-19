package de.jaschastarke.bukkit.lib.commands;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;

import de.jaschastarke.IHasName;
import de.jaschastarke.bukkit.lib.commands.annotations.Alias;
import de.jaschastarke.bukkit.lib.commands.annotations.Description;
import de.jaschastarke.bukkit.lib.commands.annotations.IsCommand;
import de.jaschastarke.bukkit.lib.commands.annotations.NeedsPermission;
import de.jaschastarke.bukkit.lib.commands.annotations.Usages;
import de.jaschastarke.bukkit.lib.commands.parser.TabCompletion;
import de.jaschastarke.bukkit.lib.permissions.PermissionManager;
import de.jaschastarke.minecraft.lib.permissions.BasicPermission;
import de.jaschastarke.minecraft.lib.permissions.IAbstractPermission;
import de.jaschastarke.minecraft.lib.permissions.IPermission;

public class MethodCommandTest {
    IMethodCommandContainer container;
    MethodCommand[] commands2;
    MethodCommand[] main;
    CommandContext context;
    
    private interface ICombinedContainer extends IMethodCommandContainer, IHasName {}
    
    @Before
    public void setUp() throws Exception {
        container = new ICombinedContainer() {
            private IPermission PERM_CONTAINER = new BasicPermission(null, "example");

            @Override // IMethodCommandContainer
            public IPermission getPermission(String subPerm) {
                return new BasicPermission(PERM_CONTAINER, subPerm);
            }
            
            @Override // IHasName
            public String getName() {
                return "Test-Package";
            }
            
            @IsCommand("command")
            @Alias({"com", "c"})
            @Description("command.example.text")
            @NeedsPermission({"command.super", "command"})
            @Usages("args...")
            public boolean exampleCommand(CommandContext context) {
                return true;
            }
            
            @IsCommand("declined")
            @NeedsPermission("other")
            public boolean declinedCommand(CommandContext context) {
                return true;
            }

            @Override
            public List<TabCompletion> getTabCompleter(MethodCommand cmd) {
                return null;
            }
        };
        main = MethodCommand.getMethodCommandsFor(container);
        
        context = new CommandContext(null);
        context.setPermissinManager(new PermissionManager() {
            @Override
            public boolean hasPermission(CommandSender player, IAbstractPermission perm) {
                return perm.getFullString().equals("example.command") ||
                        perm.getFullString().equals("example.command.super");
            }
        });
        
        commands2 = MethodCommand.getMethodCommandsFor(new Object() {
            @IsCommand("command")
            public boolean exampleCommand(CommandContext context) {
                return true;
            }
        });
    }
    
    private MethodCommand getMainMethodCommand(String name) {
        for (MethodCommand c : main) {
            if (c.getMethod().getName().equals(name))
                return c;
        }
        return null;
    }

    @Test
    public void testGetMethodCommandsFor() {
        assertEquals(2, MethodCommand.getMethodCommandsFor(container).length);
        assertEquals(1, commands2.length);
    }

    @Test
    public void testGetName() {
        assertEquals("command", getMainMethodCommand("exampleCommand").getName());
    }

    @Test
    public void testGetAliases() {
        assertArrayEquals(new String[]{"com",  "c"}, getMainMethodCommand("exampleCommand").getAliases());
    }

    @Test
    public void testExecute() throws MissingPermissionCommandException, CommandException {
        assertEquals(true, getMainMethodCommand("exampleCommand").execute(context, new String[0]));
    }
    
    @SuppressWarnings("unused")
    private void testArguments(CommandContext context, String... args) {
    }
    @SuppressWarnings("unused")
    private void testArguments2(CommandContext context, String arg1, String arg2, String arg3) {
    }
    @SuppressWarnings("unused")
    private void testArguments3(String... args) {
    }
    @SuppressWarnings("unused")
    private void testArguments4(String arg1) {
    }
    
    private Class<?>[] getParameters(String method) {
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.getName().equals(method))
                return m.getParameterTypes();
        }
        return null;
    }

    @Test
    public void testBuildArgumentsCommandContextObjectArrayClassArray() throws Exception {
        assertArrayEquals(new Object[]{null, new String[]{"arg", "arg2"}}, MethodCommand.buildArguments(null, new String[]{"arg", "arg2"}, getParameters("testArguments")));
        assertArrayEquals(new Object[]{null, "arg", null, null}, MethodCommand.buildArguments(null, new String[]{"arg"}, getParameters("testArguments2")));
        assertArrayEquals(new Object[]{context, "arg", "arg2", "arg3"}, MethodCommand.buildArguments(context, new String[]{"arg", "arg2", "arg3"}, getParameters("testArguments2")));
        
        assertArrayEquals(new Object[]{null, new String[0]}, MethodCommand.buildArguments(null, new String[]{}, getParameters("testArguments")));
        assertArrayEquals(new Object[]{context, new String[0]}, MethodCommand.buildArguments(context, new String[]{}, getParameters("testArguments")));
        assertArrayEquals(new Object[]{new String[0]}, MethodCommand.buildArguments(null, new String[]{}, getParameters("testArguments3")));
        assertArrayEquals(new Object[]{new String[0]}, MethodCommand.buildArguments(context, new String[]{}, getParameters("testArguments3")));
        
        assertArrayEquals(new Object[]{"arg1"}, MethodCommand.buildArguments(null, new String[]{"arg1"}, getParameters("testArguments4")));
        assertArrayEquals(new Object[]{null}, MethodCommand.buildArguments(context, new String[]{}, getParameters("testArguments4")));
    }
    
    @Test(expected = CommandException.class)
    public void testBuildArgumentsCommandContextObjectArrayClassArrayError() throws CommandException {
        MethodCommand.buildArguments(null, new String[]{"arg", "arg2"}, getParameters("testArguments4"));
    }

    @Test
    public void testBuildArgumentsCommandContextObjectArray() throws CommandException {
        assertArrayEquals(new Object[]{null, "arg"}, MethodCommand.buildArguments(null, new String[]{"arg"}));
        assertArrayEquals(new Object[]{context, "arg", "arg2"}, MethodCommand.buildArguments(context, new String[]{"arg", "arg2"}));
    }

    @Test
    public void testGetRequiredPermissions() {
        IAbstractPermission[] perms = getMainMethodCommand("exampleCommand").getRequiredPermissions();
        assertEquals(2, perms.length);
        assertEquals("example.command.super", perms[0].getFullString());
        assertEquals("example.command", perms[1].getFullString());
    }

    @Test
    public void testGetUsage() {
        assertEquals("args...", getMainMethodCommand("exampleCommand").getUsages()[0]);
    }

    @Test
    public void testGetDescription() {
        assertEquals("command.example.text", getMainMethodCommand("exampleCommand").getDescription().toString());
    }

    @Test
    public void testGetPackageName() {
        assertEquals("Test-Package", getMainMethodCommand("exampleCommand").getPackageName());
        assertEquals(null, commands2[0].getPackageName());
    }

    @Test(expected = MissingPermissionCommandException.class)
    public void testRequiredPermissions() throws MissingPermissionCommandException, CommandException {
        getMainMethodCommand("declinedCommand").execute(context, null);
    }

}
