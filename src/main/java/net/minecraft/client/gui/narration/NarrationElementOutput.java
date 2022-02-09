package net.minecraft.client.gui.narration;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;

public interface NarrationElementOutput
{
default void add(NarratedElementType pType, Component pContents)
    {
        this.add(pType, NarrationThunk.from(pContents.getString()));
    }

default void add(NarratedElementType pType, String pContents)
    {
        this.add(pType, NarrationThunk.from(pContents));
    }

default void a(NarratedElementType p_169150_, Component... p_169151_)
    {
        this.add(p_169150_, NarrationThunk.from(ImmutableList.copyOf(p_169151_)));
    }

    void add(NarratedElementType pType, NarrationThunk<?> pContents);

    NarrationElementOutput nest();
}
