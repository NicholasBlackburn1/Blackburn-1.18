package com.mojang.realmsclient.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public interface ErrorCallback
{
    void error(Component pError);

default void error(String pError)
    {
        this.error(new TextComponent(pError));
    }
}
