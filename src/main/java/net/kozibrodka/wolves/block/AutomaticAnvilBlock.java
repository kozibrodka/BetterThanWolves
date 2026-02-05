package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.block.entity.AutomaticAnvilBlockEntity;
import net.kozibrodka.wolves.container.AutomaticAnvilScreenHandler;
import net.kozibrodka.wolves.events.BlockEntityListener;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.util.Identifier;

public class AutomaticAnvilBlock extends LazyBlockWithEntityTemplate {
    public AutomaticAnvilBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
    }

    @Override
    public int getTexture(int side, int meta) {
        if (side == meta) {
            return topTexture;
        }
        return sideTexture;
    }

    @Override
    public void onPlaced(World world, int x, int y, int z, LivingEntity placer) {
        int var6 = MathHelper.floor((double)(placer.yaw * 4.0F / 360.0F) + (double)0.5F) & 3;
        if (var6 == 0) {
            world.setBlockMeta(x, y, z, 2);
        }

        if (var6 == 1) {
            world.setBlockMeta(x, y, z, 5);
        }

        if (var6 == 2) {
            world.setBlockMeta(x, y, z, 3);
        }

        if (var6 == 3) {
            world.setBlockMeta(x, y, z, 4);
        }
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new AutomaticAnvilBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity playerEntity) {
        AutomaticAnvilBlockEntity automaticAnvilBlockEntity = (AutomaticAnvilBlockEntity) world.getBlockEntity(x, y, z);
        GuiHelper.openGUI(playerEntity, Identifier.of(BlockEntityListener.NAMESPACE, "openAutomaticAnvil"), automaticAnvilBlockEntity, new AutomaticAnvilScreenHandler(playerEntity.inventory, automaticAnvilBlockEntity));
        return true;
    }

    public void onBreak(World world, int x, int y, int z) {
        InventoryHandler.ejectInventoryContents(world, x, y, z, (Inventory) world.getBlockEntity(x, y, z));
        super.onBreak(world, x, y, z);
    }
}
