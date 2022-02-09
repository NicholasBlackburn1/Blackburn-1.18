package net.minecraft.resources;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.Registry;

public class ResourceKey<T>
{
    private static final Map < String, ResourceKey<? >> VALUES = Collections.synchronizedMap(Maps.newIdentityHashMap());
    private final ResourceLocation registryName;
    private final ResourceLocation location;

    public static <T> Codec<ResourceKey<T>> codec(ResourceKey <? extends Registry<T >> p_195967_)
    {
        return ResourceLocation.CODEC.xmap((p_195979_) ->
        {
            return create(p_195967_, p_195979_);
        }, ResourceKey::location);
    }

    public static <T> ResourceKey<T> create(ResourceKey <? extends Registry<T >> pRegistryKey, ResourceLocation pLocation)
    {
        return create(pRegistryKey.location, pLocation);
    }

    public static <T> ResourceKey<Registry<T>> createRegistryKey(ResourceLocation pLocation)
    {
        return create(Registry.ROOT_REGISTRY_NAME, pLocation);
    }

    private static <T> ResourceKey<T> create(ResourceLocation pRegistryKey, ResourceLocation pLocation)
    {
        String s = (pRegistryKey + ":" + pLocation).intern();
        return (ResourceKey<T>)VALUES.computeIfAbsent(s, (p_195971_) ->
        {
            return new ResourceKey(pRegistryKey, pLocation);
        });
    }

    private ResourceKey(ResourceLocation pRegistryName, ResourceLocation pLocation)
    {
        this.registryName = pRegistryName;
        this.location = pLocation;
    }

    public String toString()
    {
        return "ResourceKey[" + this.registryName + " / " + this.location + "]";
    }

    public boolean isFor(ResourceKey <? extends Registry<? >> pRegistryKey)
    {
        return this.registryName.equals(pRegistryKey.location());
    }

    public <E> Optional<ResourceKey<E>> cast(ResourceKey <? extends Registry<E >> p_195976_)
    {
        return this.isFor(p_195976_) ? Optional.of((ResourceKey<E>)this) : Optional.empty();
    }

    public ResourceLocation location()
    {
        return this.location;
    }

    public static <T> Function<ResourceLocation, ResourceKey<T>> elementKey(ResourceKey <? extends Registry<T >> pRegistryKey)
    {
        return (p_195974_) ->
        {
            return create(pRegistryKey, p_195974_);
        };
    }
}
