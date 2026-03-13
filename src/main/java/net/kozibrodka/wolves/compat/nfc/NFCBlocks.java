package net.kozibrodka.wolves.compat.nfc;

import net.kozibrodka.wolves.block.ConverterBlock;
import net.kozibrodka.wolves.block.LazyBlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class NFCBlocks {
    public static LazyBlockTemplate getConverterBlock(Namespace NAMESPACE) {
        return new ConverterBlock(Identifier.of(NAMESPACE, "converter"), Material.METAL, 1.5F, Block.METAL_SOUND_GROUP);
    }
}
