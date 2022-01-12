/***
 * this is the interface for custom commands
 */

package space.nickyblackburn.command;

import java.util.List;

import net.minecraft.client.Minecraft;

public interface ICommandRegister{

    public void register(List<String> command,Minecraft mc);
    
    public String getName();

    public String getDesc();
}