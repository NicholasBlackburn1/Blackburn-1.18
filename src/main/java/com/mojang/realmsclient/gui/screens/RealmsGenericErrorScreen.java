package com.mojang.realmsclient.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.realms.RealmsScreen;

public class RealmsGenericErrorScreen extends RealmsScreen
{
    private final Screen nextScreen;
    private final Pair<Component, Component> lines;
    private MultiLineLabel line2Split = MultiLineLabel.EMPTY;

    public RealmsGenericErrorScreen(RealmsServiceException pServiceException, Screen pNextScreen)
    {
        super(NarratorChatListener.NO_TITLE);
        this.nextScreen = pNextScreen;
        this.lines = errorMessage(pServiceException);
    }

    public RealmsGenericErrorScreen(Component pServiceException, Screen pNextScreen)
    {
        super(NarratorChatListener.NO_TITLE);
        this.nextScreen = pNextScreen;
        this.lines = errorMessage(pServiceException);
    }

    public RealmsGenericErrorScreen(Component pLine1, Component pLine2, Screen pNextScreen)
    {
        super(NarratorChatListener.NO_TITLE);
        this.nextScreen = pNextScreen;
        this.lines = errorMessage(pLine1, pLine2);
    }

    private static Pair<Component, Component> errorMessage(RealmsServiceException p_200950_)
    {
        if (p_200950_.realmsError == null)
        {
            return Pair.of(new TextComponent("An error occurred (" + p_200950_.httpResultCode + "):"), new TextComponent(p_200950_.rawResponse));
        }
        else
        {
            String s = "mco.errorMessage." + p_200950_.realmsError.getErrorCode();
            return Pair.of(new TextComponent("Realms (" + p_200950_.realmsError + "):"), (Component)(I18n.exists(s) ? new TranslatableComponent(s) : Component.nullToEmpty(p_200950_.realmsError.getErrorMessage())));
        }
    }

    private static Pair<Component, Component> errorMessage(Component p_200952_)
    {
        return Pair.of(new TextComponent("An error occurred: "), p_200952_);
    }

    private static Pair<Component, Component> errorMessage(Component p_200954_, Component p_200955_)
    {
        return Pair.of(p_200954_, p_200955_);
    }

    public void init()
    {
        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height - 52, 200, 20, new TextComponent("Ok"), (p_88686_) ->
        {
            this.minecraft.setScreen(this.nextScreen);
        }));
        this.line2Split = MultiLineLabel.create(this.font, this.lines.getSecond(), this.width * 3 / 4);
    }

    public Component getNarrationMessage()
    {
        return (new TextComponent("")).append(this.lines.getFirst()).append(": ").append(this.lines.getSecond());
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        this.renderBackground(pPoseStack);
        drawCenteredString(pPoseStack, this.font, this.lines.getFirst(), this.width / 2, 80, 16777215);
        this.line2Split.renderCentered(pPoseStack, this.width / 2, 100, 9, 16711680);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}
