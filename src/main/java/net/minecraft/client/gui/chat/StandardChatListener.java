package net.minecraft.client.gui.chat;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;

public class StandardChatListener implements ChatListener
{
    private final Minecraft minecraft;

    public StandardChatListener(Minecraft pMinecraft)
    {
        this.minecraft = pMinecraft;
    }

    public void handle(ChatType pChatType, Component pMessage, UUID pSender)
    {
        if (pChatType != ChatType.CHAT)
        {
            this.minecraft.gui.getChat().addMessage(pMessage);
        }
        else
        {
            this.minecraft.gui.getChat().enqueueMessage(pMessage);
        }
    }
}
