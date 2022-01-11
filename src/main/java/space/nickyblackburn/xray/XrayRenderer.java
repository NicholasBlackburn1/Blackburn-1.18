package space.nickyblackburn.xray;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import space.nickyblackburn.utils.Consts;

import java.util.List;
import java.util.regex.*;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;

import org.lwjgl.opengl.GL11;

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
            Consts.blocklistseenpos.add(pos);

            Consts.log("got about"+ Consts.blocklistseen.get(i));
            Consts.log("got x,y,z"+ " "+ Consts.blocklistseenpos.get(i));
         

        }

        Consts.log("got about  :"+" "+ Consts.blocklistseen.size()+ " "+ "blocks");

        
        
        
    }

    public void renderImportantBlocks(Minecraft minecraft ,PoseStack pose,MultiBufferSource p_113689_, int i){
        BlockInWorld blockinworld;
        BoundingBox bb;
        
        VertexConsumer vertexconsumer = p_113689_.getBuffer(RenderType.lines());
        blockinworld = new BlockInWorld(minecraft.level, (BlockPos) Consts.blocklistseenpos.get(i), false);
        
        bb = new BoundingBox(blockinworld.getPos());
    

        
        Consts.log("bounding box centers of block " + " "+ bb.getCenter());
        LevelRenderer.renderLineBox(pose, vertexconsumer, (double)bb.minX(), (double)bb.minY(), (double)bb.minZ(), (double)(bb.maxX() + 1), (double)(bb.maxY() + 1), (double)(bb.maxZ() + 1), 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
    }


    
}
