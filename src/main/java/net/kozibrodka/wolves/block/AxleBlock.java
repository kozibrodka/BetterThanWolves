package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
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

    public AxleBlock(Identifier id) {
        super(id, Material.WOOD);
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

    public void onPlaced(World world, int x, int y, int z, int facing) {
        setAxisAlignmentBasedOnFacing(world, x, y, z, facing);
    }

    public void onPlaced(World world, int x, int y, int z) {
        super.onPlaced(world, x, y, z);
        validatePowerLevel(world, x, y, z);
        world.scheduleBlockUpdate(x, y, z, BlockListener.axleBlock.id, getTickRate());
        world.notifyNeighbors(x, y, z, world.getBlockId(x, y, z));
    }

    @Override
    public void onBreak(World world, int x, int y, int z) {
        super.onBreak(world, x, y, z);
        BlockPosition[] potentialMachines = getConnectionCandidates(world, x, y, z);
        int axis = getAxisAlignment(world, x, y, z);
        updateAdjacentMachine(world, potentialMachines[0], axis * 2 + 1,false);
        updateAdjacentMachine(world, potentialMachines[1], axis * 2,false);
    }

    public void onTick(World world, int x, int y, int z, Random random) {
        validatePowerLevel(world, x, y, z);
    }

    public Box getCollisionShape(World world, int x, int y, int z) {
        int axis = getAxisAlignment(world, x, y, z);
        return switch (axis) {
            case 0 -> // '\0'
                    Box.createCached(((float) x + 0.5F) - 0.125F, (float) y, ((float) z + 0.5F) - 0.125F, (float) x + 0.5F + 0.125F, (float) y + 1.0F, (float) z + 0.5F + 0.125F);
            case 1 -> // '\001'
                    Box.createCached(((float) x + 0.5F) - 0.125F, ((float) y + 0.5F) - 0.125F, (float) z, (float) x + 0.5F + 0.125F, (float) y + 0.5F + 0.125F, (float) z + 1.0F);
            default ->
                    Box.createCached((float) x, ((float) y + 0.5F) - 0.125F, ((float) z + 0.5F) - 0.125F, (float) x + 1.0F, (float) y + 0.5F + 0.125F, (float) z + 0.5F + 0.125F);
        };
    }

    public void updateBoundingBox(BlockView blockView, int x, int y, int z) {
        int axis = getAxisAlignment(blockView, x, y, z);
        switch (axis) {
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

    public void neighborUpdate(World world, int x, int y, int z, int id) {
        validatePowerLevel(world, x, y, z);
        world.scheduleBlockUpdate(x, y, z, id, getTickRate());
    }

    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (getPowerLevel(world, x, y, z) > 0) {
            emitAxleParticles(world, x, y, z, random);
        }
    }

    public int getAxisAlignment(BlockView blockView, int x, int y, int z) {
        return blockView.getBlockMeta(x, y, z) >> 2;
    }

    public void setAxisAlignmentBasedOnFacing(World world, int x, int y, int z, int facing) {
        int axis = switch (facing) { // '\0'
            case 0, 1 -> // '\001'
                    0; // '\002'
            case 2, 3 -> // '\003'
                    1;
            default -> 2;
        };
        int metadata = world.getBlockMeta(x, y, z) & 3;
        metadata |= axis << 2;
        world.setBlockMeta(x, y, z, metadata);
        world.blockUpdateEvent(x, y, z);
    }

    public int getPowerLevel(BlockView blockView, int x, int y, int z) {
        return blockView.getBlockMeta(x, y, z) & 3;
    }

    public void setPowerLevel(World world, int x, int y, int z, int powerLevel) {
        if (world.isRemote) {
            return;
            //TODO: Maybe more of those conditions in other blocks
        }
        powerLevel &= 3;
        int metadata = world.getBlockMeta(x, y, z) & 0xc;
        metadata |= powerLevel;
        world.setBlockMeta(x, y, z, metadata);
        world.blockUpdateEvent(x, y, z);
        BlockPosition[] potentialMachines = getConnectionCandidates(world, x, y, z);
        int axis = getAxisAlignment(world, x, y, z);
        updateAdjacentMachine(world, potentialMachines[0], axis * 2 + 1,powerLevel > 0);
        updateAdjacentMachine(world, potentialMachines[1], axis * 2,powerLevel > 0);
    }

    public boolean isAxleOrientedTowardsFacing(BlockView blockView, int x, int y, int z, int facing) {
        int axis = getAxisAlignment(blockView, x, y, z);
        switch (axis) {
            case 0: // '\0'
                if (facing == 0 || facing == 1) {
                    return true;
                }
                break;

            case 1: // '\001'
                if (facing == 2 || facing == 3) {
                    return true;
                }
                break;

            default:
                if (facing == 4 || facing == 5) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void breakAxle(World world, int x, int y, int z) {
        if (world.getBlockId(x, y, z) == BlockListener.axleBlock.id || world.getBlockId(x, y, z) == BlockListener.nonCollidingAxleBlock.id) {
            for (int drops = 0; drops < 5; drops++) {
                UnsortedUtils.EjectSingleItemWithRandomOffset(world, x, y, z, ItemListener.hempFibers.id, 0);
            }

            for (int drops = 0; drops < 2; drops++) {
                UnsortedUtils.EjectSingleItemWithRandomOffset(world, x, y, z, Item.STICK.id, 0);
            }

            world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.explode", 0.2F, 1.25F);
            if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.explode", x, y, z, 0.2F, 1.25F);
            }
            world.setBlock(x, y, z, 0);
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

    /**
     * Check if adjacent axles have power and handle the situation.
     * @param world World the block is in.
     * @param x x coordinate of the block.
     * @param y y coordinate of the block.
     * @param z z coordinate of the block.
     */
    private void validatePowerLevel(World world, int x, int y, int z) {
        int currentPower = getPowerLevel(world, x, y, z);
        int axis = getAxisAlignment(world, x, y, z);
        // Skip if the axle already is fully powered
        if (currentPower == 3) {
            return;
        }
        // Determine power source locations based on axis
        BlockPosition[] potentialSources = getConnectionCandidates(world, x, y, z);
        // Check both sources for power
        int maxNeighborPower = 0;
        int greaterPowerNeighbors = 0;
        for (int tempSource = 0; tempSource < 2; tempSource++) {
            // Skip if source is not an axle
            int tempId = world.getBlockId(potentialSources[tempSource].x, potentialSources[tempSource].y, potentialSources[tempSource].z);
            if (tempId != BlockListener.axleBlock.id && tempId != BlockListener.nonCollidingAxleBlock.id) {
                continue;
            }
            // Skip if other axle is misaligned
            int tempAxis = getAxisAlignment(world, potentialSources[tempSource].x, potentialSources[tempSource].y, potentialSources[tempSource].z);
            if (tempAxis != axis) {
                continue;
            }
            // Determine most powerful neighbour
            int tempPowerLevel = getPowerLevel(world, potentialSources[tempSource].x, potentialSources[tempSource].y, potentialSources[tempSource].z);
            if (tempPowerLevel > maxNeighborPower) {
                maxNeighborPower = tempPowerLevel;
            }
            // Calculate amount of neighbours with more power than the axle itself
            if (tempPowerLevel > currentPower) {
                greaterPowerNeighbors++;
            }
        }
        // Break axle and stop validation if two neighbours have more power
        if (greaterPowerNeighbors >= 2) {
            breakAxle(world, x, y, z);
            return;
        }
        // Check if axle branch is too long
        int newPower;
        if (maxNeighborPower > currentPower) {
            if (maxNeighborPower == 1) {
                breakAxle(world, x, y, z);
                return;
            }
            newPower = maxNeighborPower - 1;
        } else {
            newPower = 0;
        }
        // Change power level if it is different to the old one
        if (newPower != currentPower) {
            setPowerLevel(world, x, y, z, newPower);
        }
    }

    private BlockPosition[] getConnectionCandidates(World world, int x, int y, int z) {
        int axis = getAxisAlignment(world, x, y, z);
        BlockPosition[] connectionCandidates = new BlockPosition[2];
        connectionCandidates[0] = new BlockPosition(x, y, z);
        connectionCandidates[1] = new BlockPosition(x, y, z);
        switch (axis) {
            case 0: // '\0'
                connectionCandidates[0].addFacingAsOffset(0);
                connectionCandidates[1].addFacingAsOffset(1);
                break;
            case 1: // '\001'
                connectionCandidates[0].addFacingAsOffset(2);
                connectionCandidates[1].addFacingAsOffset(3);
                break;
            default:
                connectionCandidates[0].addFacingAsOffset(4);
                connectionCandidates[1].addFacingAsOffset(5);
                break;
        }
        return connectionCandidates;
    }

    private void updateAdjacentMachine(World world, BlockPosition potentialMachine, int side, boolean powered) {
        int x = potentialMachine.x;
        int y = potentialMachine.y;
        int z = potentialMachine.z;
        int machineId = world.getBlockId(x, y, z);
        if (machineId == 0) {
            return;
        }
        Block machineBlock = Block.BLOCKS[machineId];
        if (machineBlock instanceof MechanicalDevice device) {
            if (!device.canInputMechanicalPower(world, x, y, z, side)) {
                return;
            }
            if (powered) {
                device.powerMachine(world, x, y, z);
            } else {
                device.unpowerMachine(world, x, y, z);
            }
        }
    }

    private void emitAxleParticles(World world, int x, int y, int z, Random random) {
        for (int counter = 0; counter < 2; counter++) {
            float smokeX = (float) x + random.nextFloat();
            float smokeY = (float) y + random.nextFloat() * 0.5F + 0.625F;
            float smokeZ = (float) z + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    public void overpower(World world, int x, int y, int z) {
        int currentPower = getPowerLevel(world, x, y, z);
        int axis = getAxisAlignment(world, x, y, z);
        BlockPosition[] potentialSources = new BlockPosition[2];
        potentialSources[0] = new BlockPosition(x, y, z);
        potentialSources[1] = new BlockPosition(x, y, z);
        switch (axis) {
            case 0: // '\0'
                potentialSources[0].addFacingAsOffset(0);
                potentialSources[1].addFacingAsOffset(1);
                break;

            case 1: // '\001'
                potentialSources[0].addFacingAsOffset(2);
                potentialSources[1].addFacingAsOffset(3);
                break;

            default:
                potentialSources[0].addFacingAsOffset(4);
                potentialSources[1].addFacingAsOffset(5);
                break;
        }
        for (int tempSource = 0; tempSource < 2; tempSource++) {
            int tempId = world.getBlockId(potentialSources[tempSource].x, potentialSources[tempSource].y, potentialSources[tempSource].z);
            if (tempId == BlockListener.axleBlock.id || tempId == BlockListener.nonCollidingAxleBlock.id) {
                int tempAxis = getAxisAlignment(world, potentialSources[tempSource].x, potentialSources[tempSource].y, potentialSources[tempSource].z);
                if (tempAxis != axis) {
                    continue;
                }
                int tempPowerLevel = getPowerLevel(world, potentialSources[tempSource].x, potentialSources[tempSource].y, potentialSources[tempSource].z);
                if (tempPowerLevel < currentPower) {
                    overpower(world, potentialSources[tempSource].x, potentialSources[tempSource].y, potentialSources[tempSource].z);
                }
                continue;
            }
            if (tempId == BlockListener.gearBox.id) {
                ((GearboxBlock) BlockListener.gearBox).overpower(world, potentialSources[tempSource].x, potentialSources[tempSource].y, potentialSources[tempSource].z);
            }
        }

    }

}
