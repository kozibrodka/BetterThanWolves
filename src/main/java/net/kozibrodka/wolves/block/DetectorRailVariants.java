package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.template.block.TemplateDetectorRailBlock;
import net.modificationstation.stationapi.api.util.Identifier;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithInventoryRenderer.class)
public class DetectorRailVariants extends TemplateDetectorRailBlock implements BlockWithInventoryRenderer
        /* implements ITextureProvider */ //MAYBE FOR FAKE LATER
{

    public DetectorRailVariants(Identifier iid, int iTextureID, boolean isWooden) {
        super(iid, iTextureID);
        this.wooden = isWooden;
        setHardness(0.7F);
        setSoundGroup(METAL_SOUND_GROUP);
    }

    protected boolean wooden;

    @Override
    public int getTexture(int i, int meta) {
        if (wooden) {
            if((meta & 8) == 0){
                return TextureListener.rail_wood;
            } else{
                return TextureListener.rail_wood_on;
            }
        } else {
            if((meta & 8) == 0){
                return TextureListener.rail_obsidian;
            } else{
                return TextureListener.rail_obsidian_on;
            }
        }
    }

    @Override
    public int getTexture(int i) {
        if (wooden) {
            return TextureListener.rail_wood;
        } else {
            return TextureListener.rail_obsidian;
        }
    }

    @Override
    public int getTextureId(BlockView iblockaccess, int i, int j, int k, int meta) {
        if (wooden) {
            if((meta & 8) == 0){
                return TextureListener.rail_wood;
            } else{
                return TextureListener.rail_wood_on;
            }
        } else {
            if((meta & 8) == 0){
                return TextureListener.rail_obsidian;
            } else{
                return TextureListener.rail_obsidian_on;
            }
        }
    }

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {

    }
}
