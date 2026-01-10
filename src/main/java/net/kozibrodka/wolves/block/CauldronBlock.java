// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockCauldron.java

package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.block.entity.CauldronBlockEntity;
import net.kozibrodka.wolves.container.CauldronScreenHandler;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ScreenHandlerListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;


public class CauldronBlock extends TemplateBlockWithEntity
        implements RotatableBlock, MechanicalDevice {

    public CauldronBlock(Identifier iid) {
        super(iid, Material.METAL);
        setTickRandomly(true);
    }

    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }

    public void onBreak(World world, int i, int j, int k) {
        InventoryHandler.ejectInventoryContents(world, i, j, k, (Inventory) world.getBlockEntity(i, j, k));
        super.onBreak(world, i, j, k);
    }

    public int getTexture(int side, int meta) {
        if (meta == 0 && side == 1 || meta > 0 && side == meta) {
            return TextureListener.cauldron_top;
        }
        int backSide = meta;
        backSide = switch (backSide) {
            case 2 -> 3;
            case 3 -> 2;
            case 4 -> 5;
            case 5 -> 4;
            default -> backSide;
        };
        switch (meta) {
            case 2:
                if (side == backSide) {
                    return TextureListener.cauldron_bottom;
                }
                if (side == 4) {
                    return TextureListener.cauldron_side_left;
                }
                if (side == 5) {
                    return TextureListener.cauldron_side_right;
                }
                return TextureListener.cauldron_side;
            case 3:
                if (side == backSide) {
                    return TextureListener.cauldron_bottom;
                }
                if (side < 2) {
                    return TextureListener.cauldron_side_down;
                }
                if (side == 4) {
                    return TextureListener.cauldron_side_right;
                }
                if (side == 5) {
                    return TextureListener.cauldron_side_left;
                }
                return TextureListener.cauldron_side;
            case 4:
                if (side == backSide) {
                    return TextureListener.cauldron_bottom;
                }
                if (side < 2) {
                    return TextureListener.cauldron_side_left;
                }
                if (side == 2) {
                    return TextureListener.cauldron_side_right;
                }
                if (side == 3) {
                    return TextureListener.cauldron_side_left;
                }
                return TextureListener.cauldron_side;
            case 5:
                if (side == backSide) {
                    return TextureListener.cauldron_bottom;
                }
                if (side < 2) {
                    return TextureListener.cauldron_side_right;
                }
                if (side == 2) {
                    return TextureListener.cauldron_side_left;
                }
                if (side == 3) {
                    return TextureListener.cauldron_side_right;
                }
                return TextureListener.cauldron_side;
            default:
                if (side == 0) {
                    return TextureListener.cauldron_bottom;
                } else {
                    return TextureListener.cauldron_side;
                }
        }
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityPlayer) {
        CauldronBlockEntity tileentitycauldron = (CauldronBlockEntity) world.getBlockEntity(i, j, k);
        ScreenHandlerListener.TempGuiX = i;
        ScreenHandlerListener.TempGuiY = j;
        ScreenHandlerListener.TempGuiZ = k;
        if (world.isRemote) {
            PacketHelper.send(new ScreenPacket("cauldron", 0, i, j, k));
        }
        GuiHelper.openGUI(entityPlayer, Identifier.of("wolves:openCauldron"), tileentitycauldron, new CauldronScreenHandler(entityPlayer.inventory, tileentitycauldron));
        return true;
    }

    protected BlockEntity createBlockEntity() {
        return new CauldronBlockEntity();
    }

    public Box getCollisionShape(World world, int i, int j, int k) {
        return Box.createCached(i, j, k, i + 1, (double) j + 0.99000000953674316D, (double) k + 1.0D);
    }

    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (getFireHeat(world, x, y, z) > 0) {
            for (int counter = 0; counter < 5; counter++) {
                float smokeX = (float) x + random.nextFloat();
                float smokeY = (float) y + random.nextFloat() * 0.5F + 1.0F;
                float smokeZ = (float) z + random.nextFloat();
                world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    public int getFireHeat(World world, int x, int y, int z) {
        int belowId = world.getBlockId(x, y - 1, z);
        if (belowId == Block.FIRE.id) {
            return 1;
        }
        if (belowId == BlockListener.stokedFire.id) {
            return 2;
        }
        return 0;
    }

    public void onEntityCollision(World world, int i, int j, int k, Entity entity) {
        List collisionList = null;
        collisionList = world.collectEntitiesByClass(ItemEntity.class, Box.createCached((float) i, (double) (float) j + 0.99000000953674316D, (float) k, (float) (i + 1), (double) (float) j + 0.99000000953674316D + 0.05000000074505806D, (float) (k + 1)));
        if (collisionList != null && collisionList.size() > 0) {
            CauldronBlockEntity tileEntityCauldron = (CauldronBlockEntity) world.getBlockEntity(i, j, k);
            for (int listIndex = 0; listIndex < collisionList.size(); listIndex++) {
                ItemEntity targetEntityItem = (ItemEntity) collisionList.get(listIndex);
                if (targetEntityItem.dead) {
                    continue;
                }
                if (InventoryHandler.addItemInstanceToInventory(tileEntityCauldron, targetEntityItem.stack)) {
                    world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.pop", 0.25F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.pop", i, j, k, 0.25F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    }
                    targetEntityItem.markDead();
                    continue;
                }
                int iBlockAboveID = world.getBlockId(i, j + 1, k);
                if (iBlockAboveID != Block.FLOWING_WATER.id && iBlockAboveID != Block.WATER.id) {
                    continue;
                }
                double fFullBoxTop = (double) j + 1.05D;
                if (targetEntityItem.boundingBox.minY < fFullBoxTop) {
                    double offset = fFullBoxTop - targetEntityItem.boundingBox.minY;
                    targetEntityItem.setPos(targetEntityItem.x, targetEntityItem.y + offset, targetEntityItem.z);
                }
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

    public void onTick(World world, int i, int j, int k, Random random) {
        ValidateFireUnderState(world, i, j, k);
    }

    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        ValidateFireUnderState(world, i, j, k);
    }

    public int getFacing(BlockView iBlockAccess, int i, int j, int l) {
        return 0;
    }

    public void setFacing(World world1, int l, int i1, int j1, int k1) {
    }

    public boolean canRotate(BlockView iBlockAccess, int i, int j, int l) {
        return false;
    }

    public boolean canTransmitRotation(BlockView iBlockAccess, int i, int j, int l) {
        return true;
    }

    public void rotate(World world1, int l, int i1, int j1, boolean flag) {
    }

    private void ValidateFireUnderState(World world, int x, int y, int z) {
        int newFireState = getFireHeat(world, x, y, z);
        CauldronBlockEntity cauldronBlockEntity = (CauldronBlockEntity) world.getBlockEntity(x, y, z);
        cauldronBlockEntity.NotifyOfChangeInFireUnder(newFireState);
    }

    public final int cauldronTopTextureIndex = 17;
    public final int cauldronSideTextureIndex = 18;
    public final int cauldronBottomTextureIndex = 19;
    public static final double dCauldronCollisionBoxHeight = 0.99000000953674316D;

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public void powerMachine(World world, int x, int y, int z, int side) {
        int rotation = side;
        rotation = switch (rotation) {
            case 2 -> 5;
            case 3 -> 4;
            case 4 -> 2;
            case 5 -> 3;
            default -> rotation;
        };
        world.setBlockMeta(x, y, z, rotation);
    }

    @Override
    public void unpowerMachine(World world, int x, int y, int z, int side) {
        world.setBlockMeta(x, y, z, 0);
    }

    @Override
    public boolean isMachinePowered(World world, int x, int y, int z) {
        return world.getBlockMeta(x, y, z) > 1;
    }

    @Override
    public boolean canInputMechanicalPower(World world, int x, int y, int z, int side) {
        return side > 1;
    }
}
