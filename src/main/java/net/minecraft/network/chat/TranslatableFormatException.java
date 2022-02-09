package net.minecraft.network.chat;

public class TranslatableFormatException extends IllegalArgumentException
{
    public TranslatableFormatException(TranslatableComponent pComponent, String pInvalidIndex)
    {
        super(String.format("Error parsing: %s: %s", pComponent, pInvalidIndex));
    }

    public TranslatableFormatException(TranslatableComponent pComponent, int pInvalidIndex)
    {
        super(String.format("Invalid index %d requested for %s", pInvalidIndex, pComponent));
    }

    public TranslatableFormatException(TranslatableComponent pComponent, Throwable pInvalidIndex)
    {
        super(String.format("Error while parsing: %s", pComponent), pInvalidIndex);
    }
}
