package net.minecraft.gametest.framework;

import java.util.function.Consumer;
import net.minecraft.world.level.block.Rotation;

public class TestFunction
{
    private final String batchName;
    private final String testName;
    private final String structureName;
    private final boolean required;
    private final int maxAttempts;
    private final int requiredSuccesses;
    private final Consumer<GameTestHelper> function;
    private final int maxTicks;
    private final long setupTicks;
    private final Rotation rotation;

    public TestFunction(String pBatchName, String pTestName, String pStructureName, int pMaxTicks, long pSetupTicks, boolean p_177806_, Consumer<GameTestHelper> pRequired)
    {
        this(pBatchName, pTestName, pStructureName, Rotation.NONE, pMaxTicks, pSetupTicks, p_177806_, 1, 1, pRequired);
    }

    public TestFunction(String pBatchName, String pTestName, String pStructureName, Rotation pRotation, int pMaxTicks, long pSetupTicks, boolean p_177826_, Consumer<GameTestHelper> pRequired)
    {
        this(pBatchName, pTestName, pStructureName, pRotation, pMaxTicks, pSetupTicks, p_177826_, 1, 1, pRequired);
    }

    public TestFunction(String pBatchName, String pTestName, String pStructureName, Rotation pRotation, int pMaxTicks, long pSetupTicks, boolean p_177815_, int pRequired, int pRequiredSuccesses, Consumer<GameTestHelper> pMaxAttempts)
    {
        this.batchName = pBatchName;
        this.testName = pTestName;
        this.structureName = pStructureName;
        this.rotation = pRotation;
        this.maxTicks = pMaxTicks;
        this.required = p_177815_;
        this.requiredSuccesses = pRequired;
        this.maxAttempts = pRequiredSuccesses;
        this.function = pMaxAttempts;
        this.setupTicks = pSetupTicks;
    }

    public void run(GameTestHelper pGameTestHelper)
    {
        this.function.accept(pGameTestHelper);
    }

    public String getTestName()
    {
        return this.testName;
    }

    public String getStructureName()
    {
        return this.structureName;
    }

    public String toString()
    {
        return this.testName;
    }

    public int getMaxTicks()
    {
        return this.maxTicks;
    }

    public boolean isRequired()
    {
        return this.required;
    }

    public String getBatchName()
    {
        return this.batchName;
    }

    public long getSetupTicks()
    {
        return this.setupTicks;
    }

    public Rotation getRotation()
    {
        return this.rotation;
    }

    public boolean isFlaky()
    {
        return this.maxAttempts > 1;
    }

    public int getMaxAttempts()
    {
        return this.maxAttempts;
    }

    public int getRequiredSuccesses()
    {
        return this.requiredSuccesses;
    }
}
