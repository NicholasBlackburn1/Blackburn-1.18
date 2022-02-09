package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.util.WorldGenerationInfo;
import net.minecraft.network.chat.Component;

public class ResettingGeneratedWorldTask extends ResettingWorldTask
{
    private final WorldGenerationInfo generationInfo;

    public ResettingGeneratedWorldTask(WorldGenerationInfo pGenerationInfo, long pServerId, Component p_167661_, Runnable pTitle)
    {
        super(pServerId, p_167661_, pTitle);
        this.generationInfo = pGenerationInfo;
    }

    protected void sendResetRequest(RealmsClient pClient, long pServerId) throws RealmsServiceException
    {
        pClient.resetWorldWithSeed(pServerId, this.generationInfo);
    }
}
