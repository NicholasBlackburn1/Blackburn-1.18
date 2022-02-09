package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import net.minecraft.network.chat.TranslatableComponent;

public class CloseServerTask extends LongRunningTask
{
    private final RealmsServer serverData;
    private final RealmsConfigureWorldScreen configureScreen;

    public CloseServerTask(RealmsServer pServerData, RealmsConfigureWorldScreen pConfigureScreen)
    {
        this.serverData = pServerData;
        this.configureScreen = pConfigureScreen;
    }

    public void run()
    {
        this.setTitle(new TranslatableComponent("mco.configure.world.closing"));
        RealmsClient realmsclient = RealmsClient.create();

        for (int i = 0; i < 25; ++i)
        {
            if (this.aborted())
            {
                return;
            }

            try
            {
                boolean flag = realmsclient.close(this.serverData.id);

                if (flag)
                {
                    this.configureScreen.stateChanged();
                    this.serverData.state = RealmsServer.State.CLOSED;
                    setScreen(this.configureScreen);
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

                LOGGER.error("Failed to close server", (Throwable)exception);
                this.error("Failed to close the server");
            }
        }
    }
}
