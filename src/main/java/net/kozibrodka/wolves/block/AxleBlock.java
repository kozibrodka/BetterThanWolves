package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;


public class AxleBlock extends TemplateBlock {

    public AxleBlock(Identifier iid) {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSoundGroup(WOOD_SOUND_GROUP);
    }

    public int getTickRate() {
        return 1;
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public void onPlaced(World world, int i, int j, int k, int iFacing) {
        SetAxisAlignmentBasedOnFacing(world, i, j, k, iFacing);
    }

    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        SetPowerLevel(world, i, j, k, 0);
        world.scheduleBlockUpdate(i, j, k, BlockListener.axleBlock.id, getTickRate());
    }

    public void onTick(World world, int i, int j, int k, Random random) {
        ValidatePowerLevel(world, i, j, k);
    }

    public Box getCollisionShape(World world, int i, int j, int k) {
        int iAxis = getAxisAlignment(world, i, j, k);
        switch (iAxis) {
            case 0: // '\0'
                return Box.createCached(((float) i + 0.5F) - 0.125F, (float) j, ((float) k + 0.5F) - 0.125F, (float) i + 0.5F + 0.125F, (float) j + 1.0F, (float) k + 0.5F + 0.125F);

            case 1: // '\001'
                return Box.createCached(((float) i + 0.5F) - 0.125F, ((float) j + 0.5F) - 0.125F, (float) k, (float) i + 0.5F + 0.125F, (float) j + 0.5F + 0.125F, (float) k + 1.0F);
        }
        return Box.createCached((float) i, ((float) j + 0.5F) - 0.125F, ((float) k + 0.5F) - 0.125F, (float) i + 1.0F, (float) j + 0.5F + 0.125F, (float) k + 0.5F + 0.125F);
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k) {
        int iAxis = getAxisAlignment(iBlockAccess, i, j, k);
        switch (iAxis) {
            case 0: // '\0'
                setBoundingBox(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
                break;

            case 1: // '\001'
                setBoundingBox(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);
                break;

            default:
                setBoundingBox(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
                break;
        }
    }

    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        ValidatePowerLevel(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        if (GetPowerLevel(world, i, j, k) > 0) {
            EmitAxleParticles(world, i, j, k, random);
        }
    }

    public int getAxisAlignment(BlockView iBlockAccess, int i, int j, int k) {
        return iBlockAccess.getBlockMeta(i, j, k) >> 2;
    }

    public void SetAxisAlignmentBasedOnFacing(World world, int i, int j, int k, int iFacing) {
        int iAxis;
        switch (iFacing) {
            case 0: // '\0'
            case 1: // '\001'
                iAxis = 0;
                break;

            case 2: // '\002'
            case 3: // '\003'
                iAxis = 1;
                break;

            default:
                iAxis = 2;
                break;
        }
        int iMetaData = world.getBlockMeta(i, j, k) & 3;
        iMetaData |= iAxis << 2;
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    public int GetPowerLevel(BlockView iBlockAccess, int i, int j, int k) {
        return iBlockAccess.getBlockMeta(i, j, k) & 3;
    }

    public void SetPowerLevel(World world, int i, int j, int k, int iPowerLevel) {
        if (world.isRemote) {
            return;
            //TODO: Maybe more of those conditions in other blocks
        }
        iPowerLevel &= 3;
        int iMetaData = world.getBlockMeta(i, j, k) & 0xc;
        iMetaData |= iPowerLevel;
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
//        world.method_202(i, j, k, i, j, k);
    }

    public boolean IsAxleOrientedTowardsFacing(BlockView iBlockAccess, int i, int j, int k, int iFacing) {
        int iAxis = getAxisAlignment(iBlockAccess, i, j, k);
        switch (iAxis) {
            case 0: // '\0'
                if (iFacing == 0 || iFacing == 1) {
                    return true;
                }
                break;

            case 1: // '\001'
                if (iFacing == 2 || iFacing == 3) {
                    return true;
                }
                break;

            default:
                if (iFacing == 4 || iFacing == 5) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void BreakAxle(World world, int i, int j, int k) {
        if (world.getBlockId(i, j, k) == BlockListener.axleBlock.id || world.getBlockId(i, j, k) == BlockListener.nonCollidingAxleBlock.id) {
            for (int iTemp = 0; iTemp < 5; iTemp++) {
                UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.hempFibers.id, 0);
            }

            for (int iTemp = 0; iTemp < 2; iTemp++) {
                UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Item.STICK.id, 0);
            }

            world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 0.2F, 1.25F);
            if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
            }
            world.setBlock(i, j, k, 0);
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

    private void ValidatePowerLevel(World world, int i, int j, int k) {
        int currentPower = GetPowerLevel(world, i, j, k);
        int axis = getAxisAlignment(world, i, j, k);
        if (currentPower != 3) {
            BlockPosition[] potentialSources = new BlockPosition[2];
            potentialSources[0] = new BlockPosition(i, j, k);
            potentialSources[1] = new BlockPosition(i, j, k);
            switch (axis) {
                case 0: // '\0'
                    potentialSources[0].AddFacingAsOffset(0);
                    potentialSources[1].AddFacingAsOffset(1);
                    break;
                case 1: // '\001'
                    potentialSources[0].AddFacingAsOffset(2);
                    potentialSources[1].AddFacingAsOffset(3);
                    break;
                default:
                    potentialSources[0].AddFacingAsOffset(4);
                    potentialSources[1].AddFacingAsOffset(5);
                    break;
            }
            int maxNeighborPower = 0;
            int greaterPowerNeighbors = 0;
            for (int tempSource = 0; tempSource < 2; tempSource++) {
                int tempId = world.getBlockId(potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                if (tempId != BlockListener.axleBlock.id && tempId != BlockListener.nonCollidingAxleBlock.id) {
                    continue;
                }
                int tempAxis = getAxisAlignment(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                if (tempAxis != axis) {
                    continue;
                }
                int tempPowerLevel = GetPowerLevel(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                if (tempPowerLevel > maxNeighborPower) {
                    maxNeighborPower = tempPowerLevel;
                }
                if (tempPowerLevel > currentPower) {
                    greaterPowerNeighbors++;
                }
            }

            if (greaterPowerNeighbors >= 2) {
                BreakAxle(world, i, j, k);
                return;
            }
            int newPower = currentPower;
            if (maxNeighborPower > currentPower) {
                if (maxNeighborPower == 1) {
                    BreakAxle(world, i, j, k);
                    return;
                }
                newPower = maxNeighborPower - 1;
            } else {
                newPower = 0;
            }
            if (newPower != currentPower) {
//                System.out.println("ZNIAMIA");
                SetPowerLevel(world, i, j, k, newPower);
//                world.method_243(i, j, k);
//                world.method_202(i, j, k, i, j, k);
            }
        }
    }

    private void EmitAxleParticles(World world, int i, int j, int k, Random random) {
        for (int counter = 0; counter < 2; counter++) {
            float smokeX = (float) i + random.nextFloat();
            float smokeY = (float) j + random.nextFloat() * 0.5F + 0.625F;
            float smokeZ = (float) k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    public void Overpower(World world, int i, int j, int k) {
        int iCurrentPower = GetPowerLevel(world, i, j, k);
        int iAxis = getAxisAlignment(world, i, j, k);
        BlockPosition[] potentialSources = new BlockPosition[2];
        potentialSources[0] = new BlockPosition(i, j, k);
        potentialSources[1] = new BlockPosition(i, j, k);
        switch (iAxis) {
            case 0: // '\0'
                potentialSources[0].AddFacingAsOffset(0);
                potentialSources[1].AddFacingAsOffset(1);
                break;

            case 1: // '\001'
                potentialSources[0].AddFacingAsOffset(2);
                potentialSources[1].AddFacingAsOffset(3);
                break;

            default:
                potentialSources[0].AddFacingAsOffset(4);
                potentialSources[1].AddFacingAsOffset(5);
                break;
        }
        for (int tempSource = 0; tempSource < 2; tempSource++) {
            int iTempid = world.getBlockId(potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
            if (iTempid == BlockListener.axleBlock.id || iTempid == BlockListener.nonCollidingAxleBlock.id) {
                int iTempAxis = getAxisAlignment(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                if (iTempAxis != iAxis) {
                    continue;
                }
                int iTempPowerLevel = GetPowerLevel(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                if (iTempPowerLevel < iCurrentPower) {
                    Overpower(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                }
                continue;
            }
            if (iTempid == BlockListener.gearBox.id) {
                ((GearboxBlock) BlockListener.gearBox).overpower(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
            }
        }

    }

}
