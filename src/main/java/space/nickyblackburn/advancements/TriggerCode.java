package space.nickyblackburn.advancements;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class TriggerCode extends SimpleCriterionTrigger<TriggerCode.TriggerInstance> {
    public static final ResourceLocation ID = new ResourceLocation("blackburn");
 
    public ResourceLocation getId() {
       return ID;
    }
 
    public TriggerCode.TriggerInstance createInstance(JsonObject p_70644_, EntityPredicate.Composite p_70645_, DeserializationContext p_70646_) {
       return new TriggerCode.TriggerInstance(p_70645_);
    }
 
    public void trigger(ServerPlayer p_70642_) {
       this.trigger(p_70642_, (p_70648_) -> {
          return true;
       });
    }
 
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
       public TriggerInstance(EntityPredicate.Composite p_70654_) {
          super(TriggerCode.ID, p_70654_);
       }
    }
 }