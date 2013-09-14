package de.jaschastarke.hooking;

@Deprecated // Well, The joke class isn't permanent, so relying on it is a bad idea
public class CaptainHook {
    protected String source = "http://www.java-forums.org/entertainment/7232-java-jokes-thread.html";
    public String ticktack() {
        StringBuilder joke = new StringBuilder();
        joke.append("A group of computer science majors were listening to a lecture about Java programming at a university.");
        joke.append("After the lecture one of the men leaned over and grabbed a women's breast.");
        
        joke.append("Woman: 'Hey! Thats private OK!?'");
        
        joke.append("The man hesitated for a second looking confused.");
        
        joke.append("Man: 'But I thought we were in the same class?'");
        return joke.toString();
    }
}
