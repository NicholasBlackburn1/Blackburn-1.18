package net.minecraft.resources;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.Registry;

public final class RegistryFileCodec<E> implements Codec<Supplier<E>>
{
    private final ResourceKey <? extends Registry<E >> registryKey;
    private final Codec<E> elementCodec;
    private final boolean allowInline;

    public static <E> RegistryFileCodec<E> create(ResourceKey <? extends Registry<E >> pRegistryKey, Codec<E> pElementCodec)
    {
        return create(pRegistryKey, pElementCodec, true);
    }

    public static <E> Codec<List<Supplier<E>>> homogeneousList(ResourceKey <? extends Registry<E >> pRegistryKey, Codec<E> pElementCodec)
    {
        return Codec.either(create(pRegistryKey, pElementCodec, false).listOf(), pElementCodec.<Supplier<E>>xmap((p_135604_) ->
        {
            return () -> {
                return p_135604_;
            };
        }, Supplier::get).listOf()).xmap((p_135578_) ->
        {
            return p_135578_.map((p_179856_) -> {
                return p_179856_;
            }, (p_179852_) -> {
                return p_179852_;
            });
        }, Either::left);
    }

    private static <E> RegistryFileCodec<E> create(ResourceKey <? extends Registry<E >> pRegistryKey, Codec<E> pElementCodec, boolean pAllowInline)
    {
        return new RegistryFileCodec<>(pRegistryKey, pElementCodec, pAllowInline);
    }

    private RegistryFileCodec(ResourceKey <? extends Registry<E >> pRegistryKey, Codec<E> pElementCodec, boolean pAllowInline)
    {
        this.registryKey = pRegistryKey;
        this.elementCodec = pElementCodec;
        this.allowInline = pAllowInline;
    }

    public <T> DataResult<T> encode(Supplier<E> pInput, DynamicOps<T> pOps, T pPrefix)
    {
        return pOps instanceof RegistryWriteOps ? ((RegistryWriteOps)pOps).encode(pInput.get(), pPrefix, this.registryKey, this.elementCodec) : this.elementCodec.encode(pInput.get(), pOps, pPrefix);
    }

    public <T> DataResult<Pair<Supplier<E>, T>> decode(DynamicOps<T> pOps, T pInput)
    {
        return pOps instanceof RegistryReadOps ? ((RegistryReadOps)pOps).decodeElement(pInput, this.registryKey, this.elementCodec, this.allowInline) : this.elementCodec.decode(pOps, pInput).map((p_135580_) ->
        {
            return p_135580_.mapFirst((p_179850_) -> {
                return () -> {
                    return p_179850_;
                };
            });
        });
    }

    public String toString()
    {
        return "RegistryFileCodec[" + this.registryKey + " " + this.elementCodec + "]";
    }
}
