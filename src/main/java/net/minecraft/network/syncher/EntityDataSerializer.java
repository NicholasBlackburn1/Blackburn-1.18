package net.minecraft.network.syncher;

import net.minecraft.network.FriendlyByteBuf;

public interface EntityDataSerializer<T>
{
    void write(FriendlyByteBuf pBuffer, T pValue);

    T read(FriendlyByteBuf pBuffer);

default EntityDataAccessor<T> createAccessor(int pId)
    {
        return new EntityDataAccessor<>(pId, this);
    }

    T copy(T pValue);
}
