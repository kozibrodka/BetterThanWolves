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
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
public class RopeBlock extends TemplateBlock implements BlockWithWorldRenderer {

    public RopeBlock(Identifier iid) {
        super(iid, Material.PISTON_BREAKABLE);
        setHardness(0.5F);
        setSoundGroup(DIRT_SOUND_GROUP);
        descensionSpeed = -0.15F;
        ascensionSpeed = 0.2F;
    }

    public int getTexture(int iSide) {
        return TextureListener.rope;
    }

    public int getDroppedItemId(int i, Random random) {
        return ItemListener.ropeItem.id;
    }

    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        int iBlockAboveID = world.getBlockId(i, j + 1, k);
        if (iBlockAboveID != id && iBlockAboveID != BlockListener.anchor.id && iBlockAboveID != BlockListener.pulley.id) {
            dropStacks(world, i, j, k, world.getBlockMeta(i, j, k));
            world.setBlock(i, j, k, 0);
        }
    }

    public boolean canPlaceAt(World world, int i, int j, int k) {
        int iBlockAboveID = world.getBlockId(i, j + 1, k);
        return iBlockAboveID == id || iBlockAboveID == BlockListener.anchor.id;
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public Box getCollisionShape(World world, int i, int j, int k) {
        return Box.createCached(((float) i + 0.5F) - 0.0625F, (float) j, ((float) k + 0.5F) - 0.0625F, (float) i + 0.5F + 0.0625F, (float) j + 1.0F, (float) k + 0.5F + 0.0625F);
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k) {
        setBoundingBox(0.4375F, 0.0F, 0.4375F, 0.5625F, 1.0F, 0.5625F);
    }

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
        world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, soundGroup.getSound(), (soundGroup.method_1976() + 1.0F) / 2.0F, soundGroup.method_1977() * 0.8F);
        if (net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, soundGroup.getSound(), i, j, k, (soundGroup.method_1976() + 1.0F) / 2.0F, soundGroup.method_1977() * 0.8F);
        }
        world.setBlock(i, j, k, 0);
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h) {
        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k = 0; k < list2.size(); k++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g, h));
            }
        }
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
}
