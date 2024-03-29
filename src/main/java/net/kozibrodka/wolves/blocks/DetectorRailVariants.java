package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.template.block.TemplateDetectorRailBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class DetectorRailVariants extends TemplateDetectorRailBlock implements BlockWithInventoryRenderer
        /* implements ITextureProvider */ //MAYBE FOR FAKE LATER
{

    public DetectorRailVariants(Identifier iid, int iTextureID)
    {
        super(iid, iTextureID);
        setHardness(0.7F);
        setSounds(METAL_SOUNDS);
    }

    public int getTextureForSide(int i, int j) {
        if(this.id == BlockListener.detectorRailWood.id)
        {
            return TextureListener.rail_wood;
        }else{
            return TextureListener.rail_obsidian;
        }
    }

        public int getTextureForSide(int i)
    {
        if(this.id == BlockListener.detectorRailWood.id)
        {
            return TextureListener.rail_wood;
        }else{
            return TextureListener.rail_obsidian;
        }
    }

    public int getTextureForSide(BlockView iblockaccess, int i, int j, int k, int l)
    {
        if(this.id == BlockListener.detectorRailWood.id)
        {
            return TextureListener.rail_wood;
        }else{
            return TextureListener.rail_obsidian;
        }
    }

    @Override
    public void renderInventory(BlockRenderer tileRenderer, int meta) {

    }
}
