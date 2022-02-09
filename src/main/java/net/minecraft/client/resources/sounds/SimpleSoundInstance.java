package net.minecraft.client.resources.sounds;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class SimpleSoundInstance extends AbstractSoundInstance
{
    public SimpleSoundInstance(SoundEvent pSoundEvent, SoundSource pSource, float pVolume, float pPitch, BlockPos pPos)
    {
        this(pSoundEvent, pSource, pVolume, pPitch, (double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D);
    }

    public static SimpleSoundInstance forUI(SoundEvent pSound, float pPitch)
    {
        return forUI(pSound, pPitch, 0.25F);
    }

    public static SimpleSoundInstance forUI(SoundEvent pSound, float pPitch, float pVolume)
    {
        return new SimpleSoundInstance(pSound.getLocation(), SoundSource.MASTER, pVolume, pPitch, false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
    }

    public static SimpleSoundInstance forMusic(SoundEvent pSound)
    {
        return new SimpleSoundInstance(pSound.getLocation(), SoundSource.MUSIC, 1.0F, 1.0F, false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
    }

    public static SimpleSoundInstance forRecord(SoundEvent pSound, double pX, double p_119750_, double pY)
    {
        return new SimpleSoundInstance(pSound, SoundSource.RECORDS, 4.0F, 1.0F, false, 0, SoundInstance.Attenuation.LINEAR, pX, p_119750_, pY);
    }

    public static SimpleSoundInstance forLocalAmbience(SoundEvent pSound, float pVolume, float pPitch)
    {
        return new SimpleSoundInstance(pSound.getLocation(), SoundSource.AMBIENT, pPitch, pVolume, false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
    }

    public static SimpleSoundInstance forAmbientAddition(SoundEvent pSound)
    {
        return forLocalAmbience(pSound, 1.0F, 1.0F);
    }

    public static SimpleSoundInstance forAmbientMood(SoundEvent pSound, double pX, double p_119764_, double pY)
    {
        return new SimpleSoundInstance(pSound, SoundSource.AMBIENT, 1.0F, 1.0F, false, 0, SoundInstance.Attenuation.LINEAR, pX, p_119764_, pY);
    }

    public SimpleSoundInstance(SoundEvent pSoundEvent, SoundSource pSource, float pVolume, float pPitch, double pX, double p_119714_, double pY)
    {
        this(pSoundEvent, pSource, pVolume, pPitch, false, 0, SoundInstance.Attenuation.LINEAR, pX, p_119714_, pY);
    }

    private SimpleSoundInstance(SoundEvent pSoundEvent, SoundSource pSource, float pVolume, float pPitch, boolean pLooping, int pDelay, SoundInstance.Attenuation pAttenuation, double pX, double p_119731_, double pY)
    {
        this(pSoundEvent.getLocation(), pSource, pVolume, pPitch, pLooping, pDelay, pAttenuation, pX, p_119731_, pY, false);
    }

    public SimpleSoundInstance(ResourceLocation pLocation, SoundSource pSource, float pVolume, float pPitch, boolean pLooping, int pDelay, SoundInstance.Attenuation pAttenuation, double pX, double p_119742_, double pY, boolean p_119744_)
    {
        super(pLocation, pSource);
        this.volume = pVolume;
        this.pitch = pPitch;
        this.x = pX;
        this.y = p_119742_;
        this.z = pY;
        this.looping = pLooping;
        this.delay = pDelay;
        this.attenuation = pAttenuation;
        this.relative = p_119744_;
    }
}
