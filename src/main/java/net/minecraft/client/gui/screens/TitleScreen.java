package net.minecraft.client.gui.screens;

import com.google.common.util.concurrent.Runnables;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TitleScreen extends Screen
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String DEMO_LEVEL_ID = "Demo_World";
    public static final String COPYRIGHT_TEXT = "Copyright Mojang AB. Do not distribute!";
    public static final CubeMap CUBE_MAP = new CubeMap(new ResourceLocation("textures/gui/title/background/panorama"));
    private static final ResourceLocation PANORAMA_OVERLAY = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");
    private static final ResourceLocation ACCESSIBILITY_TEXTURE = new ResourceLocation("textures/gui/accessibility.png");
    private final boolean minceraftEasterEgg;
    @Nullable
    private String splash;
    private Button resetDemoButton;
    private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation MINECRAFT_EDITION = new ResourceLocation("textures/gui/title/edition.png");
    private Screen realmsNotificationsScreen;
    private int copyrightWidth;
    private int copyrightX;
    private final PanoramaRenderer panorama = new PanoramaRenderer(CUBE_MAP);
    private final boolean fading;
    private long fadeInStart;
    private Screen modUpdateNotification;

    public TitleScreen()
    {
        this(false);
    }

    public TitleScreen(boolean pFading)
    {
        super(new TranslatableComponent("narrator.screen.title"));
        this.fading = pFading;
        this.minceraftEasterEgg = (double)(new Random()).nextFloat() < 1.0E-4D;
    }

    private boolean realmsNotificationsEnabled()
    {
        return this.minecraft.options.realmsNotifications && this.realmsNotificationsScreen != null;
    }

    public void tick()
    {
        if (this.realmsNotificationsEnabled())
        {
            this.realmsNotificationsScreen.tick();
        }
    }

    public static CompletableFuture<Void> preloadResources(TextureManager pTexMngr, Executor pBackgroundExecutor)
    {
        return CompletableFuture.allOf(pTexMngr.preload(MINECRAFT_LOGO, pBackgroundExecutor), pTexMngr.preload(MINECRAFT_EDITION, pBackgroundExecutor), pTexMngr.preload(PANORAMA_OVERLAY, pBackgroundExecutor), CUBE_MAP.preload(pTexMngr, pBackgroundExecutor));
    }

    public boolean isPauseScreen()
    {
        return false;
    }

    public boolean shouldCloseOnEsc()
    {
        return false;
    }

    protected void init()
    {
        if (this.splash == null)
        {
            this.splash = this.minecraft.getSplashManager().getSplash();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int i = calendar.get(5);
            int j = calendar.get(2) + 1;

            if (i == 8 && j == 4)
            {
                this.splash = "Happy birthday, OptiFine!";
            }

            if (i == 14 && j == 8)
            {
                this.splash = "Happy birthday, sp614x!";
            }
        }

        this.copyrightWidth = this.font.width("Copyright Mojang AB. Do not distribute!");
        this.copyrightX = this.width - this.copyrightWidth - 2;
        int k = 24;
        int l = this.height / 4 + 48;
        Button button = null;

        if (this.minecraft.isDemo())
        {
            this.createDemoMenuOptions(l, 24);
        }
        else
        {
            this.createNormalMenuOptions(l, 24);

            if (Reflector.ModListScreen_Constructor.exists())
            {
                button = ReflectorForge.makeButtonMods(this, l, 24);
                this.addRenderableWidget(button);
            }
        }

        this.addRenderableWidget(new ImageButton(this.width / 2 - 124, l + 72 + 12, 20, 20, 0, 106, 20, Button.WIDGETS_LOCATION, 256, 256, (p_96790_1_) ->
        {
            this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
        }, new TranslatableComponent("narrator.button.language")));
        this.addRenderableWidget(new Button(this.width / 2 - 100, l + 72 + 12, 98, 20, new TranslatableComponent("menu.options"), (p_96787_1_) ->
        {
            this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        }));
        this.addRenderableWidget(new Button(this.width / 2 + 2, l + 72 + 12, 98, 20, new TranslatableComponent("menu.quit"), (p_96785_1_) ->
        {
            this.minecraft.stop();
        }));
        this.addRenderableWidget(new ImageButton(this.width / 2 + 104, l + 72 + 12, 20, 20, 0, 0, 20, ACCESSIBILITY_TEXTURE, 32, 64, (p_96783_1_) ->
        {
            this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.minecraft.options));
        }, new TranslatableComponent("narrator.button.accessibility")));
        this.minecraft.setConnectedToRealms(false);

        if (this.minecraft.options.realmsNotifications && this.realmsNotificationsScreen == null)
        {
            this.realmsNotificationsScreen = new RealmsNotificationsScreen();
        }

        if (this.realmsNotificationsEnabled())
        {
            this.realmsNotificationsScreen.init(this.minecraft, this.width, this.height);
        }

        if (Reflector.NotificationModUpdateScreen_init.exists())
        {
            this.modUpdateNotification = (Screen)Reflector.call(Reflector.NotificationModUpdateScreen_init, this, button);
        }
    }

    private void createNormalMenuOptions(int pY, int pRowHeight)
    {
        this.addRenderableWidget(new Button(this.width / 2 - 100, pY, 200, 20, new TranslatableComponent("menu.singleplayer"), (p_96780_1_) ->
        {
            this.minecraft.setScreen(new SelectWorldScreen(this));
        }));
        boolean flag = this.minecraft.allowsMultiplayer();
        Button.OnTooltip button$ontooltip = flag ? Button.NO_TOOLTIP : new Button.OnTooltip()
        {
            private final Component text = new TranslatableComponent("title.multiplayer.disabled");
            public void onTooltip(Button p_169458_, PoseStack p_169459_, int p_169460_, int p_169461_)
            {
                if (!p_169458_.active)
                {
                    TitleScreen.this.renderTooltip(p_169459_, TitleScreen.this.minecraft.font.split(this.text, Math.max(TitleScreen.this.width / 2 - 43, 170)), p_169460_, p_169461_);
                }
            }
            public void narrateTooltip(Consumer<Component> p_169456_)
            {
                p_169456_.accept(this.text);
            }
        };
        (this.addRenderableWidget(new Button(this.width / 2 - 100, pY + pRowHeight * 1, 200, 20, new TranslatableComponent("menu.multiplayer"), (p_169449_1_) ->
        {
            Screen screen = (Screen)(this.minecraft.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(this) : new SafetyScreen(this));
            this.minecraft.setScreen(screen);
        }, button$ontooltip))).active = flag;
        boolean flag1 = Reflector.ModListScreen_Constructor.exists();
        int i = flag1 ? this.width / 2 + 2 : this.width / 2 - 100;
        int j = flag1 ? 98 : 200;
        (this.addRenderableWidget(new Button(i, pY + pRowHeight * 2, j, 20, new TranslatableComponent("menu.online"), (p_96775_1_) ->
        {
            this.realmsButtonClicked();
        }, button$ontooltip))).active = flag;
    }

    private void createDemoMenuOptions(int pY, int pRowHeight)
    {
        boolean flag = this.checkDemoWorldPresence();
        this.addRenderableWidget(new Button(this.width / 2 - 100, pY, 200, 20, new TranslatableComponent("menu.playdemo"), (p_169442_2_) ->
        {
            if (flag)
            {
                this.minecraft.loadLevel("Demo_World");
            }
            else {
                RegistryAccess.RegistryHolder registryaccess$registryholder = RegistryAccess.builtin();
                this.minecraft.createLevel("Demo_World", MinecraftServer.DEMO_SETTINGS, registryaccess$registryholder, WorldGenSettings.demoSettings(registryaccess$registryholder));
            }
        }));
        this.resetDemoButton = this.addRenderableWidget(new Button(this.width / 2 - 100, pY + pRowHeight * 1, 200, 20, new TranslatableComponent("menu.resetdemo"), (p_169440_1_) ->
        {
            LevelStorageSource levelstoragesource = this.minecraft.getLevelSource();

            try {
                LevelStorageSource.LevelStorageAccess levelstoragesource$levelstorageaccess = levelstoragesource.createAccess("Demo_World");

                try {
                    LevelSummary levelsummary = levelstoragesource$levelstorageaccess.getSummary();

                    if (levelsummary != null)
                    {
                        this.minecraft.setScreen(new ConfirmScreen(this::confirmDemo, new TranslatableComponent("selectWorld.deleteQuestion"), new TranslatableComponent("selectWorld.deleteWarning", levelsummary.getLevelName()), new TranslatableComponent("selectWorld.deleteButton"), CommonComponents.GUI_CANCEL));
                    }
                }
                catch (Throwable throwable11)
                {
                    if (levelstoragesource$levelstorageaccess != null)
                    {
                        try
                        {
                            levelstoragesource$levelstorageaccess.close();
                        }
                        catch (Throwable throwable)
                        {
                            throwable11.addSuppressed(throwable);
                        }
                    }

                    throw throwable11;
                }

                if (levelstoragesource$levelstorageaccess != null)
                {
                    levelstoragesource$levelstorageaccess.close();
                }
            }
            catch (IOException ioexception1)
            {
                SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
                LOGGER.warn("Failed to access demo world", (Throwable)ioexception1);
            }
        }));
        this.resetDemoButton.active = flag;
    }

    private boolean checkDemoWorldPresence()
    {
        try
        {
            LevelStorageSource.LevelStorageAccess levelstoragesource$levelstorageaccess = this.minecraft.getLevelSource().createAccess("Demo_World");
            boolean flag;

            try
            {
                flag = levelstoragesource$levelstorageaccess.getSummary() != null;
            }
            catch (Throwable throwable1)
            {
                if (levelstoragesource$levelstorageaccess != null)
                {
                    try
                    {
                        levelstoragesource$levelstorageaccess.close();
                    }
                    catch (Throwable throwable)
                    {
                        throwable1.addSuppressed(throwable);
                    }
                }

                throw throwable1;
            }

            if (levelstoragesource$levelstorageaccess != null)
            {
                levelstoragesource$levelstorageaccess.close();
            }

            return flag;
        }
        catch (IOException ioexception1)
        {
            SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
            LOGGER.warn("Failed to read demo world data", (Throwable)ioexception1);
            return false;
        }
    }

    private void realmsButtonClicked()
    {
        this.minecraft.setScreen(new RealmsMainScreen(this));
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        if (this.fadeInStart == 0L && this.fading)
        {
            this.fadeInStart = Util.getMillis();
        }

        float f = this.fading ? (float)(Util.getMillis() - this.fadeInStart) / 1000.0F : 1.0F;
        GlStateManager._disableDepthTest();
        this.panorama.render(pPartialTick, Mth.clamp(f, 0.0F, 1.0F));
        int i = 274;
        int j = this.width / 2 - 137;
        int k = 30;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, PANORAMA_OVERLAY);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.fading ? (float)Mth.ceil(Mth.clamp(f, 0.0F, 1.0F)) : 1.0F);
        blit(pPoseStack, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
        float f1 = this.fading ? Mth.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = Mth.ceil(f1 * 255.0F) << 24;

        if ((l & -67108864) != 0)
        {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, MINECRAFT_LOGO);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f1);

            if (this.minceraftEasterEgg)
            {
                this.blitOutlineBlack(j, 30, (p_169445_2_, p_169445_3_) ->
                {
                    this.blit(pPoseStack, p_169445_2_ + 0, p_169445_3_, 0, 0, 99, 44);
                    this.blit(pPoseStack, p_169445_2_ + 99, p_169445_3_, 129, 0, 27, 44);
                    this.blit(pPoseStack, p_169445_2_ + 99 + 26, p_169445_3_, 126, 0, 3, 44);
                    this.blit(pPoseStack, p_169445_2_ + 99 + 26 + 3, p_169445_3_, 99, 0, 26, 44);
                    this.blit(pPoseStack, p_169445_2_ + 155, p_169445_3_, 0, 45, 155, 44);
                });
            }
            else
            {
                this.blitOutlineBlack(j, 30, (p_96766_2_, p_96766_3_) ->
                {
                    this.blit(pPoseStack, p_96766_2_ + 0, p_96766_3_, 0, 0, 155, 44);
                    this.blit(pPoseStack, p_96766_2_ + 155, p_96766_3_, 0, 45, 155, 44);
                });
            }

            RenderSystem.setShaderTexture(0, MINECRAFT_EDITION);
            blit(pPoseStack, j + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);

            if (Reflector.ForgeHooksClient_renderMainMenu.exists())
            {
                Reflector.callVoid(Reflector.ForgeHooksClient_renderMainMenu, this, pPoseStack, this.font, this.width, this.height, l);
            }

            if (this.splash != null)
            {
                pPoseStack.pushPose();
                pPoseStack.translate((double)(this.width / 2 + 90), 70.0D, 0.0D);
                pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(-20.0F));
                float f2 = 1.8F - Mth.abs(Mth.sin((float)(Util.getMillis() % 1000L) / 1000.0F * ((float)Math.PI * 2F)) * 0.1F);
                f2 = f2 * 100.0F / (float)(this.font.width(this.splash) + 32);
                pPoseStack.scale(f2, f2, f2);
                drawCenteredString(pPoseStack, this.font, this.splash, 0, -8, 16776960 | l);
                pPoseStack.popPose();
            }

            String s = "Minecraft " + SharedConstants.getCurrentVersion().getName();

            if (this.minecraft.isDemo())
            {
                s = s + " Demo";
            }
            else
            {
                s = s + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : "/" + this.minecraft.getVersionType());
            }

            if (Minecraft.checkModStatus().shouldReportAsModified())
            {
                s = s + I18n.a("menu.modded");
            }

            if (Reflector.BrandingControl.exists())
            {
                if (Reflector.BrandingControl_forEachLine.exists())
                {
                    BiConsumer<Integer, String> biconsumer = (brdline, brd) ->
                    {
                        drawString(pPoseStack, this.font, brd, 2, this.height - (10 + brdline * (9 + 1)), 16777215 | l);
                    };
                    Reflector.call(Reflector.BrandingControl_forEachLine, true, true, biconsumer);
                }

                if (Reflector.BrandingControl_forEachAboveCopyrightLine.exists())
                {
                    BiConsumer<Integer, String> biconsumer1 = (brdline, brd) ->
                    {
                        drawString(pPoseStack, this.font, brd, this.width - this.font.width(brd), this.height - (10 + (brdline + 1) * (9 + 1)), 16777215 | l);
                    };
                    Reflector.call(Reflector.BrandingControl_forEachAboveCopyrightLine, biconsumer1);
                }
            }
            else
            {
                drawString(pPoseStack, this.font, s, 2, this.height - 10, 16777215 | l);
            }

            drawString(pPoseStack, this.font, "Copyright Mojang AB. Do not distribute!", this.copyrightX, this.height - 10, 16777215 | l);

            if (pMouseX > this.copyrightX && pMouseX < this.copyrightX + this.copyrightWidth && pMouseY > this.height - 10 && pMouseY < this.height)
            {
                fill(pPoseStack, this.copyrightX, this.height - 1, this.copyrightX + this.copyrightWidth, this.height, 16777215 | l);
            }

            for (GuiEventListener guieventlistener : this.children())
            {
                if (guieventlistener instanceof AbstractWidget)
                {
                    ((AbstractWidget)guieventlistener).setAlpha(f1);
                }
            }

            super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

            if (this.realmsNotificationsEnabled() && f1 >= 1.0F)
            {
                this.realmsNotificationsScreen.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            }
        }

        if (this.modUpdateNotification != null)
        {
            this.modUpdateNotification.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }
    }

    public boolean mouseClicked(double pMouseX, double p_96736_, int pMouseY)
    {
        if (super.mouseClicked(pMouseX, p_96736_, pMouseY))
        {
            return true;
        }
        else if (this.realmsNotificationsEnabled() && this.realmsNotificationsScreen.mouseClicked(pMouseX, p_96736_, pMouseY))
        {
            return true;
        }
        else
        {
            if (pMouseX > (double)this.copyrightX && pMouseX < (double)(this.copyrightX + this.copyrightWidth) && p_96736_ > (double)(this.height - 10) && p_96736_ < (double)this.height)
            {
                this.minecraft.setScreen(new WinScreen(false, Runnables.doNothing()));
            }

            return false;
        }
    }

    public void removed()
    {
        if (this.realmsNotificationsScreen != null)
        {
            this.realmsNotificationsScreen.removed();
        }
    }

    private void confirmDemo(boolean p_96778_)
    {
        if (p_96778_)
        {
            try
            {
                LevelStorageSource.LevelStorageAccess levelstoragesource$levelstorageaccess = this.minecraft.getLevelSource().createAccess("Demo_World");

                try
                {
                    levelstoragesource$levelstorageaccess.deleteLevel();
                }
                catch (Throwable throwable1)
                {
                    if (levelstoragesource$levelstorageaccess != null)
                    {
                        try
                        {
                            levelstoragesource$levelstorageaccess.close();
                        }
                        catch (Throwable throwable)
                        {
                            throwable1.addSuppressed(throwable);
                        }
                    }

                    throw throwable1;
                }

                if (levelstoragesource$levelstorageaccess != null)
                {
                    levelstoragesource$levelstorageaccess.close();
                }
            }
            catch (IOException ioexception1)
            {
                SystemToast.onWorldDeleteFailure(this.minecraft, "Demo_World");
                LOGGER.warn("Failed to delete demo world", (Throwable)ioexception1);
            }
        }

        this.minecraft.setScreen(this);
    }
}
