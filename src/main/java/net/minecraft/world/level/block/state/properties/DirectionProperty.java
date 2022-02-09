package net.minecraft.world.level.block.state.properties;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.Direction;

public class DirectionProperty extends EnumProperty<Direction>
{
    protected DirectionProperty(String pName, Collection<Direction> pValues)
    {
        super(pName, Direction.class, pValues);
    }

    public static DirectionProperty create(String pName)
    {
        return create(pName, (p_187558_) ->
        {
            return true;
        });
    }

    public static DirectionProperty create(String pName, Predicate<Direction> pValues)
    {
        return create(pName, Arrays.stream(Direction.values()).filter(pValues).collect(Collectors.toList()));
    }

    public static DirectionProperty a(String p_61550_, Direction... p_61551_)
    {
        return create(p_61550_, Lists.newArrayList(p_61551_));
    }

    public static DirectionProperty create(String pName, Collection<Direction> pValues)
    {
        return new DirectionProperty(pName, pValues);
    }
}
