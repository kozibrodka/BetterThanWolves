package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.block.entity.DropperBlockEntity;
import net.kozibrodka.wolves.container.DropperScreenHandler;
import net.kozibrodka.wolves.events.ScreenHandlerListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class DropperBlock extends TemplateBlockWithEntity implements MechanicalDevice {
    public DropperBlock(Identifier identifier, Material material) {
        super(identifier, material);
        setTranslationKey(identifier.namespace, identifier.path);
    }

    @Override
    public int getTexture(int side, int meta) {
        if (side == 0) {
            if (meta == 0) {
                return TextureListener.dropper_bottom;
            }
            return TextureListener.dropper_bottom_open;
        } else if (side == 1) {
            return TextureListener.dropper_top;
        } else {
            return TextureListener.dropper_side;
        }
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new DropperBlockEntity();
    }

    public boolean onUse(World world, int x, int y, int z, PlayerEntity playerEntity) {
        DropperBlockEntity dropperBlockEntity = (DropperBlockEntity) world.getBlockEntity(x, y, z);
        ScreenHandlerListener.TempGuiX = x;
        ScreenHandlerListener.TempGuiY = y;
        ScreenHandlerListener.TempGuiZ = z;
        if (world.isRemote) {
            PacketHelper.send(new ScreenPacket("dropper", 0, x, y, z));
        }
        GuiHelper.openGUI(playerEntity, Identifier.of("wolves:openDropper"), dropperBlockEntity, new DropperScreenHandler(playerEntity.inventory, dropperBlockEntity));
        return true;
    }

    public boolean isBlockOn(World world, int x, int y, int z) {
        if (world == null) {
            return false;
        }
        return world.getBlockMeta(x, y, z) > 0;
    }

    public void onBreak(World world, int x, int y, int z) {
        InventoryHandler.ejectInventoryContents(world, x, y, z, (Inventory) world.getBlockEntity(x, y, z));
        super.onBreak(world, x, y, z);
    }

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
        world.setBlockMeta(x, y, z, 1);
    }

    @Override
    public void unpowerMachine(World world, int x, int y, int z, int side) {
        world.setBlockMeta(x, y, z, 0);
    }

    @Override
    public boolean isMachinePowered(World world, int x, int y, int z) {
        return isBlockOn(world, x, y, z);
    }

    @Override
    public boolean canInputMechanicalPower(World world, int x, int y, int z, int side) {
        return side > 1;
    }
}
