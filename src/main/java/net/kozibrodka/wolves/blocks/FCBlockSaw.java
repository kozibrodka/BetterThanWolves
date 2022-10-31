package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.mixin.LevelAccessor;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.kozibrodka.wolves.utils.FCIBlock;
import net.kozibrodka.wolves.utils.FCMechanicalDevice;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;

import java.util.List;
import java.util.Random;

public class FCBlockSaw extends TemplateBlockBase
    implements FCMechanicalDevice, FCIBlock
{

    public FCBlockSaw(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
        texture = 57;
        setTicksRandomly(true);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        setDefaultState(getDefaultState()
                .with(FACING, 0)
                .with(POWER, false)
        );
    }

    public int getTickrate()
    {
        return iSawTickRate;
    }

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        SetFacing(world, i, j, k, FCUtilsMisc.GetOppositeFacing(iFacing));
    }

    public void afterPlaced(Level world, int i, int j, int k, Living entityLiving)
    {
        int iFacing = FCUtilsMisc.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
        SetFacing(world, i, j, k, iFacing);
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, id, getTickrate());
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        int iFacing = GetFacing(world, i, j, k);
        switch(iFacing)
        {
        case 0: // '\0'
            return Box.createButWasteMemory((float)i, ((float)j + 1.0F) - 0.75F, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);

        case 1: // '\001'
            return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 0.75F, (float)k + 1.0F);

        case 2: // '\002'
            return Box.createButWasteMemory((float)i, (float)j, ((float)k + 1.0F) - 0.75F, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);

        case 3: // '\003'
            return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 0.75F);

        case 4: // '\004'
            return Box.createButWasteMemory(((float)i + 1.0F) - 0.75F, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);
        }
        return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 0.75F, (float)j + 1.0F, (float)k + 1.0F);
    }

    public void updateBoundingBox(BlockView iblockaccess, int i, int j, int k)
    {
        int iFacing = GetFacing(iblockaccess, i, j, k);
        switch(iFacing)
        {
        case 0: // '\0'
            setBoundingBox(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;

        case 1: // '\001'
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
            break;

        case 2: // '\002'
            setBoundingBox(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
            break;

        case 3: // '\003'
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
            break;

        case 4: // '\004'
            setBoundingBox(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;

        default:
            setBoundingBox(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
            break;
        }
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        if(iid == mod_FCBetterThanWolves.fcAxleBlock.id || iid == mod_FCBetterThanWolves.fcHandCrank.id)
        {
            world.method_216(i, j, k, id, getTickrate());
        } else
        {
            world.method_216(i, j, k, id, getTickrate() + world.rand.nextInt(6));
        }
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bOn = IsBlockOn(world, i, j, k);
        if(bOn != bReceivingPower)
        {
             world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
            EmitSawParticles(world, i, j, k, random);
            SetBlockOn(world, i, j, k, bReceivingPower);
            if(bReceivingPower)
            {
                world.method_216(i, j, k, id, getTickrate() + random.nextInt(6));
            }
        } else
        if(bOn)
        {
            int iFacing = GetFacing(world, i, j, k);
            FCBlockPos targetPos = new FCBlockPos(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            if(!AttemptToSawBlock(world, targetPos.i, targetPos.j, targetPos.k, random, iFacing))
            {
                BreakSaw(world, i, j, k);
            }
        }
    }

    public void randomDisplayTick(Level world, int i, int j, int k, Random random)
    {
        if(IsBlockOn(world, i, j, k))
        {
            EmitSawParticles(world, i, j, k, random);
        }
    }

    public void onEntityCollision(Level world, int i, int j, int k, EntityBase entity)
    {
        if(IsBlockOn(world, i, j, k) && (entity instanceof Living))
        {
            int iFacing = GetFacing(world, i, j, k);
            float fHalfLength = 0.3125F;
            float fHalfWidth = 0.0078125F;
            float fBlockHeight = 0.25F;
            Box sawBox;
            switch(iFacing)
            {
            case 0: // '\0'
                sawBox = Box.createButWasteMemory(0.5F - fHalfLength, 0.0D, 0.5F - fHalfWidth, 0.5F + fHalfLength, fBlockHeight, 0.5F + fHalfWidth);
                break;

            case 1: // '\001'
                sawBox = Box.createButWasteMemory(0.5F - fHalfLength, 1.0F - fBlockHeight, 0.5F - fHalfWidth, 0.5F + fHalfLength, 1.0D, 0.5F + fHalfWidth);
                break;

            case 2: // '\002'
                sawBox = Box.createButWasteMemory(0.5F - fHalfLength, 0.5F - fHalfWidth, 0.0D, 0.5F + fHalfLength, 0.5F + fHalfWidth, fBlockHeight);
                break;

            case 3: // '\003'
                sawBox = Box.createButWasteMemory(0.5F - fHalfLength, 0.5F - fHalfWidth, 1.0F - fBlockHeight, 0.5F + fHalfLength, 0.5F + fHalfWidth, 1.0D);
                break;

            case 4: // '\004'
                sawBox = Box.createButWasteMemory(0.0D, 0.5F - fHalfWidth, 0.5F - fHalfLength, fBlockHeight, 0.5F + fHalfWidth, 0.5F + fHalfLength);
                break;

            default:
                sawBox = Box.createButWasteMemory(1.0F - fBlockHeight, 0.5F - fHalfWidth, 0.5F - fHalfLength, 1.0D, 0.5F + fHalfWidth, 0.5F + fHalfLength);
                break;
            }
            sawBox = sawBox.method_98(i, j, k);
            List collisionList = null;
            collisionList = world.getEntities(Living.class, sawBox);
            if(collisionList != null && collisionList.size() > 0)
            {
                for(int iTempListIndex = 0; iTempListIndex < collisionList.size(); iTempListIndex++)
                {
                    Living tempTargetEntity = (Living)collisionList.get(iTempListIndex);
                    if(tempTargetEntity.damage(null, 4))
                    {
                         world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
                        EmitBloodParticles(world, i, j, k, world.rand);
                    }
                }

            }
        }
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        Level level = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).level;
        if(level.getTileId(i,j,k) == mod_FCBetterThanWolves.fcSaw.id) {
            return (level.getBlockState(i, j, k).get(FACING));
        }else{
            return 0;
        }
//        return iBlockAccess.getTileMeta(i, j, k) & 7;
    }

    public void SetFacing(Level world, int i, int j, int k, int iFacing)
    {
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(FACING, iFacing));
//        int iMetaData = world.getTileMeta(i, j, k) & -8;
//        iMetaData |= iFacing;
//        world.setTileMeta(i, j, k, iMetaData);
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int k)
    {
        int iFacing = GetFacing(iBlockAccess, i, j, k);
        return iFacing != 0;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int k)
    {
        int iFacing = GetFacing(iBlockAccess, i, j, k);
        return iFacing != 0 && iFacing != 1;
    }

    public void Rotate(Level world, int i, int j, int k, boolean bReverse)
    {
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = FCUtilsMisc.RotateFacingAroundJ(iFacing, bReverse);
        if(iNewFacing != iFacing)
        {
            SetFacing(world, i, j, k, iNewFacing);
            world.method_202(i, j, k, i, j, k);
            world.method_216(i, j, k, id, getTickrate());
            ((LevelAccessor) world).invokeMethod_235(i, j, k, id);
        }
        FCUtilsMisc.DestroyHorizontallyAttachedAxles(world, i, j, k);
    }

    public boolean IsBlockOn(BlockView iBlockAccess, int i, int j, int k)
    {
        Level level = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).level;
        if(level.getTileId(i,j,k) == mod_FCBetterThanWolves.fcSaw.id) {
            return (level.getBlockState(i, j, k).get(POWER));
        }else{
            return false;
        }
//        return (iBlockAccess.getTileMeta(i, j, k) & 8) > 0;
    }

    public void SetBlockOn(Level world, int i, int j, int k, boolean bOn)
    {
//        int iMetaData = world.getTileMeta(i, j, k) & 7;
//        if(bOn)
//        {
//            iMetaData |= 8;
//        }
//        world.setTileMeta(i, j, k, iMetaData);
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(POWER, bOn));
    }

    void EmitSawParticles(Level world, int i, int j, int k, Random random)
    {
        int iFacing = GetFacing(world, i, j, k);
        float fBladeXPos = i;
        float fBladeYPos = j;
        float fBladeZPos = k;
        float fBladeXExtent = 0.0F;
        float fBladeZExtent = 0.0F;
        switch(iFacing)
        {
        case 0: // '\0'
            fBladeXPos += 0.5F;
            fBladeZPos += 0.5F;
            fBladeXExtent = 1.0F;
            break;

        case 1: // '\001'
            fBladeXPos += 0.5F;
            fBladeZPos += 0.5F;
            fBladeYPos++;
            fBladeXExtent = 1.0F;
            break;

        case 2: // '\002'
            fBladeXPos += 0.5F;
            fBladeYPos += 0.5F;
            fBladeXExtent = 1.0F;
            break;

        case 3: // '\003'
            fBladeXPos += 0.5F;
            fBladeYPos += 0.5F;
            fBladeZPos++;
            fBladeXExtent = 1.0F;
            break;

        case 4: // '\004'
            fBladeYPos += 0.5F;
            fBladeZPos += 0.5F;
            fBladeZExtent = 1.0F;
            break;

        default:
            fBladeYPos += 0.5F;
            fBladeZPos += 0.5F;
            fBladeXPos++;
            fBladeZExtent = 1.0F;
            break;
        }
        for(int counter = 0; counter < 5; counter++)
        {
            float smokeX = fBladeXPos + (random.nextFloat() - 0.5F) * fBladeXExtent;
            float smokeY = fBladeYPos + random.nextFloat() * 0.1F;
            float smokeZ = fBladeZPos + (random.nextFloat() - 0.5F) * fBladeZExtent;
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    void EmitBloodParticles(Level world, int i, int j, int k, Random random)
    {
        int iFacing = GetFacing(world, i, j, k);
        FCBlockPos iTargetPos = new FCBlockPos(i, j, k);
        iTargetPos.AddFacingAsOffset(iFacing);
        for(int counter = 0; counter < 10; counter++)
        {
            float smokeX = (float)iTargetPos.i + random.nextFloat();
            float smokeY = (float)iTargetPos.j + random.nextFloat();
            float smokeZ = (float)iTargetPos.k + random.nextFloat();
            world.addParticle("reddust", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    boolean AttemptToSawBlock(Level world, int i, int j, int k, Random random, int iSawFacing)
    {
        if(!world.isAir(i, j, k))
        {
            int iTargetid = world.getTileId(i, j, k);
            boolean bSawedBlock = false;
            BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
            boolean bRemoveOriginalBlockIfSawed = true;
            //TODO: logic change Wood Types Blocks/BlockBase A LOT!
            if(iTargetid == Blocks.OAK_LOG.id) //if(iTargetid == BlockBase.LOG.id)
            {
                for(int iTempCount = 0; iTempCount < 4; iTempCount++)
                {
                    FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, BlockBase.WOOD.id, 0);
                }

                bSawedBlock = true;
            } else
            if(iTargetid == BlockBase.WOOD.id)
            {
                for(int iTempCount = 0; iTempCount < 2; iTempCount++)
                {
                    FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcPanel_wood.id, 1);
                }

                bSawedBlock = true;
            } else
            if(iTargetid == mod_FCBetterThanWolves.fcPanel_wood.id)
            {
                    for(int iTempCount = 0; iTempCount < 2; iTempCount++)
                    {
                        FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcMoulding_wood.id, 0);
                    }

                    bSawedBlock = true;
            } else
            if(iTargetid == mod_FCBetterThanWolves.fcMoulding_wood.id)
            {
                for(int iTempCount = 0; iTempCount < 2; iTempCount++)
                {
                    FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcCorner_wood.id, 0);
                }

                bSawedBlock = true;
            } else
            if(iTargetid == mod_FCBetterThanWolves.fcCorner_wood.id)
            {
                for(int iTempCount = 0; iTempCount < 2; iTempCount++)
                {
                    FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcGear.id, 0);
                }

                bSawedBlock = true;
            } else
            if(iTargetid == mod_FCBetterThanWolves.fcCompanionCube_slab.id){
                if(iSawFacing == 0 || iSawFacing == 1)
                {
                    FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcCompanionCube_slab.id, 1);
                    bSawedBlock = true;
                }
            } else
            if(iTargetid == mod_FCBetterThanWolves.fcCompanionCube.id){
                FCBlockCompanionCube cubeBlock = (FCBlockCompanionCube)mod_FCBetterThanWolves.fcCompanionCube;
                if(iSawFacing == 0 || iSawFacing == 1)
                {
                    for(int iTempCount = 0; iTempCount < 2; iTempCount++)
                    {
                        FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcCompanionCube_slab.id, 1);
                    }
                }else {
                    int faceOLd = cubeBlock.GetFacing(world,i,j,k);
                    world.setTile(i,j,k, mod_FCBetterThanWolves.fcCompanionCube_slab.id);
                    FCBlockCompanionSlab slabBlock = (FCBlockCompanionSlab)mod_FCBetterThanWolves.fcCompanionCube_slab;
                    slabBlock.SetFacing(world,i,j,k, faceOLd);

                    FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcCompanionCube_slab.id, 1);
                    world.method_202(i, j, k, i, j, k);
                    bRemoveOriginalBlockIfSawed = false;
                }
                    FCBlockPos bloodPos = new FCBlockPos(i, j, k);
                    bloodPos.AddFacingAsOffset(FCUtilsMisc.GetOppositeFacing(iSawFacing));
                    EmitBloodParticles(world, bloodPos.i, bloodPos.j, bloodPos.k, world.rand);
                    world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.wolf.hurt", 5F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                    bSawedBlock = true;

            } else
//            if(iTargetid == BlockBase.SUGAR_CANES.id) //TODO debug code?? cyhba useless
//            {
//                targetBlock.drop(world, i, j, k, world.getTileMeta(i, j, k));
//                bSawedBlock = true;
//            } else
            if(iTargetid != BlockBase.PISTON_HEAD.id && targetBlock != null)
            {
                Material targetMaterial = targetBlock.material;
                if(targetMaterial != Material.WOOD)
                {
                    if(targetMaterial.isSolid())
                    {
                        return false;
                    }
                } else
                {
                    targetBlock.drop(world, i, j, k, world.getTileMeta(i, j, k));
                    bSawedBlock = true;
                }
            }
            if(bSawedBlock)
            {
                 world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
                EmitSawParticles(world, i, j, k, random);
                if(bRemoveOriginalBlockIfSawed)
                {
                    world.setTile(i, j, k, 0);
                }
            }
        }
        return true;
    }

    public void BreakSaw(Level world, int i, int j, int k)
    {
        for(int iTemp = 0; iTemp < 2; iTemp++)
        {
            FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcGear.id, 0);
        }

        for(int iTemp = 0; iTemp < 2; iTemp++)
        {
            FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, BlockBase.WOOD.id, 0);
        }

        for(int iTemp = 0; iTemp < 2; iTemp++)
        {
            FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, ItemBase.ironIngot.id, 0);
        }

        for(int iTemp = 0; iTemp < 1; iTemp++)
        {
            FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcBelt.id, 0);
        }

         world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
        world.setTile(i, j, k, 0);
    }

    public boolean CanOutputMechanicalPower()
    {
        return false;
    }

    public boolean CanInputMechanicalPower()
    {
        return true;
    }

    public boolean IsInputtingMechanicalPower(Level world, int i, int j, int k)
    {
        int iSawFacing = GetFacing(world, i, j, k);
        for(int iFacing = 0; iFacing <= 5; iFacing++)
        {
            if(iFacing == iSawFacing)
            {
                continue;
            }
            FCBlockPos targetPos = new FCBlockPos(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
            if(iTargetid != mod_FCBetterThanWolves.fcAxleBlock.id)
            {
                continue;
            }
            FCBlockAxle axleBlock = (FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock;
            if(axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, iFacing) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0)
            {
                return true;
            }
        }

        return false;
    }

    public boolean IsOutputtingMechanicalPower(Level world, int i, int j, int l)
    {
        return false;
    }

    private static int iSawTickRate = 10;
    public static final float fSawBaseHeight = 0.75F;
    private final int iSawTopTextureIndex = 56;
    private final int iSawSideTextureIndex = 57;
    private final int iSawBladeTextureIndex = 58;

    /**
     * STATES
     */
    public static final IntProperty FACING = IntProperty.of("facing", 0, 5);
    public static final BooleanProperty POWER = BooleanProperty.of("power");

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(FACING);
        builder.add(POWER);
    }

}
