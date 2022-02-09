package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetNbtFunction extends LootItemConditionalFunction
{
    final CompoundTag tag;

    SetNbtFunction(LootItemCondition[] pConditions, CompoundTag pTag)
    {
        super(pConditions);
        this.tag = pTag;
    }

    public LootItemFunctionType getType()
    {
        return LootItemFunctions.SET_NBT;
    }

    public ItemStack run(ItemStack pStack, LootContext pContext)
    {
        pStack.getOrCreateTag().merge(this.tag);
        return pStack;
    }

    @Deprecated
    public static LootItemConditionalFunction.Builder<?> setTag(CompoundTag pTag)
    {
        return simpleBuilder((p_81191_) ->
        {
            return new SetNbtFunction(p_81191_, pTag);
        });
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetNbtFunction>
    {
        public void serialize(JsonObject pJson, SetNbtFunction pValue, JsonSerializationContext pSerializationContext)
        {
            super.serialize(pJson, pValue, pSerializationContext);
            pJson.addProperty("tag", pValue.tag.toString());
        }

        public SetNbtFunction b(JsonObject p_81195_, JsonDeserializationContext p_81196_, LootItemCondition[] p_81197_)
        {
            try
            {
                CompoundTag compoundtag = TagParser.parseTag(GsonHelper.getAsString(p_81195_, "tag"));
                return new SetNbtFunction(p_81197_, compoundtag);
            }
            catch (CommandSyntaxException commandsyntaxexception)
            {
                throw new JsonSyntaxException(commandsyntaxexception.getMessage());
            }
        }
    }
}
