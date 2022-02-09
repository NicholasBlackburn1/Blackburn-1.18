package com.mojang.realmsclient.dto;

import com.google.gson.annotations.SerializedName;

public class RealmsWorldResetDto extends ValueObject implements ReflectionBasedSerialization
{
    @SerializedName("seed")
    private final String seed;
    @SerializedName("worldTemplateId")
    private final long worldTemplateId;
    @SerializedName("levelType")
    private final int levelType;
    @SerializedName("generateStructures")
    private final boolean generateStructures;

    public RealmsWorldResetDto(String pSeed, long pWorldTemplateId, int p_87645_, boolean pLevelType)
    {
        this.seed = pSeed;
        this.worldTemplateId = pWorldTemplateId;
        this.levelType = p_87645_;
        this.generateStructures = pLevelType;
    }
}
