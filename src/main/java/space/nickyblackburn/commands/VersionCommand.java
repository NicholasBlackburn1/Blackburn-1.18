package space.nickyblackburn.commands;

import java.util.List;

public class VersionCommand implements CommandBase {

    @Override
    public String Name() {
       
        return "version";
    }

    @Override
    public String Aliases() {
        
        return "ver";
    }

    @Override
    public String Parameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String Description() {
        // TODO Auto-generated method stub
        return "A command that tells me the version!";
    }

    @Override
    public void runCommand() {
        
    }

    @Override
    public List<String> commandUsage() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
