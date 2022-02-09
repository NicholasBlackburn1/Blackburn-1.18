package net.minecraft.data.models.model;

import java.util.Optional;
import java.util.stream.IntStream;
import net.minecraft.resources.ResourceLocation;

public class ModelTemplates
{
    public static final ModelTemplate CUBE = a("cube", TextureSlot.PARTICLE, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN);
    public static final ModelTemplate CUBE_DIRECTIONAL = a("cube_directional", TextureSlot.PARTICLE, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN);
    public static final ModelTemplate CUBE_ALL = a("cube_all", TextureSlot.ALL);
    public static final ModelTemplate CUBE_MIRRORED_ALL = a("cube_mirrored_all", "_mirrored", TextureSlot.ALL);
    public static final ModelTemplate CUBE_COLUMN = a("cube_column", TextureSlot.END, TextureSlot.SIDE);
    public static final ModelTemplate CUBE_COLUMN_HORIZONTAL = a("cube_column_horizontal", "_horizontal", TextureSlot.END, TextureSlot.SIDE);
    public static final ModelTemplate CUBE_COLUMN_MIRRORED = a("cube_column_mirrored", "_mirrored", TextureSlot.END, TextureSlot.SIDE);
    public static final ModelTemplate CUBE_TOP = a("cube_top", TextureSlot.TOP, TextureSlot.SIDE);
    public static final ModelTemplate CUBE_BOTTOM_TOP = a("cube_bottom_top", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
    public static final ModelTemplate CUBE_ORIENTABLE = a("orientable", TextureSlot.TOP, TextureSlot.FRONT, TextureSlot.SIDE);
    public static final ModelTemplate CUBE_ORIENTABLE_TOP_BOTTOM = a("orientable_with_bottom", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.FRONT);
    public static final ModelTemplate CUBE_ORIENTABLE_VERTICAL = a("orientable_vertical", "_vertical", TextureSlot.FRONT, TextureSlot.SIDE);
    public static final ModelTemplate BUTTON = a("button", TextureSlot.TEXTURE);
    public static final ModelTemplate BUTTON_PRESSED = a("button_pressed", "_pressed", TextureSlot.TEXTURE);
    public static final ModelTemplate BUTTON_INVENTORY = a("button_inventory", "_inventory", TextureSlot.TEXTURE);
    public static final ModelTemplate DOOR_BOTTOM = a("door_bottom", "_bottom", TextureSlot.TOP, TextureSlot.BOTTOM);
    public static final ModelTemplate DOOR_BOTTOM_HINGE = a("door_bottom_rh", "_bottom_hinge", TextureSlot.TOP, TextureSlot.BOTTOM);
    public static final ModelTemplate DOOR_TOP = a("door_top", "_top", TextureSlot.TOP, TextureSlot.BOTTOM);
    public static final ModelTemplate DOOR_TOP_HINGE = a("door_top_rh", "_top_hinge", TextureSlot.TOP, TextureSlot.BOTTOM);
    public static final ModelTemplate FENCE_POST = a("fence_post", "_post", TextureSlot.TEXTURE);
    public static final ModelTemplate FENCE_SIDE = a("fence_side", "_side", TextureSlot.TEXTURE);
    public static final ModelTemplate FENCE_INVENTORY = a("fence_inventory", "_inventory", TextureSlot.TEXTURE);
    public static final ModelTemplate WALL_POST = a("template_wall_post", "_post", TextureSlot.WALL);
    public static final ModelTemplate WALL_LOW_SIDE = a("template_wall_side", "_side", TextureSlot.WALL);
    public static final ModelTemplate WALL_TALL_SIDE = a("template_wall_side_tall", "_side_tall", TextureSlot.WALL);
    public static final ModelTemplate WALL_INVENTORY = a("wall_inventory", "_inventory", TextureSlot.WALL);
    public static final ModelTemplate FENCE_GATE_CLOSED = a("template_fence_gate", TextureSlot.TEXTURE);
    public static final ModelTemplate FENCE_GATE_OPEN = a("template_fence_gate_open", "_open", TextureSlot.TEXTURE);
    public static final ModelTemplate FENCE_GATE_WALL_CLOSED = a("template_fence_gate_wall", "_wall", TextureSlot.TEXTURE);
    public static final ModelTemplate FENCE_GATE_WALL_OPEN = a("template_fence_gate_wall_open", "_wall_open", TextureSlot.TEXTURE);
    public static final ModelTemplate PRESSURE_PLATE_UP = a("pressure_plate_up", TextureSlot.TEXTURE);
    public static final ModelTemplate PRESSURE_PLATE_DOWN = a("pressure_plate_down", "_down", TextureSlot.TEXTURE);
    public static final ModelTemplate PARTICLE_ONLY = a(TextureSlot.PARTICLE);
    public static final ModelTemplate SLAB_BOTTOM = a("slab", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
    public static final ModelTemplate SLAB_TOP = a("slab_top", "_top", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
    public static final ModelTemplate LEAVES = a("leaves", TextureSlot.ALL);
    public static final ModelTemplate STAIRS_STRAIGHT = a("stairs", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
    public static final ModelTemplate STAIRS_INNER = a("inner_stairs", "_inner", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
    public static final ModelTemplate STAIRS_OUTER = a("outer_stairs", "_outer", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
    public static final ModelTemplate TRAPDOOR_TOP = a("template_trapdoor_top", "_top", TextureSlot.TEXTURE);
    public static final ModelTemplate TRAPDOOR_BOTTOM = a("template_trapdoor_bottom", "_bottom", TextureSlot.TEXTURE);
    public static final ModelTemplate TRAPDOOR_OPEN = a("template_trapdoor_open", "_open", TextureSlot.TEXTURE);
    public static final ModelTemplate ORIENTABLE_TRAPDOOR_TOP = a("template_orientable_trapdoor_top", "_top", TextureSlot.TEXTURE);
    public static final ModelTemplate ORIENTABLE_TRAPDOOR_BOTTOM = a("template_orientable_trapdoor_bottom", "_bottom", TextureSlot.TEXTURE);
    public static final ModelTemplate ORIENTABLE_TRAPDOOR_OPEN = a("template_orientable_trapdoor_open", "_open", TextureSlot.TEXTURE);
    public static final ModelTemplate POINTED_DRIPSTONE = a("pointed_dripstone", TextureSlot.CROSS);
    public static final ModelTemplate CROSS = a("cross", TextureSlot.CROSS);
    public static final ModelTemplate TINTED_CROSS = a("tinted_cross", TextureSlot.CROSS);
    public static final ModelTemplate FLOWER_POT_CROSS = a("flower_pot_cross", TextureSlot.PLANT);
    public static final ModelTemplate TINTED_FLOWER_POT_CROSS = a("tinted_flower_pot_cross", TextureSlot.PLANT);
    public static final ModelTemplate RAIL_FLAT = a("rail_flat", TextureSlot.RAIL);
    public static final ModelTemplate RAIL_CURVED = a("rail_curved", "_corner", TextureSlot.RAIL);
    public static final ModelTemplate RAIL_RAISED_NE = a("template_rail_raised_ne", "_raised_ne", TextureSlot.RAIL);
    public static final ModelTemplate RAIL_RAISED_SW = a("template_rail_raised_sw", "_raised_sw", TextureSlot.RAIL);
    public static final ModelTemplate CARPET = a("carpet", TextureSlot.WOOL);
    public static final ModelTemplate CORAL_FAN = a("coral_fan", TextureSlot.FAN);
    public static final ModelTemplate CORAL_WALL_FAN = a("coral_wall_fan", TextureSlot.FAN);
    public static final ModelTemplate GLAZED_TERRACOTTA = a("template_glazed_terracotta", TextureSlot.PATTERN);
    public static final ModelTemplate CHORUS_FLOWER = a("template_chorus_flower", TextureSlot.TEXTURE);
    public static final ModelTemplate DAYLIGHT_DETECTOR = a("template_daylight_detector", TextureSlot.TOP, TextureSlot.SIDE);
    public static final ModelTemplate STAINED_GLASS_PANE_NOSIDE = a("template_glass_pane_noside", "_noside", TextureSlot.PANE);
    public static final ModelTemplate STAINED_GLASS_PANE_NOSIDE_ALT = a("template_glass_pane_noside_alt", "_noside_alt", TextureSlot.PANE);
    public static final ModelTemplate STAINED_GLASS_PANE_POST = a("template_glass_pane_post", "_post", TextureSlot.PANE, TextureSlot.EDGE);
    public static final ModelTemplate STAINED_GLASS_PANE_SIDE = a("template_glass_pane_side", "_side", TextureSlot.PANE, TextureSlot.EDGE);
    public static final ModelTemplate STAINED_GLASS_PANE_SIDE_ALT = a("template_glass_pane_side_alt", "_side_alt", TextureSlot.PANE, TextureSlot.EDGE);
    public static final ModelTemplate COMMAND_BLOCK = a("template_command_block", TextureSlot.FRONT, TextureSlot.BACK, TextureSlot.SIDE);
    public static final ModelTemplate ANVIL = a("template_anvil", TextureSlot.TOP);
    public static final ModelTemplate[] STEMS = IntStream.range(0, 8).mapToObj((p_125729_) ->
    {
        return a("stem_growth" + p_125729_, "_stage" + p_125729_, TextureSlot.STEM);
    }).toArray((p_125718_) ->
    {
        return new ModelTemplate[p_125718_];
    });
    public static final ModelTemplate ATTACHED_STEM = a("stem_fruit", TextureSlot.STEM, TextureSlot.UPPER_STEM);
    public static final ModelTemplate CROP = a("crop", TextureSlot.CROP);
    public static final ModelTemplate FARMLAND = a("template_farmland", TextureSlot.DIRT, TextureSlot.TOP);
    public static final ModelTemplate FIRE_FLOOR = a("template_fire_floor", TextureSlot.FIRE);
    public static final ModelTemplate FIRE_SIDE = a("template_fire_side", TextureSlot.FIRE);
    public static final ModelTemplate FIRE_SIDE_ALT = a("template_fire_side_alt", TextureSlot.FIRE);
    public static final ModelTemplate FIRE_UP = a("template_fire_up", TextureSlot.FIRE);
    public static final ModelTemplate FIRE_UP_ALT = a("template_fire_up_alt", TextureSlot.FIRE);
    public static final ModelTemplate CAMPFIRE = a("template_campfire", TextureSlot.FIRE, TextureSlot.LIT_LOG);
    public static final ModelTemplate LANTERN = a("template_lantern", TextureSlot.LANTERN);
    public static final ModelTemplate HANGING_LANTERN = a("template_hanging_lantern", "_hanging", TextureSlot.LANTERN);
    public static final ModelTemplate TORCH = a("template_torch", TextureSlot.TORCH);
    public static final ModelTemplate WALL_TORCH = a("template_torch_wall", TextureSlot.TORCH);
    public static final ModelTemplate PISTON = a("template_piston", TextureSlot.PLATFORM, TextureSlot.BOTTOM, TextureSlot.SIDE);
    public static final ModelTemplate PISTON_HEAD = a("template_piston_head", TextureSlot.PLATFORM, TextureSlot.SIDE, TextureSlot.UNSTICKY);
    public static final ModelTemplate PISTON_HEAD_SHORT = a("template_piston_head_short", TextureSlot.PLATFORM, TextureSlot.SIDE, TextureSlot.UNSTICKY);
    public static final ModelTemplate SEAGRASS = a("template_seagrass", TextureSlot.TEXTURE);
    public static final ModelTemplate TURTLE_EGG = a("template_turtle_egg", TextureSlot.ALL);
    public static final ModelTemplate TWO_TURTLE_EGGS = a("template_two_turtle_eggs", TextureSlot.ALL);
    public static final ModelTemplate THREE_TURTLE_EGGS = a("template_three_turtle_eggs", TextureSlot.ALL);
    public static final ModelTemplate FOUR_TURTLE_EGGS = a("template_four_turtle_eggs", TextureSlot.ALL);
    public static final ModelTemplate SINGLE_FACE = a("template_single_face", TextureSlot.TEXTURE);
    public static final ModelTemplate CAULDRON_LEVEL1 = a("template_cauldron_level1", TextureSlot.CONTENT, TextureSlot.INSIDE, TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
    public static final ModelTemplate CAULDRON_LEVEL2 = a("template_cauldron_level2", TextureSlot.CONTENT, TextureSlot.INSIDE, TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
    public static final ModelTemplate CAULDRON_FULL = a("template_cauldron_full", TextureSlot.CONTENT, TextureSlot.INSIDE, TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
    public static final ModelTemplate AZALEA = a("template_azalea", TextureSlot.TOP, TextureSlot.SIDE);
    public static final ModelTemplate POTTED_AZALEA = a("template_potted_azalea_bush", TextureSlot.TOP, TextureSlot.SIDE);
    public static final ModelTemplate FLAT_ITEM = b("generated", TextureSlot.LAYER0);
    public static final ModelTemplate FLAT_HANDHELD_ITEM = b("handheld", TextureSlot.LAYER0);
    public static final ModelTemplate FLAT_HANDHELD_ROD_ITEM = b("handheld_rod", TextureSlot.LAYER0);
    public static final ModelTemplate SHULKER_BOX_INVENTORY = b("template_shulker_box", TextureSlot.PARTICLE);
    public static final ModelTemplate BED_INVENTORY = b("template_bed", TextureSlot.PARTICLE);
    public static final ModelTemplate BANNER_INVENTORY = b("template_banner");
    public static final ModelTemplate SKULL_INVENTORY = b("template_skull");
    public static final ModelTemplate CANDLE = a("template_candle", TextureSlot.ALL, TextureSlot.PARTICLE);
    public static final ModelTemplate TWO_CANDLES = a("template_two_candles", TextureSlot.ALL, TextureSlot.PARTICLE);
    public static final ModelTemplate THREE_CANDLES = a("template_three_candles", TextureSlot.ALL, TextureSlot.PARTICLE);
    public static final ModelTemplate FOUR_CANDLES = a("template_four_candles", TextureSlot.ALL, TextureSlot.PARTICLE);
    public static final ModelTemplate CANDLE_CAKE = a("template_cake_with_candle", TextureSlot.CANDLE, TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.TOP, TextureSlot.PARTICLE);

    private static ModelTemplate a(TextureSlot... p_125727_)
    {
        return new ModelTemplate(Optional.empty(), Optional.empty(), p_125727_);
    }

    private static ModelTemplate a(String p_125724_, TextureSlot... p_125725_)
    {
        return new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "block/" + p_125724_)), Optional.empty(), p_125725_);
    }

    private static ModelTemplate b(String p_125731_, TextureSlot... p_125732_)
    {
        return new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/" + p_125731_)), Optional.empty(), p_125732_);
    }

    private static ModelTemplate a(String p_125720_, String p_125721_, TextureSlot... p_125722_)
    {
        return new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "block/" + p_125720_)), Optional.of(p_125721_), p_125722_);
    }
}
