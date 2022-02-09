package net.minecraft.client.renderer.texture;

import java.util.Collection;

public class StitcherException extends RuntimeException
{
    private final Collection<TextureAtlasSprite.Info> allSprites;

    public StitcherException(TextureAtlasSprite.Info pSpriteInfo, Collection<TextureAtlasSprite.Info> pAllSprites)
    {
        super(String.format("Unable to fit: %s - size: %dx%d - Maybe try a lower resolution resourcepack?", pSpriteInfo.name(), pSpriteInfo.width(), pSpriteInfo.height()));
        this.allSprites = pAllSprites;
    }

    public Collection<TextureAtlasSprite.Info> getAllSprites()
    {
        return this.allSprites;
    }

    public StitcherException(TextureAtlasSprite.Info spriteInfoIn, Collection<TextureAtlasSprite.Info> spriteInfosIn, int atlasWidth, int atlasHeight, int maxWidth, int maxHeight)
    {
        super(String.format("Unable to fit: %s, size: %dx%d, atlas: %dx%d, atlasMax: %dx%d - Maybe try a lower resolution resourcepack?", "" + spriteInfoIn.name(), spriteInfoIn.width(), spriteInfoIn.height(), atlasWidth, atlasHeight, maxWidth, maxHeight));
        this.allSprites = spriteInfosIn;
    }
}
