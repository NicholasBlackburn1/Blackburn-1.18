/**
 * this is where i build my commands and get them to work
 */

package space.nickyblackburn.commands.builder;

import java.util.List;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import space.nickyblackburn.utils.Consts;

public class CommandBuilder {
    
    
    String name;
    String desc;
    // this should build the commands
    public void Builder(String Name,String Des){

        this.name = Name;
        this.desc = Des;
    
    }


}
