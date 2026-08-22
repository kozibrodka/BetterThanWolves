package net.kozibrodka.wolves.compat.nfc;

import net.minecraft.block.Block;
import net.newfrontiercraft.nfc.events.init.BlockListener;

public class MinecartLogicExpander {

    public static double changeSpeedLimit(double constant, int groundId) {
        if (groundId == Block.SOUL_SAND.id) {
            return constant / 4;
        } else if (groundId == Block.DIRT.id || groundId == Block.GRASS_BLOCK.id || groundId == Block.SAND.id) {
            return constant / 2;
        } else if (groundId == BlockListener.pebble.id) {
            return constant + 0.1;
        } else if (groundId == BlockListener.aluminiumBlock.id) {
            return constant + 0.2;
        } else if (groundId == Block.IRON_BLOCK.id) {
            return constant + 0.3;
        } else if (groundId == BlockListener.steelBlock.id) {
            return constant + 0.4;
        } else if (groundId == BlockListener.osmiumBlock.id || groundId == BlockListener.osmiumBricks.id) {
            return constant + 1;
        }
        return constant;
    }

    public static double getGroundSlidingFactor(int groundId) {
        if (groundId == BlockListener.aluminiumBlock.id) {
            return 0.9;
        } else if (groundId == Block.IRON_BLOCK.id) {
            return 0.95;
        } else if (groundId == BlockListener.steelBlock.id) {
            return 0.975;
        } else if (groundId == BlockListener.osmiumBlock.id || groundId == BlockListener.osmiumBricks.id) {
            return 1.0;
        }
        return 0.5;
    }
}
