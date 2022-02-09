package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import net.minecraft.network.chat.Component;

public abstract class ResettingWorldTask extends LongRunningTask
{
    private final long serverId;
    private final Component title;
    private final Runnable callback;

    public ResettingWorldTask(long pServerId, Component p_167677_, Runnable pTitle)
    {
        this.serverId = pServerId;
        this.title = p_167677_;
        this.callback = pTitle;
    }

    protected abstract void sendResetRequest(RealmsClient pClient, long pServerId) throws RealmsServiceException;

    public void run()
    {
        RealmsClient realmsclient = RealmsClient.create();
        this.setTitle(this.title);
        int i = 0;

        while (i < 25)
        {
            try
            {
                if (this.aborted())
                {
                    return;
                }

                this.sendResetRequest(realmsclient, this.serverId);

                if (this.aborted())
                {
                    return;
                }

                this.callback.run();
                return;
            }
            catch (RetryCallException retrycallexception)
            {
                if (this.aborted())
                {
                    return;
                }

                pause((long)retrycallexception.delaySeconds);
                ++i;
            }
            catch (Exception exception)
            {
                if (this.aborted())
                {
                    return;
                }

                LOGGER.error("Couldn't reset world");
                this.error(exception.toString());
                return;
            }
        }
    }
}
