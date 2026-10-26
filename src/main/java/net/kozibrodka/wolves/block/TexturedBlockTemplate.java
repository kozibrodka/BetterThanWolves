package net.kozibrodka.wolves.block;

import net.minecraft.block.material.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TexturedBlockTemplate extends TemplateBlock {

    int topTexture;
    int sideTexture;
    int bottomTexture;

    public TexturedBlockTemplate(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material);
        setTranslationKey(identifier.namespace, identifier.path);
        setHardness(hardness);
        setSoundGroup(blockSounds);
    }

    @Override
    public TexturedBlockTemplate setHardness(float Hardness) {
        return (TexturedBlockTemplate) super.setHardness(Hardness);
    }

    public void specifyTextures(int topTexture, int sideTexture, int bottomTexture) {
        this.topTexture = topTexture;
        this.sideTexture = sideTexture;
        this.bottomTexture = bottomTexture;
    }

    public void specifyTextures(int universalTexture) {
        topTexture = universalTexture;
        sideTexture = universalTexture;
        bottomTexture = universalTexture;
    }

    @Override
    public int getTexture(int i, int j) {
        if (i == 0) return bottomTexture;
        if (i == 1) return topTexture;
        return sideTexture;
    }
}
