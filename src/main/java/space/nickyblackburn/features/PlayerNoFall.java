package space.nickyblackburn.features;

import net.minecraft.client.Minecraft;

public class PlayerNoFall {
    


    public void enbaleNofall(Minecraft mc){

        if(mc.player.fallDistance <= (mc.player.isFallFlying() ? 1 : 2))
			return;
		/**
		if(mc.player.isFallFlying() && mc.player.isCrouching()
			&& !isFallingFastEnoughToCauseDamage(player))
			return;
		
		mc.player.setE*/
    }
}
