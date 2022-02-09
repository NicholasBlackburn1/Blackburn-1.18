package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LootItem extends LootPoolSingletonContainer
{
    final Item item;

    LootItem(Item pItem, int pWeight, int pQuality, LootItemCondition[] pConditions, LootItemFunction[] pFunctions)
    {
        super(pWeight, pQuality, pConditions, pFunctions);
        this.item = pItem;
    }

    public LootPoolEntryType getType()
    {
        return LootPoolEntries.ITEM;
    }

    public void createItemStack(Consumer<ItemStack> pStackConsumer, LootContext pLootContext)
    {
        pStackConsumer.accept(new ItemStack(this.item));
    }

    public static LootPoolSingletonContainer.Builder<?> lootTableItem(ItemLike pItem)
    {
        return simpleBuilder((p_79583_, p_79584_, p_79585_, p_79586_) ->
        {
            return new LootItem(pItem.asItem(), p_79583_, p_79584_, p_79585_, p_79586_);
        });
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<LootItem>
    {
        public void serializeCustom(JsonObject pObject, LootItem pContext, JsonSerializationContext pConditions)
        {
            super.serializeCustom(pObject, pContext, pConditions);
            ResourceLocation resourcelocation = Registry.ITEM.getKey(pContext.item);

            if (resourcelocation == null)
            {
                throw new IllegalArgumentException("Can't serialize unknown item " + pContext.item);
            }
            else
            {
                pObject.addProperty("name", resourcelocation.toString());
            }
        }

        protected LootItem b(JsonObject p_79594_, JsonDeserializationContext p_79595_, int p_79596_, int p_79597_, LootItemCondition[] p_79598_, LootItemFunction[] p_79599_)
        {
            Item item = GsonHelper.getAsItem(p_79594_, "name");
            return new LootItem(item, p_79596_, p_79597_, p_79598_, p_79599_);
        }
    }
}
