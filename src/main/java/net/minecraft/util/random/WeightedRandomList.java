package net.minecraft.util.random;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class WeightedRandomList<E extends WeightedEntry>
{
    private final int totalWeight;
    private final ImmutableList<E> items;

    WeightedRandomList(List <? extends E > pItems)
    {
        this.items = ImmutableList.copyOf(pItems);
        this.totalWeight = WeightedRandom.getTotalWeight(pItems);
    }

    public static <E extends WeightedEntry> WeightedRandomList<E> create()
    {
        return new WeightedRandomList<>(ImmutableList.of());
    }

    @SafeVarargs
    public static <E extends WeightedEntry> WeightedRandomList<E> a(E... p_146331_)
    {
        return new WeightedRandomList<>(ImmutableList.copyOf(p_146331_));
    }

    public static <E extends WeightedEntry> WeightedRandomList<E> create(List<E> pItems)
    {
        return new WeightedRandomList<>(pItems);
    }

    public boolean isEmpty()
    {
        return this.items.isEmpty();
    }

    public Optional<E> getRandom(Random pRandom)
    {
        if (this.totalWeight == 0)
        {
            return Optional.empty();
        }
        else
        {
            int i = pRandom.nextInt(this.totalWeight);
            return WeightedRandom.getWeightedItem(this.items, i);
        }
    }

    public List<E> unwrap()
    {
        return this.items;
    }

    public static <E extends WeightedEntry> Codec<WeightedRandomList<E>> codec(Codec<E> pElementCodec)
    {
        return pElementCodec.listOf().xmap(WeightedRandomList::create, WeightedRandomList::unwrap);
    }
}
