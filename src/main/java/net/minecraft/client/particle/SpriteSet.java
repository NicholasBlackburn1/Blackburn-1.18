package net.minecraft.client.particle;

import java.util.Random;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface SpriteSet
{
    TextureAtlasSprite get(int pAge, int pLifetime);

    TextureAtlasSprite get(Random pRandom);
}
