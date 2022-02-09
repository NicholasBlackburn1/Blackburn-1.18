package net.minecraft.client.model.geom.builders;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

public class CubeListBuilder
{
    private final List<CubeDefinition> cubes = Lists.newArrayList();
    private int xTexOffs;
    private int yTexOffs;
    private boolean mirror;

    public CubeListBuilder texOffs(int pXTexOffs, int pYTexOffs)
    {
        this.xTexOffs = pXTexOffs;
        this.yTexOffs = pYTexOffs;
        return this;
    }

    public CubeListBuilder mirror()
    {
        return this.mirror(true);
    }

    public CubeListBuilder mirror(boolean pMirror)
    {
        this.mirror = pMirror;
        return this;
    }

    public CubeListBuilder addBox(String pComment, float pOriginX, float pOriginY, float pOriginZ, int pDimensionX, int pDimensionY, int pDimensionZ, CubeDeformation pCubeDeformation, int pXTexOffs, int pYTexOffs)
    {
        this.texOffs(pXTexOffs, pYTexOffs);
        this.cubes.add(new CubeDefinition(pComment, (float)this.xTexOffs, (float)this.yTexOffs, pOriginX, pOriginY, pOriginZ, (float)pDimensionX, (float)pDimensionY, (float)pDimensionZ, pCubeDeformation, this.mirror, 1.0F, 1.0F));
        return this;
    }

    public CubeListBuilder addBox(String pOriginX, float pOriginY, float pOriginZ, float pDimensionX, int pDimensionY, int pDimensionZ, int pCubeDeformation, int pTexScaleU, int pTexScaleV)
    {
        this.texOffs(pTexScaleU, pTexScaleV);
        this.cubes.add(new CubeDefinition(pOriginX, (float)this.xTexOffs, (float)this.yTexOffs, pOriginY, pOriginZ, pDimensionX, (float)pDimensionY, (float)pDimensionZ, (float)pCubeDeformation, CubeDeformation.NONE, this.mirror, 1.0F, 1.0F));
        return this;
    }

    public CubeListBuilder addBox(float pOriginX, float pOriginY, float pOriginZ, float pDimensionX, float pDimensionY, float pDimensionZ)
    {
        this.cubes.add(new CubeDefinition((String)null, (float)this.xTexOffs, (float)this.yTexOffs, pOriginX, pOriginY, pOriginZ, pDimensionX, pDimensionY, pDimensionZ, CubeDeformation.NONE, this.mirror, 1.0F, 1.0F));
        return this;
    }

    public CubeListBuilder addBox(String pOriginX, float pOriginY, float pOriginZ, float pDimensionX, float pDimensionY, float pDimensionZ, float pCubeDeformation)
    {
        this.cubes.add(new CubeDefinition(pOriginX, (float)this.xTexOffs, (float)this.yTexOffs, pOriginY, pOriginZ, pDimensionX, pDimensionY, pDimensionZ, pCubeDeformation, CubeDeformation.NONE, this.mirror, 1.0F, 1.0F));
        return this;
    }

    public CubeListBuilder addBox(String pComment, float pOriginX, float pOriginY, float pOriginZ, float pDimensionX, float pDimensionY, float pDimensionZ, CubeDeformation pCubeDeformation)
    {
        this.cubes.add(new CubeDefinition(pComment, (float)this.xTexOffs, (float)this.yTexOffs, pOriginX, pOriginY, pOriginZ, pDimensionX, pDimensionY, pDimensionZ, pCubeDeformation, this.mirror, 1.0F, 1.0F));
        return this;
    }

    public CubeListBuilder addBox(float pOriginX, float pOriginY, float pOriginZ, float pDimensionX, float pDimensionY, float pDimensionZ, boolean pCubeDeformation)
    {
        this.cubes.add(new CubeDefinition((String)null, (float)this.xTexOffs, (float)this.yTexOffs, pOriginX, pOriginY, pOriginZ, pDimensionX, pDimensionY, pDimensionZ, CubeDeformation.NONE, pCubeDeformation, 1.0F, 1.0F));
        return this;
    }

    public CubeListBuilder addBox(float pOriginX, float pOriginY, float pOriginZ, float pDimensionX, float pDimensionY, float pDimensionZ, CubeDeformation pCubeDeformation, float pTexScaleU, float pTexScaleV)
    {
        this.cubes.add(new CubeDefinition((String)null, (float)this.xTexOffs, (float)this.yTexOffs, pOriginX, pOriginY, pOriginZ, pDimensionX, pDimensionY, pDimensionZ, pCubeDeformation, this.mirror, pTexScaleU, pTexScaleV));
        return this;
    }

    public CubeListBuilder addBox(float pOriginX, float pOriginY, float pOriginZ, float pDimensionX, float pDimensionY, float pDimensionZ, CubeDeformation pCubeDeformation)
    {
        this.cubes.add(new CubeDefinition((String)null, (float)this.xTexOffs, (float)this.yTexOffs, pOriginX, pOriginY, pOriginZ, pDimensionX, pDimensionY, pDimensionZ, pCubeDeformation, this.mirror, 1.0F, 1.0F));
        return this;
    }

    public List<CubeDefinition> getCubes()
    {
        return ImmutableList.copyOf(this.cubes);
    }

    public static CubeListBuilder create()
    {
        return new CubeListBuilder();
    }
}
