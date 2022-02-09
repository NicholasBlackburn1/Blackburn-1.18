package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.monster.Zombie;

public class ZombieAttackGoal extends MeleeAttackGoal
{
    private final Zombie zombie;
    private int raiseArmTicks;

    public ZombieAttackGoal(Zombie pZombie, double pSpeedModifier, boolean p_26021_)
    {
        super(pZombie, pSpeedModifier, p_26021_);
        this.zombie = pZombie;
    }

    public void start()
    {
        super.start();
        this.raiseArmTicks = 0;
    }

    public void stop()
    {
        super.stop();
        this.zombie.setAggressive(false);
    }

    public void tick()
    {
        super.tick();
        ++this.raiseArmTicks;

        if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2)
        {
            this.zombie.setAggressive(true);
        }
        else
        {
            this.zombie.setAggressive(false);
        }
    }
}
