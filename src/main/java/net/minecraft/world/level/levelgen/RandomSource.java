package net.minecraft.world.level.levelgen;

public interface RandomSource
{
    RandomSource fork();

    PositionalRandomFactory forkPositional();

    void setSeed(long pSeed);

    int nextInt();

    int nextInt(int pBound);

default int nextIntBetweenInclusive(int p_189321_, int p_189322_)
    {
        return this.nextInt(p_189322_ - p_189321_ + 1) + p_189321_;
    }

    long nextLong();

    boolean nextBoolean();

    float nextFloat();

    double nextDouble();

    double nextGaussian();

default void consumeCount(int pCount)
    {
        for (int i = 0; i < pCount; ++i)
        {
            this.nextInt();
        }
    }
}
