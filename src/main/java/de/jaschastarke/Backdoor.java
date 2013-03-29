package de.jaschastarke;

public class Backdoor {
    public String joke;
    public String source = "http://www.devtopics.com/best-programming-jokes/";
    
    public void install() {
        StringBuilder s = new StringBuilder();
        s.append("A programmer is walking along a beach and finds a lamp. He rubs the lamp, and a genie appears.");
        s.append("“I am the most powerful genie in the world. I can grant you any wish, but only one wish.”");
        
        s.append("The programmer pulls out a map, points to it and says, “I’d want peace in the Middle East.”");
        
        s.append("The genie responds, “Gee, I don’t know. Those people have been fighting for millenia.");
        s.append("I can do just about anything, but this is likely beyond my limits.”");
        
        s.append("The programmer then says, “Well, I am a programmer, and my programs have lots of users.");
        s.append("Please make all my users satisfied with my software and let them ask for sensible changes.”");
        
        s.append("At which point the genie responds, “Um, let me see that map again.”");
        joke = s.toString();
    }
}
