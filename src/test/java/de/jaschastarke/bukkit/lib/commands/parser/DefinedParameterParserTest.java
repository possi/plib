package de.jaschastarke.bukkit.lib.commands.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DefinedParameterParserTest {
    private DefinedParameterParser someArgsIngoreAddWhitespace;
    private DefinedParameterParser value3ParamMix;
    private DefinedParameterParser value3NoParams;
    private DefinedParameterParser noValueLongFlag;
    private DefinedParameterParser noValue2Flags;
    private DefinedParameterParser noValueNoFlags;

    @Before
    public void setUp() throws Exception {
        noValueNoFlags = new DefinedParameterParser("arg1 -p param1 arg2 --param2 par2val --param3=par3val arg3", new String[0]);
        noValue2Flags = new DefinedParameterParser("arg1 -p -q arg2 arg3", new String[]{"p", "q"});
        noValueLongFlag = new DefinedParameterParser("arg1 --flag1 arg2", new String[]{"flag1", "flag2"});
        value3NoParams = new DefinedParameterParser("arg1 arg2 this is a long value", new String[0], 2);
        value3ParamMix = new DefinedParameterParser("arg1 -f -p param1 arg2 --this is -a value w. --params", new String[]{"f", "q"}, 2);
        someArgsIngoreAddWhitespace = new DefinedParameterParser("arg1    arg2  arg3", new String[]{"p", "q"});
    }
    

    @Test
    public void testConstructorArrayVsString() {
        assertEquals(new DefinedParameterParser("arg1   arg2 -p p1 arg3", new String[0]).getValue(),
                    new DefinedParameterParser(new String[]{"arg1", "", "", "arg2", "-p", "p1", "arg3"}, new String[0]).getValue());
    }

    @Test
    public void testGetParameter() {
        assertEquals("param1", noValueNoFlags.getParameter("-p"));
        assertEquals("param1", noValueNoFlags.getParameter("p"));
        assertEquals("par2val", noValueNoFlags.getParameter("--param2"));
        assertEquals("par2val", noValueNoFlags.getParameter("param2"));
        assertEquals("par3val", noValueNoFlags.getParameter("--param3"));
        assertEquals("par3val", noValueNoFlags.getParameter("param3"));
        assertEquals("param1", value3ParamMix.getParameter("-p"));
        assertNull(value3ParamMix.getParameter("--this"));
        assertNull(value3ParamMix.getParameter("this"));
        assertNull(value3ParamMix.getParameter("--params"));
        assertNull(value3ParamMix.getParameter("params"));
        assertNull(value3ParamMix.getParameter("-a"));
        assertNull(value3ParamMix.getParameter("a"));
    }

    @Test
    public void testGetArgument() {
        assertEquals("arg1", noValueNoFlags.getArgument(0));
        assertEquals("arg2", noValueNoFlags.getArgument(1));
        assertEquals("arg3", noValueNoFlags.getArgument(2));
        assertNull(noValueNoFlags.getArgument(3));
        assertEquals("arg1", noValue2Flags.getArgument(0));
        assertEquals("arg2", noValue2Flags.getArgument(1));
        assertEquals("arg3", noValue2Flags.getArgument(2));
        assertEquals("arg1", noValueLongFlag.getArgument(0));
        assertEquals("arg2", noValueLongFlag.getArgument(1));
        assertNull(noValueLongFlag.getArgument(2));
        assertEquals("arg1", value3NoParams.getArgument(0));
        assertEquals("arg2", value3NoParams.getArgument(1));
        assertEquals("this is a long value", value3NoParams.getArgument(2));
        assertNull(noValueLongFlag.getArgument(3));
        assertEquals("arg1", value3ParamMix.getArgument(0));
        assertEquals("arg2", value3ParamMix.getArgument(1));
        assertEquals("--this is -a value w. --params", value3ParamMix.getArgument(2));
        assertNull(value3ParamMix.getArgument(3));
        assertEquals("arg1", someArgsIngoreAddWhitespace.getArgument(0));
        assertEquals("arg2", someArgsIngoreAddWhitespace.getArgument(1));
        assertEquals("arg3", someArgsIngoreAddWhitespace.getArgument(2));
        assertNull(someArgsIngoreAddWhitespace.getArgument(3));
    }

    @Test
    public void testGetValue() {
        assertEquals("arg1 arg2 arg3", noValueNoFlags.getValue());
        assertEquals("arg1 arg2 arg3", noValue2Flags.getValue());
        assertEquals("arg1 arg2", noValueLongFlag.getValue());
        assertEquals("this is a long value", value3NoParams.getValue());
        assertEquals("--this is -a value w. --params", value3ParamMix.getValue());
        assertEquals("arg1 arg2 arg3", someArgsIngoreAddWhitespace.getValue());
    }

    @Test
    public void testGetArgumentCount() {
        assertEquals(3, noValueNoFlags.getArgumentCount());
        assertEquals(3, noValue2Flags.getArgumentCount());
        assertEquals(2, noValueLongFlag.getArgumentCount());
        assertEquals(3, value3NoParams.getArgumentCount());
        assertEquals(3, value3ParamMix.getArgumentCount());
        assertEquals(3, someArgsIngoreAddWhitespace.getArgumentCount());
    }

    @Test
    public void testGetParameterCount() {
        assertEquals(3, noValueNoFlags.getParameterCount());
        assertEquals(0, noValue2Flags.getParameterCount());
        assertEquals(0, noValueLongFlag.getParameterCount());
        assertEquals(0, value3NoParams.getParameterCount());
        assertEquals(1, value3ParamMix.getParameterCount());
        assertEquals(0, someArgsIngoreAddWhitespace.getParameterCount());
    }
    
    @Test
    public void testGetFlagCount() {
        assertEquals(0, noValueNoFlags.getFlagCount());
        assertEquals(2, noValue2Flags.getFlagCount());
        assertEquals(1, noValueLongFlag.getFlagCount());
        assertEquals(0, value3NoParams.getFlagCount());
        assertEquals(1, value3ParamMix.getFlagCount());
        assertEquals(0, someArgsIngoreAddWhitespace.getFlagCount());
    }

}
