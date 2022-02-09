package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.network.chat.Component;

public class ResettingTemplateWorldTask extends ResettingWorldTask
{
    private final WorldTemplate template;

    public ResettingTemplateWorldTask(WorldTemplate pTemplate, long pServerId, Component p_167670_, Runnable pTitle)
    {
        super(pServerId, p_167670_, pTitle);
        this.template = pTemplate;
    }

    protected void sendResetRequest(RealmsClient pClient, long pServerId) throws RealmsServiceException
    {
        pClient.resetWorldWithTemplate(pServerId, this.template.id);
    }
}
