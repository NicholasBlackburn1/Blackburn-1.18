package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RetryCallException;
import net.minecraft.network.chat.TranslatableComponent;

public class SwitchSlotTask extends LongRunningTask
{
    private final long worldId;
    private final int slot;
    private final Runnable callback;

    public SwitchSlotTask(long pWorldId, int p_90460_, Runnable pSlot)
    {
        this.worldId = pWorldId;
        this.slot = p_90460_;
        this.callback = pSlot;
    }

    public void run()
    {
        RealmsClient realmsclient = RealmsClient.create();
        this.setTitle(new TranslatableComponent("mco.minigame.world.slot.screen.title"));

        for (int i = 0; i < 25; ++i)
        {
            try
            {
                if (this.aborted())
                {
                    return;
                }

                if (realmsclient.switchSlot(this.worldId, this.slot))
                {
                    this.callback.run();
                    break;
                }
            }
            catch (RetryCallException retrycallexception)
            {
                if (this.aborted())
                {
                    return;
                }

                pause((long)retrycallexception.delaySeconds);
            }
            catch (Exception exception)
            {
                if (this.aborted())
                {
                    return;
                }

                LOGGER.error("Couldn't switch world!");
                this.error(exception.toString());
            }
        }
    }
}
