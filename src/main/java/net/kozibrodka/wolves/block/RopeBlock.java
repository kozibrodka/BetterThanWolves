package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.WolvesMpUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithInventoryRenderer.class)
public class RopeBlock extends TemplateBlock implements BlockWithWorldRenderer, BlockWithInventoryRenderer {

    public RopeBlock(Identifier iid) {
        super(iid, Material.PISTON_BREAKABLE);
        setHardness(0.5F);
        setSoundGroup(DIRT_SOUND_GROUP);
        descensionSpeed = -0.15F;
        ascensionSpeed = 0.2F;
    }

    @Override
    public int getTexture(int iSide) {
        return TextureListener.rope;
    }

    @Override
    public int getDroppedItemId(int i, Random random) {
        return ItemListener.ropeItem.id;
    }

    @Override
    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        int iBlockAboveID = world.getBlockId(i, j + 1, k);
        if (iBlockAboveID != id && iBlockAboveID != BlockListener.anchor.id && iBlockAboveID != BlockListener.pulley.id) {
            dropStacks(world, i, j, k, world.getBlockMeta(i, j, k));
            world.setBlock(i, j, k, 0);
        }
    }

    @Override
    public void onBlockAction(World world, int x, int y, int z, int code, int pitch) {
        world.playSound((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, BlockListener.rope.soundGroup.getSound(), (BlockListener.rope.soundGroup.getVolume() + 1.0F) / 2.0F, BlockListener.rope.soundGroup.getPitch() * 0.8F);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public Box getCollisionShape(World world, int i, int j, int k) {
        return Box.createCached(((float) i + 0.5F) - 0.0625F, (float) j, ((float) k + 0.5F) - 0.0625F, (float) i + 0.5F + 0.0625F, (float) j + 1.0F, (float) k + 0.5F + 0.0625F);
    }

    @Override
    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k) {
        setBoundingBox(0.4375F, 0.0F, 0.4375F, 0.5625F, 1.0F, 0.5625F);
    }

    @Override
    public void onEntityCollision(World world, int i, int j, int k, Entity entity) {
        if (entity instanceof LivingEntity) {
            entity.fallDistance = 0.0F;
            if (entity.velocityY < (double) descensionSpeed) {
                entity.velocityY = descensionSpeed;
            }
            if (entity.horizontalCollision) {
                entity.velocityY = ascensionSpeed;
            }
        }
    }

    public void BreakRope(World world, int i, int j, int k) {
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.ropeItem.id, 0);
        world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, soundGroup.getSound(), (soundGroup.getVolume() + 1.0F) / 2.0F, soundGroup.getPitch() * 0.8F);
        if (WolvesMpUtils.isEnvServ()) {
            WolvesMpUtils.soundPacket(world, i, j, k, soundGroup.getSound(), (soundGroup.getVolume() + 1.0F) / 2.0F, soundGroup.getPitch() * 0.8F);
        }
        world.setBlock(i, j, k, 0);
    }

    public static final float fRopeWidth = 0.125F;
    public float descensionSpeed;
    public float ascensionSpeed;

    @Override
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z) {
        boolean flag = CustomBlockRendering.GetOverrideBlockTexture(tileRenderer) >= 0;
        int l = this.getTexture(0);
        if (!flag) {
            CustomBlockRendering.SetOverrideBlockTexture(tileRenderer, l);
        }
        float f = 0.0625F;
        float f1 = 0.0625F;
        float f2 = 1.0F;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        tileRenderer.renderBlock(this, x, y, z);
        if (!flag) {
            CustomBlockRendering.SetOverrideBlockTexture(tileRenderer, -1);
        }
        return true;
    }

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {
        float f = 0.0625F;
        float f1 = 0.0625F;
        float f2 = 1.0F;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.rope);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
