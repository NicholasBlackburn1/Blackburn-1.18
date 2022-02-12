

/***
 * this class is for setting up twtich chat iirc in minecraft so we can see the messages from the irrc chat without an overlay
 */
package space.nickyblackburn.screens;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.entity.Entity;
import space.nickyblackburn.utils.Consts;

public class TwitchChatOverlay extends GuiComponent{
    
    Minecraft minecraft;
    Font font;
    List <String> chat;
    private long updateChatMS = 0L;

    // wats called when wana init the overlay 
    public TwitchChatOverlay(Minecraft mc){
        this.minecraft = mc;
        this.font = mc.font;
    }
    // renders the twitch chat 
    public void render(PoseStack pPoseStack){

        Entity entity = this.minecraft.getCameraEntity();

    }

    // draws the twtich chat on the overlay 
    //TODO: actually get twitch chat working 
    protected void drawTwitchChat(PoseStack pose){
        List<String> list = this.chat;

        if (list == null || System.currentTimeMillis() > this.updateChatMS){
            list.add("");
            list.add(Consts.TwitchCurrentSent+ " :"+ " "+ Consts.TwitchCurrentMessage);

        }
    }
}
