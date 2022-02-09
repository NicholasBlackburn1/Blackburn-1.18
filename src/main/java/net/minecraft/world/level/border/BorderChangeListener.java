package net.minecraft.world.level.border;

public interface BorderChangeListener
{
    void onBorderSizeSet(WorldBorder pBorder, double pSize);

    void onBorderSizeLerping(WorldBorder pBorder, double pOldSize, double p_61854_, long pNewSize);

    void onBorderCenterSet(WorldBorder pBorder, double pX, double p_61851_);

    void onBorderSetWarningTime(WorldBorder pBorder, int pWarningTime);

    void onBorderSetWarningBlocks(WorldBorder pBorder, int pWarningBlocks);

    void onBorderSetDamagePerBlock(WorldBorder pBorder, double pDamagePerBlock);

    void onBorderSetDamageSafeZOne(WorldBorder pBorder, double pDamageSafeZone);

    public static class DelegateBorderChangeListener implements BorderChangeListener
    {
        private final WorldBorder worldBorder;

        public DelegateBorderChangeListener(WorldBorder pWorldBorder)
        {
            this.worldBorder = pWorldBorder;
        }

        public void onBorderSizeSet(WorldBorder pBorder, double pNewSize)
        {
            this.worldBorder.setSize(pNewSize);
        }

        public void onBorderSizeLerping(WorldBorder pBorder, double pOldSize, double p_61877_, long pNewSize)
        {
            this.worldBorder.lerpSizeBetween(pOldSize, p_61877_, pNewSize);
        }

        public void onBorderCenterSet(WorldBorder pBorder, double pX, double p_61873_)
        {
            this.worldBorder.setCenter(pX, p_61873_);
        }

        public void onBorderSetWarningTime(WorldBorder pBorder, int pNewTime)
        {
            this.worldBorder.setWarningTime(pNewTime);
        }

        public void onBorderSetWarningBlocks(WorldBorder pBorder, int pNewDistance)
        {
            this.worldBorder.setWarningBlocks(pNewDistance);
        }

        public void onBorderSetDamagePerBlock(WorldBorder pBorder, double pNewAmount)
        {
            this.worldBorder.setDamagePerBlock(pNewAmount);
        }

        public void onBorderSetDamageSafeZOne(WorldBorder pBorder, double pNewSize)
        {
            this.worldBorder.setDamageSafeZone(pNewSize);
        }
    }
}
