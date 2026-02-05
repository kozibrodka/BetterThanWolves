package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.block.entity.MachineBusBlockEntity;
import net.kozibrodka.wolves.container.MachineBusScreenHandler;
import net.kozibrodka.wolves.events.BlockEntityListener;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.util.Identifier;

public class MachineBusBlock extends LazyBlockWithEntityTemplate {
    public MachineBusBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        if (blockEntity instanceof MachineBusBlockEntity machineBusBlockEntity)
            GuiHelper.openGUI(player, Identifier.of(BlockEntityListener.NAMESPACE, "openMachineBus"),
                    machineBusBlockEntity, new MachineBusScreenHandler(player.inventory, machineBusBlockEntity));
        return true;
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new MachineBusBlockEntity();
    }

    public void onBreak(World world, int x, int y, int z) {
        InventoryHandler.ejectInventoryContents(world, x, y, z, (Inventory) world.getBlockEntity(x, y, z));
        super.onBreak(world, x, y, z);
    }
}
