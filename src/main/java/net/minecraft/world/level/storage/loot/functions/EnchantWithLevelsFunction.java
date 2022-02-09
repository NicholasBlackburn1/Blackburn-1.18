package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class EnchantWithLevelsFunction extends LootItemConditionalFunction
{
    final NumberProvider levels;
    final boolean treasure;

    EnchantWithLevelsFunction(LootItemCondition[] pConditions, NumberProvider pLevels, boolean pTreasure)
    {
        super(pConditions);
        this.levels = pLevels;
        this.treasure = pTreasure;
    }

    public LootItemFunctionType getType()
    {
        return LootItemFunctions.ENCHANT_WITH_LEVELS;
    }

    public Set < LootContextParam<? >> getReferencedContextParams()
    {
        return this.levels.getReferencedContextParams();
    }

    public ItemStack run(ItemStack pStack, LootContext pContext)
    {
        Random random = pContext.getRandom();
        return EnchantmentHelper.enchantItem(random, pStack, this.levels.getInt(pContext), this.treasure);
    }

    public static EnchantWithLevelsFunction.Builder enchantWithLevels(NumberProvider p_165197_)
    {
        return new EnchantWithLevelsFunction.Builder(p_165197_);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<EnchantWithLevelsFunction.Builder>
    {
        private final NumberProvider levels;
        private boolean treasure;

        public Builder(NumberProvider pLevels)
        {
            this.levels = pLevels;
        }

        protected EnchantWithLevelsFunction.Builder getThis()
        {
            return this;
        }

        public EnchantWithLevelsFunction.Builder allowTreasure()
        {
            this.treasure = true;
            return this;
        }

        public LootItemFunction build()
        {
            return new EnchantWithLevelsFunction(this.getConditions(), this.levels, this.treasure);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<EnchantWithLevelsFunction>
    {
        public void serialize(JsonObject pJson, EnchantWithLevelsFunction pValue, JsonSerializationContext pSerializationContext)
        {
            super.serialize(pJson, pValue, pSerializationContext);
            pJson.add("levels", pSerializationContext.serialize(pValue.levels));
            pJson.addProperty("treasure", pValue.treasure);
        }

        public EnchantWithLevelsFunction b(JsonObject p_80502_, JsonDeserializationContext p_80503_, LootItemCondition[] p_80504_)
        {
            NumberProvider numberprovider = GsonHelper.getAsObject(p_80502_, "levels", p_80503_, NumberProvider.class);
            boolean flag = GsonHelper.getAsBoolean(p_80502_, "treasure", false);
            return new EnchantWithLevelsFunction(p_80504_, numberprovider, flag);
        }
    }
}
