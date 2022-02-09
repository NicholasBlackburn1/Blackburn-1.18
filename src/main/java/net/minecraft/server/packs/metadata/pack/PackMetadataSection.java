package net.minecraft.server.packs.metadata.pack;

import net.minecraft.network.chat.Component;

public class PackMetadataSection
{
    public static final PackMetadataSectionSerializer SERIALIZER = new PackMetadataSectionSerializer();
    private final Component description;
    private final int packFormat;

    public PackMetadataSection(Component pDescription, int pPackFormat)
    {
        this.description = pDescription;
        this.packFormat = pPackFormat;
    }

    public Component getDescription()
    {
        return this.description;
    }

    public int getPackFormat()
    {
        return this.packFormat;
    }
}
