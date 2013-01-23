package de.jaschastarke.bukkit.lib.commands;

import static org.junit.Assert.*;

import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;

import de.jaschastarke.IHasName;
import de.jaschastarke.bukkit.lib.commands.annotations.Alias;
import de.jaschastarke.bukkit.lib.commands.annotations.Description;
import de.jaschastarke.bukkit.lib.commands.annotations.IsCommand;
import de.jaschastarke.bukkit.lib.commands.annotations.NeedsPermission;
import de.jaschastarke.bukkit.lib.commands.annotations.Usage;
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
            @Usage("args...")
            public boolean a_exampleCommand(CommandContext context) {
                return true;
            }
            
            @IsCommand("declined")
            @NeedsPermission("other")
            public boolean b_declinedCommand(CommandContext context) {
                return true;
            }
        };
        main = MethodCommand.getMethodCommandsFor(container);
        
        context = new CommandContext(null);
        context.setPermissinManager(new PermissionManager(null) {
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

    @Test
    public void testGetMethodCommandsFor() {
        assertEquals(2, MethodCommand.getMethodCommandsFor(container).length);
        assertEquals(1, commands2.length);
    }

    @Test
    public void testGetName() {
        assertEquals("command", main[0].getName());
    }

    @Test
    public void testGetAliases() {
        assertArrayEquals(new String[]{"com",  "c"}, main[0].getAliases());
    }

    @Test
    public void testExecute() throws MissingPermissionCommandException, CommandException {
        assertEquals(true, main[0].execute(context, new String[0]));
    }

    @Test
    public void testBuildArgumentsCommandContextObjectArrayInt() {
        assertArrayEquals(new Object[]{null, "arg", "arg2"}, MethodCommand.buildArguments(null, new String[]{"arg", "arg2"}, 1));
        assertArrayEquals(new Object[]{context, "arg", null, null}, MethodCommand.buildArguments(context, new String[]{"arg"}, 4));
    }

    @Test
    public void testBuildArgumentsCommandContextObjectArray() {
        assertArrayEquals(new Object[]{null, "arg"}, MethodCommand.buildArguments(null, new String[]{"arg"}));
        assertArrayEquals(new Object[]{context, "arg", "arg2"}, MethodCommand.buildArguments(context, new String[]{"arg", "arg2"}));
    }

    @Test
    public void testGetRequiredPermissions() {
        IAbstractPermission[] perms = main[0].getRequiredPermissions();
        assertEquals(2, perms.length);
        assertEquals("example.command.super", perms[0].getFullString());
        assertEquals("example.command", perms[1].getFullString());
    }

    @Test
    public void testGetUsage() {
        assertEquals("args...", main[0].getUsage());
    }

    @Test
    public void testGetDescription() {
        assertEquals("command.example.text", main[0].getDescription().toString());
    }

    @Test
    public void testGetPackageName() {
        assertEquals("Test-Package", main[0].getPackageName());
        assertEquals(null, commands2[0].getPackageName());
    }

    @Test(expected = MissingPermissionCommandException.class)
    public void testRequiredPermissions() throws MissingPermissionCommandException, CommandException {
        main[1].execute(context, null);
    }

}
