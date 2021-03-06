package net.minecraft.core;

import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class DefaultedRegistry<T> extends MappedRegistry<T>
{
    private final ResourceLocation defaultKey;
    private T defaultValue;

    public DefaultedRegistry(String pDefaultName, ResourceKey <? extends Registry<T >> pRegistryKey, Lifecycle pLifecycle)
    {
        super(pRegistryKey, pLifecycle);
        this.defaultKey = new ResourceLocation(pDefaultName);
    }

    public <V extends T> V registerMapping(int pId, ResourceKey<T> pKey, V pValue, Lifecycle pLifecycle)
    {
        if (this.defaultKey.equals(pKey.location()))
        {
            this.defaultValue = (T)pValue;
        }

        return super.registerMapping(pId, pKey, pValue, pLifecycle);
    }

    public int getId(@Nullable T pValue)
    {
        int i = super.getId(pValue);
        return i == -1 ? super.getId(this.defaultValue) : i;
    }

    @Nonnull
    public ResourceLocation getKey(T pValue)
    {
        ResourceLocation resourcelocation = super.getKey(pValue);
        return resourcelocation == null ? this.defaultKey : resourcelocation;
    }

    @Nonnull
    public T get(@Nullable ResourceLocation pName)
    {
        T t = super.get(pName);
        return (T)(t == null ? this.defaultValue : t);
    }

    public Optional<T> getOptional(@Nullable ResourceLocation pName)
    {
        return Optional.ofNullable(super.get(pName));
    }

    @Nonnull
    public T byId(int pId)
    {
        T t = super.byId(pId);
        return (T)(t == null ? this.defaultValue : t);
    }

    @Nonnull
    public T getRandom(Random pRandom)
    {
        T t = super.getRandom(pRandom);
        return (T)(t == null ? this.defaultValue : t);
    }

    public ResourceLocation getDefaultKey()
    {
        return this.defaultKey;
    }
}
