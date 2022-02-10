package net.minecraft.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.Window;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.renderer.GpuWarnlistManager;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.optifine.Config;
import net.optifine.config.IteratableOptionOF;
import net.optifine.config.SliderPercentageOptionOF;

public abstract class Option
{
    public static final Map<String, Option> OPTIONS_BY_KEY = new HashMap<>();
    protected static final int OPTIONS_TOOLTIP_WIDTH = 200;



    
    public static final ProgressOption BIOME_BLEND_RADIUS = new ProgressOption("options.biomeBlendRadius", 0.0D, 7.0D, 1.0F, (p_91712_0_) ->
    {
        return (double)p_91712_0_.biomeBlendRadius;
    }, (p_92008_0_, p_92008_1_) ->
    {
        p_92008_0_.biomeBlendRadius = Mth.clamp((int)p_92008_1_.doubleValue(), 0, 7);
        Minecraft.getInstance().levelRenderer.allChanged();
    }, (p_92002_0_, p_92002_1_) ->
    {
        double d0 = p_92002_1_.get(p_92002_0_);
        int i = (int)d0 * 2 + 1;
        return p_92002_1_.genericValueLabel(new TranslatableComponent("options.biomeBlendRadius." + i));
    });
    public static final ProgressOption CHAT_HEIGHT_FOCUSED = new ProgressOption("options.chat.height.focused", 0.0D, 1.0D, 0.0F, (p_91710_0_) ->
    {
        return p_91710_0_.chatHeightFocused;
    }, (p_91997_0_, p_91997_1_) ->
    {
        p_91997_0_.chatHeightFocused = p_91997_1_;
        Minecraft.getInstance().gui.getChat().rescaleChat();
    }, (p_91991_0_, p_91991_1_) ->
    {
        double d0 = p_91991_1_.toPct(p_91991_1_.get(p_91991_0_));
        return p_91991_1_.pixelValueLabel(ChatComponent.getHeight(d0));
    });
    public static final ProgressOption CHAT_HEIGHT_UNFOCUSED = new ProgressOption("options.chat.height.unfocused", 0.0D, 1.0D, 0.0F, (p_91708_0_) ->
    {
        return p_91708_0_.chatHeightUnfocused;
    }, (p_91986_0_, p_91986_1_) ->
    {
        p_91986_0_.chatHeightUnfocused = p_91986_1_;
        Minecraft.getInstance().gui.getChat().rescaleChat();
    }, (p_91980_0_, p_91980_1_) ->
    {
        double d0 = p_91980_1_.toPct(p_91980_1_.get(p_91980_0_));
        return p_91980_1_.pixelValueLabel(ChatComponent.getHeight(d0));
    });
    public static final ProgressOption CHAT_OPACITY = new ProgressOption("options.chat.opacity", 0.0D, 1.0D, 0.0F, (p_91706_0_) ->
    {
        return p_91706_0_.chatOpacity;
    }, (p_91975_0_, p_91975_1_) ->
    {
        p_91975_0_.chatOpacity = p_91975_1_;
        Minecraft.getInstance().gui.getChat().rescaleChat();
    }, (p_91969_0_, p_91969_1_) ->
    {
        double d0 = p_91969_1_.toPct(p_91969_1_.get(p_91969_0_));
        return p_91969_1_.percentValueLabel(d0 * 0.9D + 0.1D);
    });
    public static final ProgressOption CHAT_SCALE = new ProgressOption("options.chat.scale", 0.0D, 1.0D, 0.0F, (p_91704_0_) ->
    {
        return p_91704_0_.chatScale;
    }, (p_91964_0_, p_91964_1_) ->
    {
        p_91964_0_.chatScale = p_91964_1_;
        Minecraft.getInstance().gui.getChat().rescaleChat();
    }, (p_91958_0_, p_91958_1_) ->
    {
        double d0 = p_91958_1_.toPct(p_91958_1_.get(p_91958_0_));
        return (Component)(d0 == 0.0D ? CommonComponents.optionStatus(p_91958_1_.getCaption(), false) : p_91958_1_.percentValueLabel(d0));
    });
    public static final ProgressOption CHAT_WIDTH = new ProgressOption("options.chat.width", 0.0D, 1.0D, 0.0F, (p_91702_0_) ->
    {
        return p_91702_0_.chatWidth / 4.0571431D;
    }, (p_91953_0_, p_91953_1_) ->
    {
        p_91953_1_ = p_91953_1_ * 4.0571431D;
        p_91953_0_.chatWidth = p_91953_1_;
        Minecraft.getInstance().gui.getChat().rescaleChat();
    }, (p_91947_0_, p_91947_1_) ->
    {
        double d0 = p_91947_1_.toPct(p_91947_1_.get(p_91947_0_));
        return p_91947_1_.pixelValueLabel(ChatComponent.getWidth(d0 * 4.0571431D));
    });
    public static final ProgressOption CHAT_LINE_SPACING = new ProgressOption("options.chat.line_spacing", 0.0D, 1.0D, 0.0F, (p_91700_0_) ->
    {
        return p_91700_0_.chatLineSpacing;
    }, (p_91942_0_, p_91942_1_) ->
    {
        p_91942_0_.chatLineSpacing = p_91942_1_;
    }, (p_91936_0_, p_91936_1_) ->
    {
        return p_91936_1_.percentValueLabel(p_91936_1_.toPct(p_91936_1_.get(p_91936_0_)));
    });
    public static final ProgressOption CHAT_DELAY = new ProgressOption("options.chat.delay_instant", 0.0D, 6.0D, 0.1F, (p_91698_0_) ->
    {
        return p_91698_0_.chatDelay;
    }, (p_91928_0_, p_91928_1_) ->
    {
        p_91928_0_.chatDelay = p_91928_1_;
    }, (p_91922_0_, p_91922_1_) ->
    {
        double d0 = p_91922_1_.get(p_91922_0_);
        return d0 <= 0.0D ? new TranslatableComponent("options.chat.delay_none") : new TranslatableComponent("options.chat.delay", String.format("%.1f", d0));
    });
    public static final ProgressOption FOV = new ProgressOption("options.fov", 30.0D, 110.0D, 1.0F, (p_91696_0_) ->
    {
        return p_91696_0_.fov;
    }, (p_91911_0_, p_91911_1_) ->
    {
        p_91911_0_.fov = p_91911_1_;
        Minecraft.getInstance().levelRenderer.needsUpdate();
    }, (p_91905_0_, p_91905_1_) ->
    {
        double d0 = p_91905_1_.get(p_91905_0_);

        if (d0 == 70.0D)
        {
            return p_91905_1_.genericValueLabel(new TranslatableComponent("options.fov.min"));
        }
        else {
            return d0 == p_91905_1_.getMaxValue() ? p_91905_1_.genericValueLabel(new TranslatableComponent("options.fov.max")) : p_91905_1_.genericValueLabel((int)d0);
        }
    });
    private static final Component ACCESSIBILITY_TOOLTIP_FOV_EFFECT = new TranslatableComponent("options.fovEffectScale.tooltip");
    public static final ProgressOption FOV_EFFECTS_SCALE = new ProgressOption("options.fovEffectScale", 0.0D, 1.0D, 0.0F, (p_91694_0_) ->
    {
        return Math.pow((double)p_91694_0_.fovEffectScale, 2.0D);
    }, (p_91894_0_, p_91894_1_) ->
    {
        p_91894_0_.fovEffectScale = (float)Math.sqrt(p_91894_1_);
    }, (p_91888_0_, p_91888_1_) ->
    {
        double d0 = p_91888_1_.toPct(p_91888_1_.get(p_91888_0_));
        return d0 == 0.0D ? p_91888_1_.genericValueLabel(CommonComponents.OPTION_OFF) : p_91888_1_.percentValueLabel(d0);
    }, (p_168229_0_) ->
    {
        return p_168229_0_.font.split(ACCESSIBILITY_TOOLTIP_FOV_EFFECT, 200);
    });
    private static final Component ACCESSIBILITY_TOOLTIP_SCREEN_EFFECT = new TranslatableComponent("options.screenEffectScale.tooltip");
    public static final ProgressOption SCREEN_EFFECTS_SCALE = new ProgressOption("options.screenEffectScale", 0.0D, 1.0D, 0.0F, (p_168138_0_) ->
    {
        return (double)p_168138_0_.screenEffectScale;
    }, (p_168312_0_, p_168312_1_) ->
    {
        p_168312_0_.screenEffectScale = p_168312_1_.floatValue();
    }, (p_168309_0_, p_168309_1_) ->
    {
        double d0 = p_168309_1_.toPct(p_168309_1_.get(p_168309_0_));
        return d0 == 0.0D ? p_168309_1_.genericValueLabel(CommonComponents.OPTION_OFF) : p_168309_1_.percentValueLabel(d0);
    }, (p_168214_0_) ->
    {
        return p_168214_0_.font.split(ACCESSIBILITY_TOOLTIP_SCREEN_EFFECT, 200);
    });
    public static final ProgressOption FRAMERATE_LIMIT = new ProgressOption("options.framerateLimit", 0.0D, 260.0D, 5.0F, (p_168136_0_) ->
    {
        return p_168136_0_.enableVsync ? 0 : (double)p_168136_0_.framerateLimit;
    }, (p_168300_0_, p_168300_1_) ->
    {
        p_168300_0_.framerateLimit = (int)p_168300_1_.doubleValue();
        p_168300_0_.enableVsync = false;

        if (p_168300_0_.framerateLimit <= 0)
        {
            p_168300_0_.framerateLimit = 260;
            p_168300_0_.enableVsync = true;
        }

        p_168300_0_.updateVSync();
        Minecraft.getInstance().getWindow().setFramerateLimit(p_168300_0_.framerateLimit);
    }, (p_168297_0_, p_168297_1_) ->
    {
        if (p_168297_0_.enableVsync)
        {
            return p_168297_1_.genericValueLabel(new TranslatableComponent("of.options.framerateLimit.vsync"));
        }
        else {
            double d0 = p_168297_1_.get(p_168297_0_);
            return d0 == p_168297_1_.getMaxValue() ? p_168297_1_.genericValueLabel(new TranslatableComponent("options.framerateLimit.max")) : p_168297_1_.genericValueLabel(new TranslatableComponent("options.framerate", (int)d0));
        }
    });
    public static final ProgressOption GAMMA = new ProgressOption("options.gamma", 0.0D, 1.0D, 0.0F, (p_168134_0_) ->
    {
        return p_168134_0_.gamma;
    }, (p_168288_0_, p_168288_1_) ->
    {
        p_168288_0_.gamma = p_168288_1_;
    }, (p_168285_0_, p_168285_1_) ->
    {
        double d0 = p_168285_1_.toPct(p_168285_1_.get(p_168285_0_));
        int i = (int)(d0 * 100.0D);

        if (i == 0)
        {
            return p_168285_1_.genericValueLabel(new TranslatableComponent("options.gamma.min"));
        }
        else if (i == 50)
        {
            return p_168285_1_.genericValueLabel(new TranslatableComponent("options.gamma.default"));
        }
        else {
            return i == 100 ? p_168285_1_.genericValueLabel(new TranslatableComponent("options.gamma.max")) : p_168285_1_.genericValueLabel(i);
        }
    });
    public static final ProgressOption MIPMAP_LEVELS = new ProgressOption("options.mipmapLevels", 0.0D, 4.0D, 1.0F, (p_168132_0_) ->
    {
        return (double)p_168132_0_.mipmapLevels;
    }, (p_168276_0_, p_168276_1_) ->
    {
        p_168276_0_.mipmapLevels = (int)p_168276_1_.doubleValue();
        p_168276_0_.updateMipmaps();
    }, (p_168273_0_, p_168273_1_) ->
    {
        double d0 = p_168273_1_.get(p_168273_0_);

        if (d0 >= 4.0D)
        {
            return p_168273_1_.genericValueLabel(new TranslatableComponent("of.general.max"));
        }
        else {
            return (Component)(d0 == 0.0D ? CommonComponents.optionStatus(p_168273_1_.getCaption(), false) : p_168273_1_.genericValueLabel((int)d0));
        }
    });
    public static final ProgressOption MOUSE_WHEEL_SENSITIVITY = new LogaritmicProgressOption("options.mouseWheelSensitivity", 0.01D, 10.0D, 0.01F, (p_168130_0_) ->
    {
        return p_168130_0_.mouseWheelSensitivity;
    }, (p_168264_0_, p_168264_1_) ->
    {
        p_168264_0_.mouseWheelSensitivity = p_168264_1_;
    }, (p_168261_0_, p_168261_1_) ->
    {
        double d0 = p_168261_1_.toPct(p_168261_1_.get(p_168261_0_));
        return p_168261_1_.genericValueLabel(new TextComponent(String.format("%.2f", p_168261_1_.toValue(d0))));
    });
    public static final CycleOption<Boolean> RAW_MOUSE_INPUT = CycleOption.createOnOff("options.rawMouseInput", (p_168128_0_) ->
    {
        return p_168128_0_.rawMouseInput;
    }, (p_168395_0_, p_168395_1_, p_168395_2_) ->
    {
        p_168395_0_.rawMouseInput = p_168395_2_;
        Window window = Minecraft.getInstance().getWindow();

        if (window != null)
        {
            window.updateRawMouseInput(p_168395_2_);
        }
    });
    public static final ProgressOption RENDER_DISTANCE = new ProgressOption("options.renderDistance", 2.0D, 16.0D, 1.0F, (p_168126_0_) ->
    {
        return (double)p_168126_0_.renderDistance;
    }, (p_168252_0_, p_168252_1_) ->
    {
        p_168252_0_.renderDistance = p_168252_1_.intValue();
        Minecraft.getInstance().levelRenderer.needsUpdate();
    }, (p_168249_0_, p_168249_1_) ->
    {
        double d0 = p_168249_1_.get(p_168249_0_);
        return p_168249_1_.genericValueLabel(new TranslatableComponent("options.chunks", (int)d0));
    });
    public static final ProgressOption SIMULATION_DISTANCE = new ProgressOption("options.simulationDistance", 5.0D, 16.0D, 1.0F, (p_168124_0_) ->
    {
        return (double)p_168124_0_.simulationDistance;
    }, (p_168240_0_, p_168240_1_) ->
    {
        p_168240_0_.simulationDistance = p_168240_1_.intValue();
    }, (p_168237_0_, p_168237_1_) ->
    {
        double d0 = p_168237_1_.get(p_168237_0_);
        return p_168237_1_.genericValueLabel(new TranslatableComponent("options.chunks", (int)d0));
    });
    public static final ProgressOption ENTITY_DISTANCE_SCALING = new ProgressOption("options.entityDistanceScaling", 0.5D, 5.0D, 0.25F, (p_168122_0_) ->
    {
        return (double)p_168122_0_.entityDistanceScaling;
    }, (p_168225_0_, p_168225_1_) ->
    {
        p_168225_0_.entityDistanceScaling = (float)p_168225_1_.doubleValue();
    }, (p_168222_0_, p_168222_1_) ->
    {
        double d0 = p_168222_1_.get(p_168222_0_);
        return p_168222_1_.percentValueLabel(d0);
    });
    public static final ProgressOption SENSITIVITY = new ProgressOption("options.sensitivity", 0.0D, 1.0D, 0.0F, (p_168120_0_) ->
    {
        return p_168120_0_.sensitivity;
    }, (p_168199_0_, p_168199_1_) ->
    {
        p_168199_0_.sensitivity = p_168199_1_;
    }, (p_168196_0_, p_168196_1_) ->
    {
        double d0 = p_168196_1_.toPct(p_168196_1_.get(p_168196_0_));

        if (d0 == 0.0D)
        {
            return p_168196_1_.genericValueLabel(new TranslatableComponent("options.sensitivity.min"));
        }
        else {
            return d0 == 1.0D ? p_168196_1_.genericValueLabel(new TranslatableComponent("options.sensitivity.max")) : p_168196_1_.percentValueLabel(2.0D * d0);
        }
    });
    public static final ProgressOption TEXT_BACKGROUND_OPACITY = new ProgressOption("options.accessibility.text_background_opacity", 0.0D, 1.0D, 0.0F, (p_193620_0_) ->
    {
        return p_193620_0_.textBackgroundOpacity;
    }, (p_193692_0_, p_193692_1_) ->
    {
        p_193692_0_.textBackgroundOpacity = p_193692_1_;
        Minecraft.getInstance().gui.getChat().rescaleChat();
    }, (p_193689_0_, p_193689_1_) ->
    {
        return p_193689_1_.percentValueLabel(p_193689_1_.toPct(p_193689_1_.get(p_193689_0_)));
    });
    public static final CycleOption<AmbientOcclusionStatus> AMBIENT_OCCLUSION = CycleOption.a("options.ao", AmbientOcclusionStatus.values(), (p_193624_0_) ->
    {
        return new TranslatableComponent(p_193624_0_.getKey());
    }, (p_193618_0_) ->
    {
        return p_193618_0_.ambientOcclusion;
    }, (p_193649_0_, p_193649_1_, p_193649_2_) ->
    {
        p_193649_0_.ambientOcclusion = p_193649_2_;
        Minecraft.getInstance().levelRenderer.allChanged();
    });
    private static final Component PRIORITIZE_CHUNK_TOOLTIP_NONE = new TranslatableComponent("options.prioritizeChunkUpdates.none.tooltip");
    private static final Component PRIORITIZE_CHUNK_TOOLTIP_PLAYER_AFFECTED = new TranslatableComponent("options.prioritizeChunkUpdates.byPlayer.tooltip");
    private static final Component PRIORITIZE_CHUNK_TOOLTIP_NEARBY = new TranslatableComponent("options.prioritizeChunkUpdates.nearby.tooltip");
    public static final CycleOption<PrioritizeChunkUpdates> PRIORITIZE_CHUNK_UPDATES = CycleOption.a("options.prioritizeChunkUpdates", PrioritizeChunkUpdates.values(), (p_193697_0_) ->
    {
        return new TranslatableComponent(p_193697_0_.getKey());
    }, (p_193616_0_) ->
    {
        return p_193616_0_.prioritizeChunkUpdates;
    }, (p_193673_0_, p_193673_1_, p_193673_2_) ->
    {
        p_193673_0_.prioritizeChunkUpdates = p_193673_2_;
    }).setTooltip((p_193711_0_) ->
    {
        return (p_193634_1_) -> {
            Object object;

            switch (p_193634_1_)
            {
                case NONE:
                    object = p_193711_0_.font.split(PRIORITIZE_CHUNK_TOOLTIP_NONE, 200);
                    break;

                case PLAYER_AFFECTED:
                    object = p_193711_0_.font.split(PRIORITIZE_CHUNK_TOOLTIP_PLAYER_AFFECTED, 200);
                    break;

                case NEARBY:
                    object = p_193711_0_.font.split(PRIORITIZE_CHUNK_TOOLTIP_NEARBY, 200);
                    break;

                default:
                    object = ImmutableList.of();
            }

            return (List)object;
        };
    });
    public static final CycleOption<AttackIndicatorStatus> ATTACK_INDICATOR = CycleOption.a("options.attackIndicator", AttackIndicatorStatus.values(), (p_193626_0_) ->
    {
        return new TranslatableComponent(p_193626_0_.getKey());
    }, (p_193614_0_) ->
    {
        return p_193614_0_.attackIndicator;
    }, (p_193653_0_, p_193653_1_, p_193653_2_) ->
    {
        p_193653_0_.attackIndicator = p_193653_2_;
    });
    public static final CycleOption<ChatVisiblity> CHAT_VISIBILITY = CycleOption.a("options.chat.visibility", ChatVisiblity.values(), (p_193622_0_) ->
    {
        return new TranslatableComponent(p_193622_0_.getKey());
    }, (p_193612_0_) ->
    {
        return p_193612_0_.chatVisibility;
    }, (p_193645_0_, p_193645_1_, p_193645_2_) ->
    {
        p_193645_0_.chatVisibility = p_193645_2_;
    });
    private static final Component GRAPHICS_TOOLTIP_FAST = new TranslatableComponent("options.graphics.fast.tooltip");
    private static final Component GRAPHICS_TOOLTIP_FABULOUS = new TranslatableComponent("options.graphics.fabulous.tooltip", (new TranslatableComponent("options.graphics.fabulous")).withStyle(ChatFormatting.ITALIC));
    private static final Component GRAPHICS_TOOLTIP_FANCY = new TranslatableComponent("options.graphics.fancy.tooltip");
    public static final CycleOption<GraphicsStatus> GRAPHICS = CycleOption.create("options.graphics", Arrays.asList(GraphicsStatus.values()), Stream.of(GraphicsStatus.values()).filter((p_193709_0_) ->
    {
        return p_193709_0_ != GraphicsStatus.FABULOUS;
    }).collect(Collectors.toList()), () ->
    {
        return Config.isShaders() || !GLX.isUsingFBOs() || Minecraft.getInstance().getGpuWarnlistManager().isSkippingFabulous();
    }, (p_193630_0_) ->
    {
        MutableComponent mutablecomponent = new TranslatableComponent(p_193630_0_.getKey());
        return p_193630_0_ == GraphicsStatus.FABULOUS ? mutablecomponent.withStyle(ChatFormatting.ITALIC) : mutablecomponent;
    }, (p_193610_0_) ->
    {
        return p_193610_0_.graphicsMode;
    }, (p_193661_0_, p_193661_1_, p_193661_2_) ->
    {
        Minecraft minecraft = Minecraft.getInstance();
        GpuWarnlistManager gpuwarnlistmanager = minecraft.getGpuWarnlistManager();

        if (p_193661_2_ == GraphicsStatus.FABULOUS && gpuwarnlistmanager.willShowWarning())
        {
            gpuwarnlistmanager.showWarning();
        }
        else {
            p_193661_0_.graphicsMode = p_193661_2_;
            p_193661_0_.updateRenderClouds();
            minecraft.levelRenderer.allChanged();
        }
    }).setTooltip((p_193632_0_) ->
    {
        List<FormattedCharSequence> list = p_193632_0_.font.split(GRAPHICS_TOOLTIP_FAST, 200);
        List<FormattedCharSequence> list1 = p_193632_0_.font.split(GRAPHICS_TOOLTIP_FANCY, 200);
        List<FormattedCharSequence> list2 = p_193632_0_.font.split(GRAPHICS_TOOLTIP_FABULOUS, 200);
        return (p_193703_3_) -> {
            switch (p_193703_3_)
            {
                case FANCY:
                    return list1;

                case FAST:
                    return list;

                case FABULOUS:
                    return list2;

                default:
                    return ImmutableList.of();
            }
        };
    });
    public static final CycleOption GUI_SCALE = CycleOption.create("options.guiScale", () ->
    {
        return IntStream.rangeClosed(0, Minecraft.getInstance().getWindow().calculateScale(0, Minecraft.getInstance().isEnforceUnicode())).boxed().collect(Collectors.toList());
    }, (p_193699_0_) ->
    {
        return (Component)(p_193699_0_ == 0 ? new TranslatableComponent("options.guiScale.auto") : new TextComponent(Integer.toString(p_193699_0_)));
    }, (p_193608_0_) ->
    {
        return p_193608_0_.guiScale;
    }, (p_193681_0_, p_193681_1_, p_193681_2_) ->
    {
        p_193681_0_.guiScale = p_193681_2_;
    });
    public static final CycleOption<String> AUDIO_DEVICE = CycleOption.create("options.audioDevice", () ->
    {
        return Stream.concat(Stream.of(""), Minecraft.getInstance().getSoundManager().getAvailableSoundDevices().stream()).toList();
    }, (p_193701_0_) ->
    {
        if ("".equals(p_193701_0_))
        {
            return new TranslatableComponent("options.audioDevice.default");
        }
        else {
            return p_193701_0_.startsWith("OpenAL Soft on ") ? new TextComponent(p_193701_0_.substring(SoundEngine.OPEN_AL_SOFT_PREFIX_LENGTH)) : new TextComponent(p_193701_0_);
        }
    }, (p_193606_0_) ->
    {
        return p_193606_0_.soundDevice;
    }, (p_193685_0_, p_193685_1_, p_193685_2_) ->
    {
        p_193685_0_.soundDevice = p_193685_2_;
        SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
        soundmanager.reload();
        soundmanager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    });
    public static final CycleOption<HumanoidArm> MAIN_HAND = CycleOption.a("options.mainHand", HumanoidArm.values(), HumanoidArm::getName, (p_193604_0_) ->
    {
        return p_193604_0_.mainHand;
    }, (p_193641_0_, p_193641_1_, p_193641_2_) ->
    {
        p_193641_0_.mainHand = p_193641_2_;
        p_193641_0_.broadcastOptions();
    });
    public static final CycleOption<NarratorStatus> NARRATOR = CycleOption.a("options.narrator", NarratorStatus.values(), (p_193637_0_) ->
    {
        return (Component)(NarratorChatListener.INSTANCE.isActive() ? p_193637_0_.getName() : new TranslatableComponent("options.narrator.notavailable"));
    }, (p_193602_0_) ->
    {
        return p_193602_0_.narratorStatus;
    }, (p_193665_0_, p_193665_1_, p_193665_2_) ->
    {
        p_193665_0_.narratorStatus = p_193665_2_;
        NarratorChatListener.INSTANCE.updateNarratorStatus(p_193665_2_);
    });
    public static final CycleOption<ParticleStatus> PARTICLES = CycleOption.a("options.particles", ParticleStatus.values(), (p_193695_0_) ->
    {
        return new TranslatableComponent(p_193695_0_.getKey());
    }, (p_193759_0_) ->
    {
        return p_193759_0_.particles;
    }, (p_193669_0_, p_193669_1_, p_193669_2_) ->
    {
        p_193669_0_.particles = p_193669_2_;
    });
    public static final CycleOption<CloudStatus> RENDER_CLOUDS = CycleOption.a("options.renderClouds", CloudStatus.values(), (p_193628_0_) ->
    {
        return new TranslatableComponent(p_193628_0_.getKey());
    }, (p_193757_0_) ->
    {
        return p_193757_0_.renderClouds;
    }, (p_193657_0_, p_193657_1_, p_193657_2_) ->
    {
        p_193657_0_.renderClouds = p_193657_2_;

        if (Minecraft.useShaderTransparency())
        {
            RenderTarget rendertarget = Minecraft.getInstance().levelRenderer.getCloudsTarget();

            if (rendertarget != null)
            {
                rendertarget.clear(Minecraft.ON_OSX);
            }
        }
    });
    public static final CycleOption<Boolean> TEXT_BACKGROUND = CycleOption.createBinaryOption("options.accessibility.text_background", new TranslatableComponent("options.accessibility.text_background.chat"), new TranslatableComponent("options.accessibility.text_background.everywhere"), (p_168351_0_) ->
    {
        return p_168351_0_.backgroundForChatOnly;
    }, (p_168353_0_, p_168353_1_, p_168353_2_) ->
    {
        p_168353_0_.backgroundForChatOnly = p_168353_2_;
    });
    private static final Component CHAT_TOOLTIP_HIDE_MATCHED_NAMES = new TranslatableComponent("options.hideMatchedNames.tooltip");
    public static final CycleOption<Boolean> AUTO_JUMP = CycleOption.createOnOff("options.autoJump", (p_168345_0_) ->
    {
        return p_168345_0_.autoJump;
    }, (p_168347_0_, p_168347_1_, p_168347_2_) ->
    {
        p_168347_0_.autoJump = p_168347_2_;
    });
    public static final CycleOption<Boolean> AUTO_SUGGESTIONS = CycleOption.createOnOff("options.autoSuggestCommands", (p_168339_0_) ->
    {
        return p_168339_0_.autoSuggestions;
    }, (p_168341_0_, p_168341_1_, p_168341_2_) ->
    {
        p_168341_0_.autoSuggestions = p_168341_2_;
    });
    public static final CycleOption<Boolean> CHAT_COLOR = CycleOption.createOnOff("options.chat.color", (p_168333_0_) ->
    {
        return p_168333_0_.chatColors;
    }, (p_168335_0_, p_168335_1_, p_168335_2_) ->
    {
        p_168335_0_.chatColors = p_168335_2_;
    });
    public static final CycleOption<Boolean> HIDE_MATCHED_NAMES = CycleOption.createOnOff("options.hideMatchedNames", CHAT_TOOLTIP_HIDE_MATCHED_NAMES, (p_168327_0_) ->
    {
        return p_168327_0_.hideMatchedNames;
    }, (p_168329_0_, p_168329_1_, p_168329_2_) ->
    {
        p_168329_0_.hideMatchedNames = p_168329_2_;
    });
    public static final CycleOption<Boolean> CHAT_LINKS = CycleOption.createOnOff("options.chat.links", (p_168321_0_) ->
    {
        return p_168321_0_.chatLinks;
    }, (p_168323_0_, p_168323_1_, p_168323_2_) ->
    {
        p_168323_0_.chatLinks = p_168323_2_;
    });
    public static final CycleOption<Boolean> CHAT_LINKS_PROMPT = CycleOption.createOnOff("options.chat.links.prompt", (p_168315_0_) ->
    {
        return p_168315_0_.chatLinksPrompt;
    }, (p_168317_0_, p_168317_1_, p_168317_2_) ->
    {
        p_168317_0_.chatLinksPrompt = p_168317_2_;
    });
    public static final CycleOption<Boolean> DISCRETE_MOUSE_SCROLL = CycleOption.createOnOff("options.discrete_mouse_scroll", (p_168303_0_) ->
    {
        return p_168303_0_.discreteMouseScroll;
    }, (p_168305_0_, p_168305_1_, p_168305_2_) ->
    {
        p_168305_0_.discreteMouseScroll = p_168305_2_;
    });
    public static final CycleOption<Boolean> ENABLE_VSYNC = CycleOption.createOnOff("options.vsync", (p_168291_0_) ->
    {
        return p_168291_0_.enableVsync;
    }, (p_168293_0_, p_168293_1_, p_168293_2_) ->
    {
        p_168293_0_.enableVsync = p_168293_2_;

        if (Minecraft.getInstance().getWindow() != null)
        {
            Minecraft.getInstance().getWindow().updateVsync(p_168293_0_.enableVsync);
        }
    });
    public static final CycleOption<Boolean> ENTITY_SHADOWS = CycleOption.createOnOff("options.entityShadows", (p_168279_0_) ->
    {
        return p_168279_0_.entityShadows;
    }, (p_168281_0_, p_168281_1_, p_168281_2_) ->
    {
        p_168281_0_.entityShadows = p_168281_2_;
    });
    public static final CycleOption<Boolean> FORCE_UNICODE_FONT = CycleOption.createOnOff("options.forceUnicodeFont", (p_168267_0_) ->
    {
        return p_168267_0_.forceUnicodeFont;
    }, (p_168269_0_, p_168269_1_, p_168269_2_) ->
    {
        p_168269_0_.forceUnicodeFont = p_168269_2_;
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.getWindow() != null)
        {
            minecraft.selectMainFont(p_168269_2_);
            minecraft.resizeDisplay();
        }
    });
    public static final CycleOption<Boolean> INVERT_MOUSE = CycleOption.createOnOff("options.invertMouse", (p_168255_0_) ->
    {
        return p_168255_0_.invertYMouse;
    }, (p_168257_0_, p_168257_1_, p_168257_2_) ->
    {
        p_168257_0_.invertYMouse = p_168257_2_;
    });
    public static final CycleOption<Boolean> REALMS_NOTIFICATIONS = CycleOption.createOnOff("options.realmsNotifications", (p_168243_0_) ->
    {
        return p_168243_0_.realmsNotifications;
    }, (p_168245_0_, p_168245_1_, p_168245_2_) ->
    {
        p_168245_0_.realmsNotifications = p_168245_2_;
    });
    private static final Component ALLOW_SERVER_LISTING_TOOLTIP = new TranslatableComponent("options.allowServerListing.tooltip");
    public static final CycleOption<Boolean> ALLOW_SERVER_LISTING = CycleOption.createOnOff("options.allowServerListing", ALLOW_SERVER_LISTING_TOOLTIP, (p_168231_0_) ->
    {
        return p_168231_0_.allowServerListing;
    }, (p_168233_0_, p_168233_1_, p_168233_2_) ->
    {
        p_168233_0_.allowServerListing = p_168233_2_;
        p_168233_0_.broadcastOptions();
    });
    public static final CycleOption<Boolean> REDUCED_DEBUG_INFO = CycleOption.createOnOff("options.reducedDebugInfo", (p_168216_0_) ->
    {
        return p_168216_0_.reducedDebugInfo;
    }, (p_168218_0_, p_168218_1_, p_168218_2_) ->
    {
        p_168218_0_.reducedDebugInfo = p_168218_2_;
    });
    public static final CycleOption<Boolean> SHOW_SUBTITLES = CycleOption.createOnOff("options.showSubtitles", (p_168154_0_) ->
    {
        return p_168154_0_.showSubtitles;
    }, (p_168188_0_, p_168188_1_, p_168188_2_) ->
    {
        p_168188_0_.showSubtitles = p_168188_2_;
    });
    private static final Component MOVEMENT_TOGGLE = new TranslatableComponent("options.key.toggle");
    private static final Component MOVEMENT_HOLD = new TranslatableComponent("options.key.hold");
    public static final CycleOption<Boolean> TOGGLE_CROUCH = CycleOption.createBinaryOption("key.sneak", MOVEMENT_TOGGLE, MOVEMENT_HOLD, (p_193751_0_) ->
    {
        return p_193751_0_.toggleCrouch;
    }, (p_193753_0_, p_193753_1_, p_193753_2_) ->
    {
        p_193753_0_.toggleCrouch = p_193753_2_;
    });
    public static final CycleOption<Boolean> TOGGLE_SPRINT = CycleOption.createBinaryOption("key.sprint", MOVEMENT_TOGGLE, MOVEMENT_HOLD, (p_193745_0_) ->
    {
        return p_193745_0_.toggleSprint;
    }, (p_193747_0_, p_193747_1_, p_193747_2_) ->
    {
        p_193747_0_.toggleSprint = p_193747_2_;
    });
    public static final CycleOption<Boolean> TOUCHSCREEN = CycleOption.createOnOff("options.touchscreen", (p_193739_0_) ->
    {
        return p_193739_0_.touchscreen;
    }, (p_193741_0_, p_193741_1_, p_193741_2_) ->
    {
        p_193741_0_.touchscreen = p_193741_2_;
    });
    public static final CycleOption<Boolean> USE_FULLSCREEN = CycleOption.createOnOff("options.fullscreen", (p_193733_0_) ->
    {
        return p_193733_0_.fullscreen;
    }, (p_193735_0_, p_193735_1_, p_193735_2_) ->
    {
        p_193735_0_.fullscreen = p_193735_2_;
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.getWindow() != null && minecraft.getWindow().isFullscreen() != p_193735_0_.fullscreen)
        {
            minecraft.getWindow().toggleFullScreen();
            p_193735_0_.fullscreen = minecraft.getWindow().isFullscreen();
        }
    });
    public static final CycleOption<Boolean> VIEW_BOBBING = CycleOption.createOnOff("options.viewBobbing", (p_193727_0_) ->
    {
        return p_193727_0_.bobView;
    }, (p_193729_0_, p_193729_1_, p_193729_2_) ->
    {
        p_193729_0_.bobView = p_193729_2_;
    });
    private static final Component ACCESSIBILITY_TOOLTIP_DARK_MOJANG_BACKGROUND = new TranslatableComponent("options.darkMojangStudiosBackgroundColor.tooltip");
    public static final CycleOption<Boolean> DARK_MOJANG_STUDIOS_BACKGROUND_COLOR = CycleOption.createOnOff("options.darkMojangStudiosBackgroundColor", ACCESSIBILITY_TOOLTIP_DARK_MOJANG_BACKGROUND, (p_193720_0_) ->
    {
        return p_193720_0_.darkMojangStudiosBackground;
    }, (p_193722_0_, p_193722_1_, p_193722_2_) ->
    {
        p_193722_0_.darkMojangStudiosBackground = p_193722_2_;
    });
    private static final Component ACCESSIBILITY_TOOLTIP_HIDE_LIGHTNING_FLASHES = new TranslatableComponent("options.hideLightningFlashes.tooltip");
    public static final CycleOption<Boolean> HIDE_LIGHTNING_FLASH = CycleOption.createOnOff("options.hideLightningFlashes", ACCESSIBILITY_TOOLTIP_HIDE_LIGHTNING_FLASHES, (p_193713_0_) ->
    {
        return p_193713_0_.hideLightningFlashes;
    }, (p_193715_0_, p_193715_1_, p_193715_2_) ->
    {
        p_193715_0_.hideLightningFlashes = p_193715_2_;
    });
    public static final CycleOption<Boolean> AUTOSAVE_INDICATOR = CycleOption.createOnOff("options.autosaveIndicator", (p_193639_0_) ->
    {
        return p_193639_0_.showAutosaveIndicator;
    }, (p_193677_0_, p_193677_1_, p_193677_2_) ->
    {
        p_193677_0_.showAutosaveIndicator = p_193677_2_;
    });
    private final Component caption;
    private final String translationKey;
    public static final Option FOG_FANCY = new IteratableOptionOF("of.options.FOG_FANCY");
    public static final Option FOG_START = new IteratableOptionOF("of.options.FOG_START");
    public static final Option MIPMAP_TYPE = new SliderPercentageOptionOF("of.options.MIPMAP_TYPE", 0.0D, 3.0D, 1.0F);
    public static final Option SMOOTH_FPS = new IteratableOptionOF("of.options.SMOOTH_FPS");
    public static final Option CLOUDS = new IteratableOptionOF("of.options.CLOUDS");
    public static final Option CLOUD_HEIGHT = new SliderPercentageOptionOF("of.options.CLOUD_HEIGHT");
    public static final Option TREES = new IteratableOptionOF("of.options.TREES");
    public static final Option RAIN = new IteratableOptionOF("of.options.RAIN");
    public static final Option ANIMATED_WATER = new IteratableOptionOF("of.options.ANIMATED_WATER");
    public static final Option ANIMATED_LAVA = new IteratableOptionOF("of.options.ANIMATED_LAVA");
    public static final Option ANIMATED_FIRE = new IteratableOptionOF("of.options.ANIMATED_FIRE");
    public static final Option ANIMATED_PORTAL = new IteratableOptionOF("of.options.ANIMATED_PORTAL");
    public static final Option AO_LEVEL = new SliderPercentageOptionOF("of.options.AO_LEVEL");
    public static final Option LAGOMETER = new IteratableOptionOF("of.options.LAGOMETER");
    public static final Option SHOW_FPS = new IteratableOptionOF("of.options.SHOW_FPS");
    public static final Option AUTOSAVE_TICKS = new IteratableOptionOF("of.options.AUTOSAVE_TICKS");
    public static final Option BETTER_GRASS = new IteratableOptionOF("of.options.BETTER_GRASS");
    public static final Option ANIMATED_REDSTONE = new IteratableOptionOF("of.options.ANIMATED_REDSTONE");
    public static final Option ANIMATED_EXPLOSION = new IteratableOptionOF("of.options.ANIMATED_EXPLOSION");
    public static final Option ANIMATED_FLAME = new IteratableOptionOF("of.options.ANIMATED_FLAME");
    public static final Option ANIMATED_SMOKE = new IteratableOptionOF("of.options.ANIMATED_SMOKE");
    public static final Option WEATHER = new IteratableOptionOF("of.options.WEATHER");
    public static final Option SKY = new IteratableOptionOF("of.options.SKY");
    public static final Option STARS = new IteratableOptionOF("of.options.STARS");
    public static final Option SUN_MOON = new IteratableOptionOF("of.options.SUN_MOON");
    public static final Option VIGNETTE = new IteratableOptionOF("of.options.VIGNETTE");
    public static final Option CHUNK_UPDATES = new IteratableOptionOF("of.options.CHUNK_UPDATES");
    public static final Option CHUNK_UPDATES_DYNAMIC = new IteratableOptionOF("of.options.CHUNK_UPDATES_DYNAMIC");
    public static final Option TIME = new IteratableOptionOF("of.options.TIME");
    public static final Option SMOOTH_WORLD = new IteratableOptionOF("of.options.SMOOTH_WORLD");
    public static final Option VOID_PARTICLES = new IteratableOptionOF("of.options.VOID_PARTICLES");
    public static final Option WATER_PARTICLES = new IteratableOptionOF("of.options.WATER_PARTICLES");
    public static final Option RAIN_SPLASH = new IteratableOptionOF("of.options.RAIN_SPLASH");
    public static final Option PORTAL_PARTICLES = new IteratableOptionOF("of.options.PORTAL_PARTICLES");
    public static final Option POTION_PARTICLES = new IteratableOptionOF("of.options.POTION_PARTICLES");
    public static final Option FIREWORK_PARTICLES = new IteratableOptionOF("of.options.FIREWORK_PARTICLES");
    public static final Option PROFILER = new IteratableOptionOF("of.options.PROFILER");
    public static final Option DRIPPING_WATER_LAVA = new IteratableOptionOF("of.options.DRIPPING_WATER_LAVA");
    public static final Option BETTER_SNOW = new IteratableOptionOF("of.options.BETTER_SNOW");
    public static final Option ANIMATED_TERRAIN = new IteratableOptionOF("of.options.ANIMATED_TERRAIN");
    public static final Option SWAMP_COLORS = new IteratableOptionOF("of.options.SWAMP_COLORS");
    public static final Option RANDOM_ENTITIES = new IteratableOptionOF("of.options.RANDOM_ENTITIES");
    public static final Option SMOOTH_BIOMES = new IteratableOptionOF("of.options.SMOOTH_BIOMES");
    public static final Option CUSTOM_FONTS = new IteratableOptionOF("of.options.CUSTOM_FONTS");
    public static final Option CUSTOM_COLORS = new IteratableOptionOF("of.options.CUSTOM_COLORS");
    public static final Option SHOW_CAPES = new IteratableOptionOF("of.options.SHOW_CAPES");
    public static final Option CONNECTED_TEXTURES = new IteratableOptionOF("of.options.CONNECTED_TEXTURES");
    public static final Option CUSTOM_ITEMS = new IteratableOptionOF("of.options.CUSTOM_ITEMS");
    public static final Option AA_LEVEL = new SliderPercentageOptionOF("of.options.AA_LEVEL", 0.0D, 16.0D, new double[] {0.0D, 2.0D, 4.0D, 6.0D, 8.0D, 12.0D, 16.0D});
    public static final Option AF_LEVEL = new SliderPercentageOptionOF("of.options.AF_LEVEL", 1.0D, 16.0D, new double[] {1.0D, 2.0D, 4.0D, 8.0D, 16.0D});
    public static final Option ANIMATED_TEXTURES = new IteratableOptionOF("of.options.ANIMATED_TEXTURES");
    public static final Option NATURAL_TEXTURES = new IteratableOptionOF("of.options.NATURAL_TEXTURES");
    public static final Option EMISSIVE_TEXTURES = new IteratableOptionOF("of.options.EMISSIVE_TEXTURES");
    public static final Option HELD_ITEM_TOOLTIPS = new IteratableOptionOF("of.options.HELD_ITEM_TOOLTIPS");
    public static final Option LAZY_CHUNK_LOADING = new IteratableOptionOF("of.options.LAZY_CHUNK_LOADING");
    public static final Option CUSTOM_SKY = new IteratableOptionOF("of.options.CUSTOM_SKY");
    public static final Option FAST_MATH = new IteratableOptionOF("of.options.FAST_MATH");
    public static final Option FAST_RENDER = new IteratableOptionOF("of.options.FAST_RENDER");
    public static final Option DYNAMIC_FOV = new IteratableOptionOF("of.options.DYNAMIC_FOV");
    public static final Option DYNAMIC_LIGHTS = new IteratableOptionOF("of.options.DYNAMIC_LIGHTS");
    public static final Option ALTERNATE_BLOCKS = new IteratableOptionOF("of.options.ALTERNATE_BLOCKS");
    public static final Option CUSTOM_ENTITY_MODELS = new IteratableOptionOF("of.options.CUSTOM_ENTITY_MODELS");
    public static final Option ADVANCED_TOOLTIPS = new IteratableOptionOF("of.options.ADVANCED_TOOLTIPS");
    public static final Option SCREENSHOT_SIZE = new IteratableOptionOF("of.options.SCREENSHOT_SIZE");
    public static final Option CUSTOM_GUIS = new IteratableOptionOF("of.options.CUSTOM_GUIS");
    public static final Option RENDER_REGIONS = new IteratableOptionOF("of.options.RENDER_REGIONS");
    public static final Option SHOW_GL_ERRORS = new IteratableOptionOF("of.options.SHOW_GL_ERRORS");
    public static final Option SMART_ANIMATIONS = new IteratableOptionOF("of.options.SMART_ANIMATIONS");
    public static final Option CHAT_BACKGROUND = new IteratableOptionOF("of.options.CHAT_BACKGROUND");
    public static final Option CHAT_SHADOW = new IteratableOptionOF("of.options.CHAT_SHADOW");

    public Option(String pCaptionKey)
    {
        this.caption = new TranslatableComponent(pCaptionKey);
        this.translationKey = pCaptionKey;
        OPTIONS_BY_KEY.put(pCaptionKey, this);
    }

    public abstract AbstractWidget createButton(Options pOptions, int pX, int pY, int pWidth);

    public Component getCaption()
    {
        return this.caption;
    }

    protected Component pixelValueLabel(int pValue)
    {
        return new TranslatableComponent("options.pixel_value", this.getCaption(), pValue);
    }

    protected Component percentValueLabel(double pPercentage)
    {
        return new TranslatableComponent("options.percent_value", this.getCaption(), (int)(pPercentage * 100.0D));
    }

    protected Component percentAddValueLabel(int pDoubleValue)
    {
        return new TranslatableComponent("options.percent_add_value", this.getCaption(), pDoubleValue);
    }

    public Component genericValueLabel(Component pValue)
    {
        return new TranslatableComponent("options.generic_value", this.getCaption(), pValue);
    }

    public Component genericValueLabel(int pValue)
    {
        return this.genericValueLabel(new TextComponent(Integer.toString(pValue)));
    }

    public String getResourceKey()
    {
        return this.translationKey;
    }
}
