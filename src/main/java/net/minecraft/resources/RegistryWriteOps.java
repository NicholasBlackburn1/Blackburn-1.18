package net.minecraft.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;

public class RegistryWriteOps<T> extends DelegatingOps<T>
{
    private final RegistryAccess registryAccess;

    public static <T> RegistryWriteOps<T> create(DynamicOps<T> pDelegate, RegistryAccess pRegistryAccess)
    {
        return new RegistryWriteOps<>(pDelegate, pRegistryAccess);
    }

    private RegistryWriteOps(DynamicOps<T> pDelegate, RegistryAccess pRegistryAccess)
    {
        super(pDelegate);
        this.registryAccess = pRegistryAccess;
    }

    protected <E> DataResult<T> encode(E pElement, T pPrefix, ResourceKey <? extends Registry<E >> pRegistryKey, Codec<E> pElementCodec)
    {
        Optional <? extends Registry<E >> optional = this.registryAccess.ownedRegistry(pRegistryKey);

        if (optional.isPresent())
        {
            Registry<E> registry = optional.get();
            Optional<ResourceKey<E>> optional1 = registry.getResourceKey(pElement);

            if (optional1.isPresent())
            {
                ResourceKey<E> resourcekey = optional1.get();
                return ResourceLocation.CODEC.encode(resourcekey.location(), this.delegate, pPrefix);
            }
        }

        return pElementCodec.encode(pElement, this, pPrefix);
    }
}
