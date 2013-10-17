package de.jaschastarke.bukkit.lib.commands;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import de.jaschastarke.bukkit.lib.commands.TabCompletionHelper.StringArrayCompleter;
import de.jaschastarke.bukkit.lib.commands.TabCompletionHelper.PlayerCompleter;
import de.jaschastarke.bukkit.lib.commands.TabCompletionHelper.StaticCompleter;
import de.jaschastarke.bukkit.lib.commands.TabCompletionHelper.WorldCompleter;

public class TabCompletionHelperTest {
    private static <T> void assertCollection(T[] expected, List<T> actual) {
        assertThat(actual, IsIterableContainingInAnyOrder.containsInAnyOrder(expected));
    }
    
    @Test
    public void testTabComplete() {
        TabCompletionHelper completer = TabCompletionHelper.forUsageLine("--foo -b --far |none| <x1> unknown [x3]");
        completer.setCompleter("x1", new StringArrayCompleter(new String[] {"foo", "bar", "bqwertz"}));
        completer.setCompleter("x3", new StringArrayCompleter(new String[] {"abc", "def", "ghi"}));
        
        
        assertCollection(new String[]{"--foo", "-b", "--far", "none"}, completer.tabComplete(null, new String[]{""}));
        assertCollection(new String[]{"foo", "bar", "bqwertz"}, completer.tabComplete(null, new String[]{"none", ""}));
        
        assertCollection(new String[]{"none"}, completer.tabComplete(null, new String[]{"n"}));

        assertCollection(new String[]{"bar", "bqwertz"}, completer.tabComplete(null, new String[]{"none", "b"}));
        assertCollection(new String[]{"--foo", "-b", "--far"}, completer.tabComplete(null, new String[]{"-"}));
        assertCollection(new String[]{"--foo", "--far"}, completer.tabComplete(null, new String[]{"--"}));
        assertCollection(new String[]{"--foo", "--far"}, completer.tabComplete(null, new String[]{"--f"}));
        assertCollection(new String[]{"--foo", "--far"}, completer.tabComplete(null, new String[]{"--f"}));

        assertCollection(new String[]{}, completer.tabComplete(null, new String[]{"asd"}));
        assertCollection(new String[]{"abc", "def", "ghi"}, completer.tabComplete(null, new String[]{"--far", "none", "foo", "something", ""}));
        assertCollection(new String[]{"abc"}, completer.tabComplete(null, new String[]{"--far", "-b", "none", "foo", "something", "a"}));
    }

    @Test
    public void testForUsageLine() {
        TabCompletionHelper completer = TabCompletionHelper.forUsageLine("--foo -b --ar -x -y -z |none| <player> unknown [world]");
        
        Map<String, String> params = completer.getParams();
        String[] args = completer.getArguments();
        assertEquals(6, params.size());
        
        assertArrayEquals(new String[]{"none",  "player", "unknown", "world"}, args);

        assertNull(completer.getCompleter("unknown"));
        assertTrue(completer.getCompleter("none") instanceof StaticCompleter);
        assertTrue(completer.getCompleter("player") instanceof PlayerCompleter);
        assertTrue(completer.getCompleter("world") instanceof WorldCompleter);
        
        assertTrue(params.containsKey("--foo"));
        assertNull(params.get("--foo"));
        assertFalse(params.containsKey("--notthere"));
    }

}
