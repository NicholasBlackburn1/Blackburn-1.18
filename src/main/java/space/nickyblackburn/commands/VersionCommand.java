package space.nickyblackburn.commands;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import space.nickyblackburn.utils.Consts;

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
        
        Consts.minecraft.gui.getChat().addMessage(new TextComponent(I18n.get(Consts.commandprefix+Name())));
    }

    @Override
    public List<String> commandUsage() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
