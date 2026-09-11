package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.utils.RotatableBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class EmptyPlanterBlock extends TemplateBlock
        implements RotatableBlock {

    public EmptyPlanterBlock(Identifier iid) {
        super(iid, Material.GLASS);
        textureId = 77;
        setHardness(0.6F);
        setSoundGroup(GLASS_SOUND_GROUP);
        setTickRandomly(true);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int getFacing(BlockView iBlockAccess, int i, int j, int l) {
        return 0;
    }

    @Override
    public void setFacing(World world1, int l, int i1, int j1, int k1) {
    }

    @Override
    public boolean canRotate(BlockView iBlockAccess, int i, int j, int l) {
        return false;
    }

    @Override
    public boolean canTransmitRotation(BlockView iBlockAccess, int i, int j, int l) {
        return false;
    }

    @Override
    public void rotate(World world1, int l, int i1, int j1, boolean flag) {
    }

    public static final float m_fPlanterWidth = 0.75F;
    public static final float m_fPlanterHalfWidth = 0.375F;
    public static final float m_fPlanterBandHeight = 0.3125F;
    public static final float m_fPlanterBandHalfHeight = 0.15625F;
    private final int iPlanterDirtTextureIndex = 78;

}
