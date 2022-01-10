package space.nickyblackburn.xray;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import space.nickyblackburn.utils.Consts;

public class XrayRenderer {
    

    public String renderXRay(Minecraft minecraft,Player player){
     
        BlockInWorld blockinworld = new BlockInWorld(minecraft.level, player.getOnPos(), false);

        Block block = blockinworld.getState().getBlock();


        Consts.log("Block type is "+ " "+ block.getName().toString());
        return block.getName().getContents();
    }
}
