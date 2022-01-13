package space.nickyblackburn.features;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import space.nickyblackburn.utils.Consts;

public class PlayerCrit{

    // this allows me to do a packet jump
    private void doPacketJump(Minecraft mc)
	{
		double posX = mc.player.getX();
		double posY = mc.player.getY();
		double posZ = mc.player.getZ();
		
		sendPos(mc,posX, posY + 0.0625D, posZ, true);
		sendPos(mc,posX, posY, posZ, false);
		sendPos(mc,posX, posY + 1.1E-5D, posZ, false);
		sendPos(mc,posX, posY, posZ, false);
	}

    // allows me to send the pos update to the player
    private void sendPos(Minecraft mc,double x, double y, double z, boolean onGround)
	{
		mc.getConnection().send(new ServerboundMovePlayerPacket.Pos(x, y, z, onGround));
	}

    // enables me allowing to do a crtitcal 
    public void enableCrit(Minecraft mc , Player player,Boolean enabled){
        if(enabled){
            
            //TODO: ENABLE CRIT STUFF
            try{

                if(mc.crosshairPickEntity != null){
                            Consts.log("enabled crit");
                    
                            if( mc.crosshairPickEntity.getType() != null){

                                mc.gui.getChat().addMessage(new TextComponent(mc.crosshairPickEntity.getType().toString()));
                                doPacketJump(mc);
                                
                            

                        } else{

                            
                        }
                    }
                    }
                catch(Exception e){
                    Consts.error(e.toString());
                }
             
                Consts.log("disbaled crit");
            }
    
    }      
}
