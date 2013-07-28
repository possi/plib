package de.jaschastarke.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Example DocComment
 * 
 * As the Test isn't processed by annotation processing we can't use a real doccomment.
 * Because that, we build it with String Builder
 * 
 * A List
 *  - Item 1
 *  - Item 2
 *
 *A last new Line
 * @author Jascha
 * @exampleannot
 * @someother annot with comment
 * @anotherannot with a multiline comment this time
 * the doc comment doesn't process as javadoc, yet. but it should follow our rules
 */
public class DocCommentTest {
    protected DocComment comment;
    
    private static String TITLE = "Example Doc-Comment";
    private static String LINE1 = "As the Test isn't processed by annotation processing we can't use a real doccomment.";
    private static String LINE2 = "Because that, we build it with String Builder";
    private static String LINE3 = "A List";
    private static String LINE4 = " - Item 1";
    private static String LINE5 = " - Item 2";
    private static String LINE6 = "A last new Line";
    
    private static String ANNOT1 = "annot with comment";
    private static String ANNOT2_1 = "with a multiline comment this time";
    private static String ANNOT2_2 = "the doc comment doesn't process as javadoc, yet. but it should follow our rules";
    
    
    @Before
    public void testSetup(){
        comment = new DocComment(new StringBuilder()
            .append(" ").append(TITLE).append("\r\n")
            .append(" \r\n")
            .append(" ").append(LINE1).append(" \n")
            .append(" ").append(LINE2).append("\n")
            .append(" \n")
            .append(" ").append(LINE3).append("\n")
            .append(" ").append(LINE4).append("\n")
            .append(" ").append(LINE5).append("\n")
            .append("\n")
            .append("").append(LINE6).append("\n")
            .append(" @author Jascha\n")
            .append(" @exampleannot\n")
            .append(" @someother ").append(ANNOT1).append("\n")
            .append(" @anotherannot ").append(ANNOT2_1).append("\n")
            .append(" ").append(ANNOT2_2).append("\n")
                .toString());
    }
    @Test
    public void testGetDescription() {
        assertEquals(TITLE+"\n\n"+LINE1+" "+LINE2+"\n\n"+LINE3+"\n"+LINE4+"\n"+LINE5+"\n\n"+LINE6, comment.getDescription());
    }

    @Test
    public void testGetShortDesc() {
        assertEquals(TITLE, comment.getShortDesc());
    }

    @Test
    public void testGetLongDesc() {
        assertEquals(LINE1+" "+LINE2+"\n\n"+LINE3+"\n"+LINE4+"\n"+LINE5+"\n\n"+LINE6, comment.getLongDesc());
    }

    @Test
    public void testGetAnnotationValue() {
        assertEquals("", comment.getAnnotationValue("exampleannot"));
        assertEquals(ANNOT1, comment.getAnnotationValue("someother"));
        assertEquals(ANNOT2_1+" "+ANNOT2_2, comment.getAnnotationValue("anotherannot"));
    }

}
