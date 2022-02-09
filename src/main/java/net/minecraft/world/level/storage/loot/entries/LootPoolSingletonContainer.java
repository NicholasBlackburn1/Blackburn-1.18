package net.minecraft.world.level.storage.loot.entries;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.apache.commons.lang3.ArrayUtils;

public abstract class LootPoolSingletonContainer extends LootPoolEntryContainer
{
    public static final int DEFAULT_WEIGHT = 1;
    public static final int DEFAULT_QUALITY = 0;
    protected final int weight;
    protected final int quality;
    protected final LootItemFunction[] functions;
    final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
    private final LootPoolEntry entry = new LootPoolSingletonContainer.EntryBase()
    {
        public void createItemStack(Consumer<ItemStack> p_79700_, LootContext p_79701_)
        {
            LootPoolSingletonContainer.this.createItemStack(LootItemFunction.decorate(LootPoolSingletonContainer.this.compositeFunction, p_79700_, p_79701_), p_79701_);
        }
    };

    protected LootPoolSingletonContainer(int pWeight, int pQuality, LootItemCondition[] pConditions, LootItemFunction[] pFunctions)
    {
        super(pConditions);
        this.weight = pWeight;
        this.quality = pQuality;
        this.functions = pFunctions;
        this.compositeFunction = LootItemFunctions.a(pFunctions);
    }

    public void validate(ValidationContext pValidationContext)
    {
        super.validate(pValidationContext);

        for (int i = 0; i < this.functions.length; ++i)
        {
            this.functions[i].validate(pValidationContext.forChild(".functions[" + i + "]"));
        }
    }

    protected abstract void createItemStack(Consumer<ItemStack> pStackConsumer, LootContext pLootContext);

    public boolean expand(LootContext pLootContext, Consumer<LootPoolEntry> pEntryConsumer)
    {
        if (this.canRun(pLootContext))
        {
            pEntryConsumer.accept(this.entry);
            return true;
        }
        else
        {
            return false;
        }
    }

    public static LootPoolSingletonContainer.Builder<?> simpleBuilder(LootPoolSingletonContainer.EntryConstructor pEntryBuilder)
    {
        return new LootPoolSingletonContainer.DummyBuilder(pEntryBuilder);
    }

    public abstract static class Builder<T extends LootPoolSingletonContainer.Builder<T>> extends LootPoolEntryContainer.Builder<T> implements FunctionUserBuilder<T>
    {
        protected int weight = 1;
        protected int quality = 0;
        private final List<LootItemFunction> functions = Lists.newArrayList();

        public T apply(LootItemFunction.Builder pFunctionBuilder)
        {
            this.functions.add(pFunctionBuilder.build());
            return this.getThis();
        }

        protected LootItemFunction[] getFunctions()
        {
            return this.functions.toArray(new LootItemFunction[0]);
        }

        public T setWeight(int pWeight)
        {
            this.weight = pWeight;
            return this.getThis();
        }

        public T setQuality(int pQuality)
        {
            this.quality = pQuality;
            return this.getThis();
        }
    }

    static class DummyBuilder extends LootPoolSingletonContainer.Builder<LootPoolSingletonContainer.DummyBuilder>
    {
        private final LootPoolSingletonContainer.EntryConstructor constructor;

        public DummyBuilder(LootPoolSingletonContainer.EntryConstructor p_79717_)
        {
            this.constructor = p_79717_;
        }

        protected LootPoolSingletonContainer.DummyBuilder getThis()
        {
            return this;
        }

        public LootPoolEntryContainer build()
        {
            return this.constructor.build(this.weight, this.quality, this.getConditions(), this.getFunctions());
        }
    }

    protected abstract class EntryBase implements LootPoolEntry
    {
        public int getWeight(float pLuck)
        {
            return Math.max(Mth.floor((float)LootPoolSingletonContainer.this.weight + (float)LootPoolSingletonContainer.this.quality * pLuck), 0);
        }
    }

    @FunctionalInterface
    protected interface EntryConstructor
    {
        LootPoolSingletonContainer build(int p_79727_, int p_79728_, LootItemCondition[] p_79729_, LootItemFunction[] p_79730_);
    }

    public abstract static class Serializer<T extends LootPoolSingletonContainer> extends LootPoolEntryContainer.Serializer<T>
    {
        public void serializeCustom(JsonObject pObject, T pContext, JsonSerializationContext pConditions)
        {
            if (pContext.weight != 1)
            {
                pObject.addProperty("weight", pContext.weight);
            }

            if (pContext.quality != 0)
            {
                pObject.addProperty("quality", pContext.quality);
            }

            if (!ArrayUtils.isEmpty((Object[])pContext.functions))
            {
                pObject.add("functions", pConditions.serialize(pContext.functions));
            }
        }

        public final T b(JsonObject p_79733_, JsonDeserializationContext p_79734_, LootItemCondition[] p_79735_)
        {
            int i = GsonHelper.getAsInt(p_79733_, "weight", 1);
            int j = GsonHelper.getAsInt(p_79733_, "quality", 0);
            LootItemFunction[] alootitemfunction = GsonHelper.getAsObject(p_79733_, "functions", new LootItemFunction[0], p_79734_, LootItemFunction[].class);
            return this.b(p_79733_, p_79734_, i, j, p_79735_, alootitemfunction);
        }

        protected abstract T b(JsonObject p_79744_, JsonDeserializationContext p_79745_, int p_79746_, int p_79747_, LootItemCondition[] p_79748_, LootItemFunction[] p_79749_);
    }
}
