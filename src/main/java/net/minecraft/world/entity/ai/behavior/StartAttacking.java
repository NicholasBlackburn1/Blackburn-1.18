package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class StartAttacking<E extends Mob> extends Behavior<E>
{
    private final Predicate<E> canAttackPredicate;
    private final Function < E, Optional <? extends LivingEntity >> targetFinderFunction;

    public StartAttacking(Predicate<E> p_24195_, Function < E, Optional <? extends LivingEntity >> p_24196_)
    {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED));
        this.canAttackPredicate = p_24195_;
        this.targetFinderFunction = p_24196_;
    }

    public StartAttacking(Function < E, Optional <? extends LivingEntity >> p_24193_)
    {
        this((p_24212_) ->
        {
            return true;
        }, p_24193_);
    }

    protected boolean checkExtraStartConditions(ServerLevel pLevel, E pOwner)
    {
        if (!this.canAttackPredicate.test(pOwner))
        {
            return false;
        }
        else
        {
            Optional <? extends LivingEntity > optional = this.targetFinderFunction.apply(pOwner);
            return optional.isPresent() ? pOwner.canAttack(optional.get()) : false;
        }
    }

    protected void start(ServerLevel pLevel, E pEntity, long pGameTime)
    {
        this.targetFinderFunction.apply(pEntity).ifPresent((p_24218_) ->
        {
            this.setAttackTarget(pEntity, p_24218_);
        });
    }

    private void setAttackTarget(E p_24214_, LivingEntity p_24215_)
    {
        p_24214_.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, p_24215_);
        p_24214_.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }
}
