package net.minecraft.client.gui.chat;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;

public class OverlayChatListener implements ChatListener
{
    private final Minecraft minecraft;

    public OverlayChatListener(Minecraft pMinecraft)
    {
        this.minecraft = pMinecraft;
    }

    public void handle(ChatType pChatType, Component pMessage, UUID pSender)
    {
        this.minecraft.gui.setOverlayMessage(pMessage, false);
    }
}
