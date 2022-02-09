package net.minecraftforge.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffectInstance;

public abstract class EffectRenderer
{
    public boolean shouldRender(MobEffectInstance effect)
    {
        return true;
    }

    public boolean shouldRenderInvText(MobEffectInstance effect)
    {
        return true;
    }

    public boolean shouldRenderHUD(MobEffectInstance effect)
    {
        return true;
    }

    public abstract void renderInventoryEffect(MobEffectInstance var1, EffectRenderingInventoryScreen<?> var2, PoseStack var3, int var4, int var5, float var6);

    public abstract void renderHUDEffect(MobEffectInstance var1, GuiComponent var2, PoseStack var3, int var4, int var5, float var6, float var7);
}
