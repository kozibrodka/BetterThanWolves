package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.block.entity.MachineBusBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.modificationstation.stationapi.api.util.Identifier;

public class MachineBusBlock extends LazyBlockWithEntityTemplate {
    public MachineBusBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new MachineBusBlockEntity();
    }
}
