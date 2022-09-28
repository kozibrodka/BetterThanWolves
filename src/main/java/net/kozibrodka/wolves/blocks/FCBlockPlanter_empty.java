package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCIBlock;
import net.kozibrodka.wolves.utils.FCISoil;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;

public class FCBlockPlanter_empty extends TemplateBlockBase
    implements FCIBlock
{

    public FCBlockPlanter_empty(Identifier iid)
    {
        super(iid, Material.GLASS);
        texture = 77;
        setHardness(0.6F);
        setSounds(GLASS_SOUNDS);
        setTicksRandomly(true);
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int l)
    {
        return 0;
    }

    public void SetFacing(Level world1, int l, int i1, int j1, int k1)
    {
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public void Rotate(Level world1, int l, int i1, int j1, boolean flag)
    {
    }

    public static final float m_fPlanterWidth = 0.75F;
    public static final float m_fPlanterHalfWidth = 0.375F;
    public static final float m_fPlanterBandHeight = 0.3125F;
    public static final float m_fPlanterBandHalfHeight = 0.15625F;
    private final int iPlanterDirtTextureIndex = 78;

}
