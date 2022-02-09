package net.minecraft.network.protocol.login;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundHelloPacket implements Packet<ServerLoginPacketListener>
{
    private final GameProfile gameProfile;

    public ServerboundHelloPacket(GameProfile pGameProfile)
    {
        this.gameProfile = pGameProfile;
    }

    public ServerboundHelloPacket(FriendlyByteBuf pGameProfile)
    {
        this.gameProfile = new GameProfile((UUID)null, pGameProfile.readUtf(16));
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeUtf(this.gameProfile.getName());
    }

    public void handle(ServerLoginPacketListener pHandler)
    {
        pHandler.handleHello(this);
    }

    public GameProfile getGameProfile()
    {
        return this.gameProfile;
    }
}
