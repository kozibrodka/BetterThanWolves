package net.kozibrodka.wolves.compat.whatsthis.providers.block;

import net.danygames2014.whatsthis.api.*;
import net.kozibrodka.wolves.block.*;
import net.kozibrodka.wolves.block.entity.*;
import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

public class WolvesProbeInfoProvider implements IProbeInfoProvider {
    @Override
    public String getID() {
        return BlockListener.NAMESPACE.id("wolves_block").toString();
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState state, IProbeHitData data) {
        Block block = state.getBlock();
        BlockPos pos = data.getPos();

        // Cauldron
        if (block instanceof CauldronBlock && world.getBlockEntity(pos.x, pos.y, pos.z) instanceof CauldronBlockEntity cauldron) {
            if (cauldron.IsCooking()) {
                probeInfo.progress(
                        cauldron.getCookProgressScaled(100),
                        100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" %")
                                .prefix("Cooking ")
                        
                );
            }
        }

        // Crucible
        if (block instanceof CrucibleBlock && world.getBlockEntity(pos.x, pos.y, pos.z) instanceof CrucibleBlockEntity crucible) {
            if (crucible.isCooking()) {
                probeInfo.progress(
                        crucible.getCookProgressScaled(100),
                        100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" %")
                                .prefix("Cooking ")
                );
            }
        }

        // Mill Stone
        if (block instanceof MillStoneBlock && world.getBlockEntity(pos.x, pos.y, pos.z) instanceof MillStoneBlockEntity millStone) {
            if (millStone.IsGrinding()) {
                probeInfo.progress(
                        millStone.getGrindProgressScaled(100),
                        100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" %")
                                .prefix("Grinding ")
                );
            }
        }

        // Hopper
        if (block instanceof HopperBlock && world.getBlockEntity(pos.x, pos.y, pos.z) instanceof HopperBlockEntity hopper) {
            if (hopper.hopperEjectBlocked) {
                probeInfo.text(TextStyleClass.ERROR + "Output Blocked");
            }
        }

        // Turntable
        if (block instanceof TurntableBlock && world.getBlockEntity(pos.x, pos.y, pos.z) instanceof TurntableBlockEntity turntable) {
            // Have fun Atilist, I have no idea what im doing here
//            probeInfo.text("Switch Setting: " + turntable.switchSetting);
//            probeInfo.text("Rotation Count: " + turntable.craftingRotationCount);
        }
    }
}
