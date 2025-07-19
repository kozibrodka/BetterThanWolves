package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.ParticlePacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.recipe.SawingRecipeRegistry;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithInventoryRenderer.class)
public class SawBlock extends TemplateBlock
        implements MechanicalDevice, RotatableBlock, BlockWithWorldRenderer, BlockWithInventoryRenderer {

    public SawBlock(Identifier iid) {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSoundGroup(WOOD_SOUND_GROUP);
        setTickRandomly(true);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
    }

    public int getTextureId(BlockView blockAccess, int i, int j, int k, int iSide) {
        int iFacing = GetFacing(blockAccess, i, j, k);
        return iSide != iFacing ? TextureListener.saw_side : TextureListener.saw_face;
    }

    public int getTexture(int iSide) {
        return iSide != 1 ? TextureListener.saw_side : TextureListener.saw_face;
    }

    public int getTickRate() {
        return iSawTickRate;
    }

    public void onPlaced(World world, int i, int j, int k, int iFacing) {
        SetFacing(world, i, j, k, UnsortedUtils.getOppositeFacing(iFacing));
    }

    public void onPlaced(World world, int i, int j, int k, LivingEntity entityLiving) {
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
        SetFacing(world, i, j, k, iFacing);
    }

    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public Box getCollisionShape(World world, int i, int j, int k) {
        int iFacing = GetFacing(world, i, j, k);
        switch (iFacing) {
            case 0: // '\0'
                return Box.createCached((float) i, ((float) j + 1.0F) - 0.75F, (float) k, (float) i + 1.0F, (float) j + 1.0F, (float) k + 1.0F);

            case 1: // '\001'
                return Box.createCached((float) i, (float) j, (float) k, (float) i + 1.0F, (float) j + 0.75F, (float) k + 1.0F);

            case 2: // '\002'
                return Box.createCached((float) i, (float) j, ((float) k + 1.0F) - 0.75F, (float) i + 1.0F, (float) j + 1.0F, (float) k + 1.0F);

            case 3: // '\003'
                return Box.createCached((float) i, (float) j, (float) k, (float) i + 1.0F, (float) j + 1.0F, (float) k + 0.75F);

            case 4: // '\004'
                return Box.createCached(((float) i + 1.0F) - 0.75F, (float) j, (float) k, (float) i + 1.0F, (float) j + 1.0F, (float) k + 1.0F);
        }
        return Box.createCached((float) i, (float) j, (float) k, (float) i + 0.75F, (float) j + 1.0F, (float) k + 1.0F);
    }

    public void updateBoundingBox(BlockView iblockaccess, int i, int j, int k) {
        int iFacing = GetFacing(iblockaccess, i, j, k);
        switch (iFacing) {
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

    public void setupRenderBoundingBox() {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
    }

    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        if (iid == BlockListener.axleBlock.id || iid == BlockListener.handCrank.id) {
            world.scheduleBlockUpdate(i, j, k, id, getTickRate());
        } else {
            world.scheduleBlockUpdate(i, j, k, id, getTickRate() + world.random.nextInt(6));
        }
    }

    public void onTick(World world, int i, int j, int k, Random random) {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bOn = IsBlockOn(world, i, j, k);
        if (bOn != bReceivingPower) {
            world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 0.2F, 1.25F);
            if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
            }
            EmitSawParticles(world, i, j, k, random);
            SetBlockOn(world, i, j, k, bReceivingPower);
            if (bReceivingPower) {
                world.scheduleBlockUpdate(i, j, k, id, getTickRate() + random.nextInt(6));
            }
        } else if (bOn) {
            int iFacing = GetFacing(world, i, j, k);
            BlockPosition targetPos = new BlockPosition(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            if (!AttemptToSawBlock(world, targetPos.i, targetPos.j, targetPos.k, random, iFacing)) {
                BreakSaw(world, i, j, k);
            }
        }
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        if (IsBlockOn(world, i, j, k)) {
            EmitSawParticles(world, i, j, k, random);
        }
    }

    public void onEntityCollision(World world, int i, int j, int k, Entity entity) {
        if (IsBlockOn(world, i, j, k) && (entity instanceof LivingEntity)) {
            int iFacing = GetFacing(world, i, j, k);
            float fHalfLength = 0.3125F;
            float fHalfWidth = 0.0078125F;
            float fBlockHeight = 0.25F;
            Box sawBox;
            switch (iFacing) {
                case 0: // '\0'
                    sawBox = Box.createCached(0.5F - fHalfLength, 0.0D, 0.5F - fHalfWidth, 0.5F + fHalfLength, fBlockHeight, 0.5F + fHalfWidth);
                    break;

                case 1: // '\001'
                    sawBox = Box.createCached(0.5F - fHalfLength, 1.0F - fBlockHeight, 0.5F - fHalfWidth, 0.5F + fHalfLength, 1.0D, 0.5F + fHalfWidth);
                    break;

                case 2: // '\002'
                    sawBox = Box.createCached(0.5F - fHalfLength, 0.5F - fHalfWidth, 0.0D, 0.5F + fHalfLength, 0.5F + fHalfWidth, fBlockHeight);
                    break;

                case 3: // '\003'
                    sawBox = Box.createCached(0.5F - fHalfLength, 0.5F - fHalfWidth, 1.0F - fBlockHeight, 0.5F + fHalfLength, 0.5F + fHalfWidth, 1.0D);
                    break;

                case 4: // '\004'
                    sawBox = Box.createCached(0.0D, 0.5F - fHalfWidth, 0.5F - fHalfLength, fBlockHeight, 0.5F + fHalfWidth, 0.5F + fHalfLength);
                    break;

                default:
                    sawBox = Box.createCached(1.0F - fBlockHeight, 0.5F - fHalfWidth, 0.5F - fHalfLength, 1.0D, 0.5F + fHalfWidth, 0.5F + fHalfLength);
                    break;
            }
            sawBox = sawBox.offset(i, j, k);
            List collisionList = null;
            collisionList = world.collectEntitiesByClass(LivingEntity.class, sawBox);
            if (collisionList != null && collisionList.size() > 0) {
                for (int iTempListIndex = 0; iTempListIndex < collisionList.size(); iTempListIndex++) {
                    LivingEntity tempTargetEntity = (LivingEntity) collisionList.get(iTempListIndex);
                    if (tempTargetEntity.damage(null, 4)) {
                        world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 0.2F, 1.25F);
                        if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                            voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
                        }
                        EmitBloodParticles(world, i, j, k, world.random);
                    }
                }

            }
        }
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k) {
        return iBlockAccess.getBlockMeta(i, j, k) & 7;
    }

    public void SetFacing(World world, int i, int j, int k, int iFacing) {
        int iMetaData = world.getBlockMeta(i, j, k) & -8;
        iMetaData |= iFacing;
        world.setBlockMeta(i, j, k, iMetaData);
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int k) {
        int iFacing = GetFacing(iBlockAccess, i, j, k);
        return iFacing != 0;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int k) {
        int iFacing = GetFacing(iBlockAccess, i, j, k);
        return iFacing != 0 && iFacing != 1;
    }

    public void Rotate(World world, int i, int j, int k, boolean bReverse) {
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if (iNewFacing != iFacing) {
            SetFacing(world, i, j, k, iNewFacing);
            world.setBlocksDirty(i, j, k, i, j, k);
            world.scheduleBlockUpdate(i, j, k, id, getTickRate());
            world.blockUpdate(i, j, k, i);
        }
        UnsortedUtils.DestroyHorizontallyAttachedAxles(world, i, j, k);
    }

    public boolean IsBlockOn(BlockView iBlockAccess, int i, int j, int k) {
        return (iBlockAccess.getBlockMeta(i, j, k) & 8) > 0;
    }

    public void SetBlockOn(World world, int i, int j, int k, boolean bOn) {
        int iMetaData = world.getBlockMeta(i, j, k) & 7;
        if (bOn) {
            iMetaData |= 8;
        }
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    void EmitSawParticles(World world, int i, int j, int k, Random random) {
        int iFacing = GetFacing(world, i, j, k);
        float fBladeXPos = i;
        float fBladeYPos = j;
        float fBladeZPos = k;
        float fBladeXExtent = 0.0F;
        float fBladeZExtent = 0.0F;
        switch (iFacing) {
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
        for (int counter = 0; counter < 5; counter++) {
            float smokeX = fBladeXPos + (random.nextFloat() - 0.5F) * fBladeXExtent;
            float smokeY = fBladeYPos + random.nextFloat() * 0.1F;
            float smokeZ = fBladeZPos + (random.nextFloat() - 0.5F) * fBladeZExtent;
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    void EmitBloodParticles(World world, int i, int j, int k, Random random) {
        int iFacing = GetFacing(world, i, j, k);
        BlockPosition iTargetPos = new BlockPosition(i, j, k);
        iTargetPos.AddFacingAsOffset(iFacing);
        for (int counter = 0; counter < 10; counter++) {
            float smokeX = (float) iTargetPos.i + random.nextFloat();
            float smokeY = (float) iTargetPos.j + random.nextFloat();
            float smokeZ = (float) iTargetPos.k + random.nextFloat();
            world.addParticle("reddust", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
            if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                particlePacket(world, "reddust", smokeX, smokeY, smokeZ);
            }
        }
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h) {
        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k = 0; k < list2.size(); k++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g, h));
            }
        }
    }

    @Environment(EnvType.SERVER)
    public void particlePacket(World world, String name, double x, double y, double z) {
        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k1 = 0; k1 < list2.size(); k1++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k1);
                PacketHelper.sendTo(player1, new ParticlePacket(name, x, y, z, 0, 0, 0));
            }
        }
    }

    boolean AttemptToSawBlock(World world, int i, int j, int k, Random random, int iSawFacing) {
        if (!world.isAir(i, j, k)) {
            int blockId = world.getBlockId(i, j, k);
            Block block = BLOCKS[blockId];
            if (null != block) {
                ItemStack targetItem = new ItemStack(block.asItem(), 1, world.getBlockMeta(i, j, k));
                boolean sawedBlock = false;
                Block targetBlock = Block.BLOCKS[blockId];
                boolean bRemoveOriginalBlockIfSawed = true;

                // Standard recipes from the registry
                ItemStack output = SawingRecipeRegistry.getInstance().getResult(targetItem);
                if (output != null) {
                    if (output.count == 0) output.count = 1;
                    for (int iTempCount = 0; iTempCount < output.count; iTempCount++) {
                        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, output.itemId, output.getDamage());
                    }
                    sawedBlock = true;
                }

                // Special recipes with more complex outcomes
                else if (blockId == BlockListener.companionCube.id) {
                    CompanionCubeBlock cubeBlock = (CompanionCubeBlock) BlockListener.companionCube;
                    if (!cubeBlock.GetHalfCubeState(world, i, j, k)) {
                        if (iSawFacing == 0 || iSawFacing == 1) {
                            for (int iTempCount = 0; iTempCount < 2; iTempCount++) {
                                UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, BlockListener.companionCube.asItem().id, 1);
                            }

                        } else {
                            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, BlockListener.companionCube.asItem().id, 1);
                            cubeBlock.SetHalfCubeState(world, i, j, k, true);
                            world.setBlocksDirty(i, j, k, i, j, k);
                            bRemoveOriginalBlockIfSawed = false;
                        }
                        BlockPosition bloodPos = new BlockPosition(i, j, k);
                        bloodPos.AddFacingAsOffset(UnsortedUtils.getOppositeFacing(iSawFacing));
                        EmitBloodParticles(world, bloodPos.i, bloodPos.j, bloodPos.k, world.random);
                        world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "mob.wolf.hurt", 5F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                        if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                            voicePacket(world, "mob.wolf.hurt", i, j, k, 5F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                        }
                        sawedBlock = true;
                    } else if (iSawFacing == 0 || iSawFacing == 1) {
                        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, BlockListener.companionCube.asItem().id, 1);
                        sawedBlock = true;
                    }
                } else if (blockId == Block.LEAVES.id || blockId == Block.SUGAR_CANE.id || blockId == Block.WHEAT.id || blockId == BlockListener.hempCrop.id) {
                    targetBlock.dropStacks(world, i, j, k, world.getBlockMeta(i, j, k));
                    sawedBlock = true;
                } else if (blockId != Block.PISTON_HEAD.id && targetBlock != null) {
                    Material targetMaterial = targetBlock.material;
                    if (targetMaterial != Material.WOOD) {
                        if (targetMaterial.isSolid()) {
                            return false;
                        }
                    } else {
                        targetBlock.dropStacks(world, i, j, k, world.getBlockMeta(i, j, k));
                        sawedBlock = true;
                    }
                }
                if (sawedBlock) {
                    world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 0.2F, 1.25F);
                    if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
                    }
                    EmitSawParticles(world, i, j, k, random);
                    if (bRemoveOriginalBlockIfSawed) {
                        world.setBlock(i, j, k, 0);
                    }
                }
            }
        }
        return true;
    }

    public void BreakSaw(World world, int i, int j, int k) {
        for (int iTemp = 0; iTemp < 2; iTemp++) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.gear.id, 0);
        }

        for (int iTemp = 0; iTemp < 2; iTemp++) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Block.PLANKS.asItem().id, 0);
        }

        for (int iTemp = 0; iTemp < 2; iTemp++) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Item.IRON_INGOT.id, 0);
        }

        for (int iTemp = 0; iTemp < 1; iTemp++) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.belt.id, 0);
        }

        world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 0.2F, 1.25F);
        if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
        }
        world.setBlock(i, j, k, 0);
    }

    public boolean CanOutputMechanicalPower() {
        return false;
    }

    public boolean CanInputMechanicalPower() {
        return true;
    }

    public boolean IsInputtingMechanicalPower(World world, int i, int j, int k) {
        int iSawFacing = GetFacing(world, i, j, k);
        for (int iFacing = 0; iFacing <= 5; iFacing++) {
            if (iFacing == iSawFacing) {
                continue;
            }
            BlockPosition targetPos = new BlockPosition(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int blockId = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
            if (blockId != BlockListener.axleBlock.id) {
                continue;
            }
            AxleBlock axleBlock = (AxleBlock) BlockListener.axleBlock;
            if (axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, iFacing) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0) {
                return true;
            }
        }

        return false;
    }

    public boolean IsOutputtingMechanicalPower(World world, int i, int j, int l) {
        return false;
    }

    private static final int iSawTickRate = 10;
    public static final float fSawBaseHeight = 0.75F;
    private final int iSawTopTextureIndex = 56;
    private final int iSawSideTextureIndex = 57;
    private final int iSawBladeTextureIndex = 58;

    @Override
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z) {
        float f = 0.5F;
        float f1 = 0.5F;
        float f2 = 0.75F;
        int l = GetFacing(tileView, x, y, z);
        switch (l) {
            case 0: // '\0'
                this.setBoundingBox(0.5F - f1, 1.0F - f2, 0.5F - f, 0.5F + f1, 1.0F, 0.5F + f);
                break;

            case 1: // '\001'
                this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
                break;

            case 2: // '\002'
                this.setBoundingBox(0.5F - f1, 0.5F - f, 1.0F - f2, 0.5F + f1, 0.5F + f, 1.0F);
                break;

            case 3: // '\003'
                this.setBoundingBox(0.5F - f1, 0.5F - f, 0.0F, 0.5F + f1, 0.5F + f, f2);
                break;

            case 4: // '\004'
                this.setBoundingBox(1.0F - f2, 0.5F - f1, 0.5F - f, 1.0F, 0.5F + f1, 0.5F + f);
                break;

            default:
                this.setBoundingBox(0.0F, 0.5F - f1, 0.5F - f, f2, 0.5F + f1, 0.5F + f);
                break;
        }
        tileRenderer.renderBlock(this, x, y, z);
        f = 0.3125F;
        f1 = 0.0078125F;
        f2 = 0.25F;
        switch (l) {
            case 0: // '\0'
                this.setBoundingBox(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 0.999F, 0.5F + f1);
                break;

            case 1: // '\001'
                this.setBoundingBox(0.5F - f, 0.001F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
                break;

            case 2: // '\002'
                this.setBoundingBox(0.5F - f, 0.5F - f1, 0.0F, 0.5F + f, 0.5F + f1, f2);
                break;

            case 3: // '\003'
                this.setBoundingBox(0.5F - f, 0.5F - f1, 1.0F - f2, 0.5F + f, 0.5F + f1, 1.0F);
                break;

            case 4: // '\004'
                this.setBoundingBox(0.0F, 0.5F - f1, 0.5F - f, f2, 0.5F + f1, 0.5F + f);
                break;

            default:
                this.setBoundingBox(1.0F - f2, 0.5F - f1, 0.5F - f, 1.0F, 0.5F + f1, 0.5F + f);
                break;
        }
        CustomBlockRendering.renderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.saw_saw);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {
        this.setupRenderBoundingBox();
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 1);
        float f = 0.3125F;
        float f1 = 0.0078125F;
        this.setBoundingBox(0.5F - f, 0.001F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.saw_saw);
    }
}
