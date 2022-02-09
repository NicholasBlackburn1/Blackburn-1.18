package net.minecraft.data.tags;

import java.nio.file.Path;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

public class EntityTypeTagsProvider extends TagsProvider < EntityType<? >>
{
    public EntityTypeTagsProvider(DataGenerator pGenerator)
    {
        super(pGenerator, Registry.ENTITY_TYPE);
    }

    protected void addTags()
    {
        this.tag(EntityTypeTags.SKELETONS).a(EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON);
        this.tag(EntityTypeTags.RAIDERS).a(EntityType.EVOKER, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.WITCH);
        this.tag(EntityTypeTags.BEEHIVE_INHABITORS).add(EntityType.BEE);
        this.tag(EntityTypeTags.ARROWS).a(EntityType.ARROW, EntityType.SPECTRAL_ARROW);
        this.tag(EntityTypeTags.IMPACT_PROJECTILES).addTag(EntityTypeTags.ARROWS).a(EntityType.SNOWBALL, EntityType.FIREBALL, EntityType.SMALL_FIREBALL, EntityType.EGG, EntityType.TRIDENT, EntityType.DRAGON_FIREBALL, EntityType.WITHER_SKULL);
        this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).a(EntityType.RABBIT, EntityType.ENDERMITE, EntityType.SILVERFISH, EntityType.FOX);
        this.tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS).a(EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.SALMON, EntityType.COD, EntityType.SQUID, EntityType.GLOW_SQUID);
        this.tag(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES).a(EntityType.DROWNED, EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN);
        this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).a(EntityType.STRAY, EntityType.POLAR_BEAR, EntityType.SNOW_GOLEM, EntityType.WITHER);
        this.tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).a(EntityType.STRIDER, EntityType.BLAZE, EntityType.MAGMA_CUBE);
    }

    protected Path getPath(ResourceLocation pId)
    {
        return this.generator.getOutputFolder().resolve("data/" + pId.getNamespace() + "/tags/entity_types/" + pId.getPath() + ".json");
    }

    public String getName()
    {
        return "Entity Type Tags";
    }
}
