package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.block.entity.LoomBlockEntity;
import net.kozibrodka.wolves.container.LoomScreenHandler;
import net.kozibrodka.wolves.events.ScreenHandlerListener;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.util.Identifier;

public class LoomBlock extends LazyBlockWithEntityTemplate {
    public LoomBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity playerEntity) {
        LoomBlockEntity loomBlockEntity = (LoomBlockEntity)world.getBlockEntity(i, j, k);
        ScreenHandlerListener.TempGuiX = i;
        ScreenHandlerListener.TempGuiY = j;
        ScreenHandlerListener.TempGuiZ = k;
        //if(world.isRemote){
        //    PacketHelper.send(new ScreenPacket("mill",0, i, j, k));
        //}
        GuiHelper.openGUI(playerEntity, Identifier.of("wolves:openLoom"), loomBlockEntity, new LoomScreenHandler(playerEntity.inventory, (LoomBlockEntity) loomBlockEntity));
        return true;
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new LoomBlockEntity();
    }
}
