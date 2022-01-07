import java.util.List;

/**
 * this is where i have all my commands start from
 */

public interface CommandBase{

    // returns the name of the command 
    public String Name();
    
    // command alicesse
    public String Aliases();

    // Command parms 
    public String Parameters();

    // returns the dec of the command 
    public String Description();

    // Where the command acually executes
    public void runCommand();

    public List<String> commandUsage();

    
    

    

    


}
