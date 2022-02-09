package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.apache.commons.lang3.ArrayUtils;

public abstract class LootItemConditionalFunction implements LootItemFunction
{
    protected final LootItemCondition[] predicates;
    private final Predicate<LootContext> compositePredicates;

    protected LootItemConditionalFunction(LootItemCondition[] pConditions)
    {
        this.predicates = pConditions;
        this.compositePredicates = LootItemConditions.a(pConditions);
    }

    public final ItemStack apply(ItemStack p_80689_, LootContext p_80690_)
    {
        return this.compositePredicates.test(p_80690_) ? this.run(p_80689_, p_80690_) : p_80689_;
    }

    protected abstract ItemStack run(ItemStack pStack, LootContext pContext);

    public void validate(ValidationContext pContext)
    {
        LootItemFunction.super.validate(pContext);

        for (int i = 0; i < this.predicates.length; ++i)
        {
            this.predicates[i].validate(pContext.forChild(".conditions[" + i + "]"));
        }
    }

    protected static LootItemConditionalFunction.Builder<?> simpleBuilder(Function<LootItemCondition[], LootItemFunction> pConstructor)
    {
        return new LootItemConditionalFunction.DummyBuilder(pConstructor);
    }

    public abstract static class Builder<T extends LootItemConditionalFunction.Builder<T>> implements LootItemFunction.Builder, ConditionUserBuilder<T>
    {
        private final List<LootItemCondition> conditions = Lists.newArrayList();

        public T when(LootItemCondition.Builder pConditionBuilder)
        {
            this.conditions.add(pConditionBuilder.build());
            return this.getThis();
        }

        public final T unwrap()
        {
            return this.getThis();
        }

        protected abstract T getThis();

        protected LootItemCondition[] getConditions()
        {
            return this.conditions.toArray(new LootItemCondition[0]);
        }
    }

    static final class DummyBuilder extends LootItemConditionalFunction.Builder<LootItemConditionalFunction.DummyBuilder>
    {
        private final Function<LootItemCondition[], LootItemFunction> constructor;

        public DummyBuilder(Function<LootItemCondition[], LootItemFunction> pConstructor)
        {
            this.constructor = pConstructor;
        }

        protected LootItemConditionalFunction.DummyBuilder getThis()
        {
            return this;
        }

        public LootItemFunction build()
        {
            return this.constructor.apply(this.getConditions());
        }
    }

    public abstract static class Serializer<T extends LootItemConditionalFunction> implements net.minecraft.world.level.storage.loot.Serializer<T>
    {
        public void serialize(JsonObject pJson, T pValue, JsonSerializationContext pSerializationContext)
        {
            if (!ArrayUtils.isEmpty((Object[])pValue.predicates))
            {
                pJson.add("conditions", pSerializationContext.serialize(pValue.predicates));
            }
        }

        public final T deserialize(JsonObject pJson, JsonDeserializationContext pSerializationContext)
        {
            LootItemCondition[] alootitemcondition = GsonHelper.getAsObject(pJson, "conditions", new LootItemCondition[0], pSerializationContext, LootItemCondition[].class);
            return this.b(pJson, pSerializationContext, alootitemcondition);
        }

        public abstract T b(JsonObject p_80721_, JsonDeserializationContext p_80722_, LootItemCondition[] p_80723_);
    }
}
