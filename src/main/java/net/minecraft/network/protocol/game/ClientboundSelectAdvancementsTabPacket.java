package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

public class ClientboundSelectAdvancementsTabPacket implements Packet<ClientGamePacketListener>
{
    @Nullable
    private final ResourceLocation tab;

    public ClientboundSelectAdvancementsTabPacket(@Nullable ResourceLocation pBuffer)
    {
        this.tab = pBuffer;
    }

    public void handle(ClientGamePacketListener pHandler)
    {
        pHandler.handleSelectAdvancementsTab(this);
    }

    public ClientboundSelectAdvancementsTabPacket(FriendlyByteBuf pBuffer)
    {
        if (pBuffer.readBoolean())
        {
            this.tab = pBuffer.readResourceLocation();
        }
        else
        {
            this.tab = null;
        }
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeBoolean(this.tab != null);

        if (this.tab != null)
        {
            pBuffer.writeResourceLocation(this.tab);
        }
    }

    @Nullable
    public ResourceLocation getTab()
    {
        return this.tab;
    }
}
