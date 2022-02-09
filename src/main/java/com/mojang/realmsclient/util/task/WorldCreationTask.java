package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class WorldCreationTask extends LongRunningTask
{
    private final String name;
    private final String motd;
    private final long worldId;
    private final Screen lastScreen;

    public WorldCreationTask(long pWorldId, String p_90469_, String pName, Screen pMotd)
    {
        this.worldId = pWorldId;
        this.name = p_90469_;
        this.motd = pName;
        this.lastScreen = pMotd;
    }

    public void run()
    {
        this.setTitle(new TranslatableComponent("mco.create.world.wait"));
        RealmsClient realmsclient = RealmsClient.create();

        try
        {
            realmsclient.initializeWorld(this.worldId, this.name, this.motd);
            setScreen(this.lastScreen);
        }
        catch (RealmsServiceException realmsserviceexception)
        {
            LOGGER.error("Couldn't create world");
            this.error(realmsserviceexception.toString());
        }
        catch (Exception exception)
        {
            LOGGER.error("Could not create world");
            this.error(exception.getLocalizedMessage());
        }
    }
}
