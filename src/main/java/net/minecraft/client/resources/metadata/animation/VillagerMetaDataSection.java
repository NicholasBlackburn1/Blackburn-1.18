package net.minecraft.client.resources.metadata.animation;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class VillagerMetaDataSection
{
    public static final VillagerMetadataSectionSerializer SERIALIZER = new VillagerMetadataSectionSerializer();
    public static final String SECTION_NAME = "villager";
    private final VillagerMetaDataSection.Hat hat;

    public VillagerMetaDataSection(VillagerMetaDataSection.Hat p_119069_)
    {
        this.hat = p_119069_;
    }

    public VillagerMetaDataSection.Hat getHat()
    {
        return this.hat;
    }

    public static enum Hat
    {
        NONE("none"),
        PARTIAL("partial"),
        FULL("full");

        private static final Map<String, VillagerMetaDataSection.Hat> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(VillagerMetaDataSection.Hat::getName, (p_119084_) -> {
            return p_119084_;
        }));
        private final String name;

        private Hat(String p_119081_)
        {
            this.name = p_119081_;
        }

        public String getName()
        {
            return this.name;
        }

        public static VillagerMetaDataSection.Hat getByName(String pName)
        {
            return BY_NAME.getOrDefault(pName, NONE);
        }
    }
}
