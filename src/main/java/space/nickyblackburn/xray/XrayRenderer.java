package space.nickyblackburn.xray;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import space.nickyblackburn.utils.Consts;

import java.util.List;
import java.util.regex.*;

public class XrayRenderer {

    
    // gets the current block the player is standign on 
    public String getBlockKey(Minecraft minecraft,Player player){
            
        
        BlockInWorld blockinworld = new BlockInWorld(minecraft.level, player.getOnPos(), false);
        Block block = blockinworld.getState().getBlock();

        TranslatableComponent blockname = (TranslatableComponent) block.getName();
        
        Consts.log("Block type is "+ " "+ blockname.getKey());
         
        return blockname.getKey();
    }

    // should get blocks in the radius of 50 blocks but now if it gets block i need to save pos of blocks and get them to outline them 
    public void getBlocks(Minecraft minecraft,Player player){
        BlockPos pos;
        BlockInWorld blockinworld;
        Block block;

        for(int i = 0; i < 200; i++){
            pos = new BlockPos(player.getX()-i, player.getY()-i, player.getZ()-i);

            blockinworld = new BlockInWorld(minecraft.level, pos, false);
            block = blockinworld.getState().getBlock();
            TranslatableComponent blockname = (TranslatableComponent) block.getName();

            Consts.blocklistseen.add(blockname.getKey());
            Consts.log("got about"+ Consts.blocklistseen.get(i));
        }

        Consts.log("got about  :"+" "+ Consts.blocklistseen.size()+ " "+ "blocks");
        
        
    }

    
}
