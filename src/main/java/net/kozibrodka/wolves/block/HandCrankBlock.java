package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithInventoryRenderer.class)
public class HandCrankBlock extends TemplateBlock
        implements MechanicalDevice, BlockWithWorldRenderer, BlockWithInventoryRenderer {

    public HandCrankBlock(Identifier iid) {
        super(iid, Material.PISTON_BREAKABLE);
        setHardness(0.5F);
        setSoundGroup(WOOD_SOUND_GROUP);
        setTickRandomly(true);
    }

    public int getTickRate() {
        return handCrankTickRate;
    }

    public int getTexture(int iSide) {
        if (iSide == 1) {
            return TextureListener.handcrack_top;
        }
        if (iSide == 0) {
            return TextureListener.handcrack_bottom;
        } else {
            return TextureListener.handcrack_side;
        }
    }

    public Box getCollisionShape(World world, int i, int j, int k) {
        return Box.createCached((float) i, (float) j, (float) k, (float) i + 1.0F, (float) j + handCrankBaseHeight, (float) k + 1.0F);
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k) {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean canPlaceAt(World world, int i, int j, int k) {
        return world.method_1783(i, j - 1, k);
    }

    public void onBlockBreakStart(World world, int i, int j, int k, PlayerEntity entityplayer) {
        onUse(world, i, j, k, entityplayer);
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityPlayer) {
        if (world == null) {
            return true;
        }
        int iMetaData = world.getBlockMeta(i, j, k);
        if (iMetaData == 0) {
            if (!CheckForOverpower(world, i, j, k)) {
                world.setBlockMeta(i, j, k, 1);
                world.setBlocksDirty(i, j, k, i, j, k);
                world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 1.0F, 2.0F);
                world.notifyNeighbors(i, j, k, BlockListener.handCrank.id);
                world.scheduleBlockUpdate(i, j, k, BlockListener.handCrank.id, getTickRate());
            } else {
                BreakCrankWithDrop(world, i, j, k);
            }
        }
        return true;
    }

    public void onTick(World world, int i, int j, int k, Random random) {
        int iMetaData = world.getBlockMeta(i, j, k);
        if (iMetaData > 0) {
            if (iMetaData < 7) {
                if (iMetaData <= 6) {
                    world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 1.0F, 2.0F);
                    if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, i, j, k, 1.0F, 2.0F);
                    }
                }
                if (iMetaData <= 5) {
                    world.scheduleBlockUpdate(i, j, k, BlockListener.handCrank.id, getTickRate() + iMetaData);
                } else {
                    world.scheduleBlockUpdate(i, j, k, BlockListener.handCrank.id, handCrankDelayBeforeReset);
                }
                world.setBlockMeta(i, j, k, iMetaData + 1);
                world.blockUpdateEvent(i, j, k);
                world.setBlocksDirty(i, j, k, i, j, k);
            } else {
                world.setBlockMeta(i, j, k, 0);
                world.blockUpdateEvent(i, j, k);
                world.setBlocksDirty(i, j, k, i, j, k);
                world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.7F);
                if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, i, j, k, 0.3F, 0.7F);
                }
            }
        }
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, int x, int y, int z, float g, float h) {

//        List list1 = world.getEntities(ServerPlayer.class, Box.createButWasteMemory(x - 4D, y - 4D, z - 4D, x + 4D, y + 4D, z + 4D));
//        if(list1.size() != 0) {
//            ServerPlayer player1 = (ServerPlayer) list1.get(0);
//            PacketHelper.sendTo(player1, new SoundPacket("random.click"));
//            System.out.println("WYSYLAM PAKIET");
//        }

        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k = 0; k < list2.size(); k++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket("random.click", x, y, z, g, h));
            }
        }
    }

//    public void randomDisplayTick(Level world, int i, int j, int k, Random random)
//    {
//        world.setBlocksDirty(i, j, k, i, j, k);
//    }

    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        if (!world.method_1783(i, j - 1, k)) {
            dropStacks(world, i, j, k, world.getBlockMeta(i, j, k));
            world.setBlock(i, j, k, 0);
        }
    }

    public boolean CanOutputMechanicalPower() {
        return true;
    }

    public boolean CanInputMechanicalPower() {
        return false;
    }

    public boolean IsInputtingMechanicalPower(World world, int i, int j, int l) {
        return false;
    }

    public boolean IsOutputtingMechanicalPower(World world, int i, int j, int k) {
        return world.getBlockMeta(i, j, k) > 0;
    }

    public boolean CheckForOverpower(World world, int i, int j, int k) {
        int iNumPotentialDevicesToPower = 0;
        for (int iTempFacing = 2; iTempFacing <= 5; iTempFacing++) {
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.AddFacingAsOffset(iTempFacing);
            int iTempid = world.getBlockId(tempPos.i, tempPos.j, tempPos.k);
            Block tempBlock = Block.BLOCKS[iTempid];
            if (tempBlock == null || !(tempBlock instanceof MechanicalDevice tempDevice)) {
                continue;
            }
            if (tempDevice.CanInputMechanicalPower()) {
                iNumPotentialDevicesToPower++;
            }
        }

        return iNumPotentialDevicesToPower > 1;
    }

    public void BreakCrankWithDrop(World world, int i, int j, int k) {
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Item.STICK.id, 0);
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Block.COBBLESTONE.asItem().id, 0);
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Block.COBBLESTONE.asItem().id, 0);
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.gear.id, 0);
        world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 0.2F, 1.25F);
        world.setBlock(i, j, k, 0);
    }

    @Override
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z) {
        Tessellator tessellator = Tessellator.INSTANCE;
        float f = 0.5F;
        float f1 = 0.5F;
        float f2 = handCrankBaseHeight;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        tileRenderer.renderBlock(this, x, y, z);
        float f3 = this.getLuminance(tileView, x, y, z);
        if (Block.BLOCKS_LIGHT_LUMINANCE[this.id] > 0) {
            f3 = 1.0F;
        }
        tessellator.color(f3, f3, f3);
        int leverTexture = TextureListener.handcrack_lever;
        Atlas.Sprite testTex = Atlases.getTerrain().getTexture(leverTexture);
        float f4 = (float) (testTex.getStartU());
        float f5 = (float) (testTex.getEndU());
        float f6 = (float) (testTex.getStartV());
        float f7 = (float) (testTex.getEndV());

        Vec3d[] avec3d = new Vec3d[8];
        float f8 = 0.0625F;
        float f9 = 0.0625F;
        float f10 = 0.9F;
        avec3d[0] = Vec3d.createCached(-f8, 0.0D, -f9);
        avec3d[1] = Vec3d.createCached(f8, 0.0D, -f9);
        avec3d[2] = Vec3d.createCached(f8, 0.0D, f9);
        avec3d[3] = Vec3d.createCached(-f8, 0.0D, f9);
        avec3d[4] = Vec3d.createCached(-f8, f10, -f9);
        avec3d[5] = Vec3d.createCached(f8, f10, -f9);
        avec3d[6] = Vec3d.createCached(f8, f10, f9);
        avec3d[7] = Vec3d.createCached(-f8, f10, f9);
        boolean flag = tileView.getBlockMeta(x, y, z) > 0;
        for (int k1 = 0; k1 < 8; k1++) {
            if (flag) {
                avec3d[k1].z -= 0.0625D;
                avec3d[k1].rotateX(0.35F);
            } else {
                avec3d[k1].z += 0.0625D;
                avec3d[k1].rotateX(-0.35F);
            }
            avec3d[k1].rotateY(1.570796F);
            avec3d[k1].x += (double) x + 0.5D;
            avec3d[k1].y += (float) y + 0.125F;
            avec3d[k1].z += (double) z + 0.5D;
        }

        Vec3d vec3d = null;
        Vec3d vec3d1 = null;
        Vec3d vec3d2 = null;
        Vec3d vec3d3 = null;
        for (int l1 = 0; l1 < 6; l1++) {
            if (l1 == 0) {
                f4 = (float) (testTex.getStartU() + (7 / 512F));
                f5 = (float) (testTex.getEndU() - (7 / 512F));
                f6 = (float) (testTex.getStartV());
                f7 = (float) (testTex.getEndV() - (14 / 512F));

            } else if (l1 == 2) {
                f4 = (float) (testTex.getStartU() + (7 / 512F));
                f5 = (float) (testTex.getEndU() - (7 / 512F));
                f6 = (float) (testTex.getStartV());
                f7 = (float) (testTex.getEndV());
            }
            if (l1 == 0) {
                vec3d = avec3d[0];
                vec3d1 = avec3d[1];
                vec3d2 = avec3d[2];
                vec3d3 = avec3d[3];
            } else if (l1 == 1) {
                vec3d = avec3d[7];
                vec3d1 = avec3d[6];
                vec3d2 = avec3d[5];
                vec3d3 = avec3d[4];
            } else if (l1 == 2) {
                vec3d = avec3d[1];
                vec3d1 = avec3d[0];
                vec3d2 = avec3d[4];
                vec3d3 = avec3d[5];
            } else if (l1 == 3) {
                vec3d = avec3d[2];
                vec3d1 = avec3d[1];
                vec3d2 = avec3d[5];
                vec3d3 = avec3d[6];
            } else if (l1 == 4) {
                vec3d = avec3d[3];
                vec3d1 = avec3d[2];
                vec3d2 = avec3d[6];
                vec3d3 = avec3d[7];
            } else if (l1 == 5) {
                vec3d = avec3d[0];
                vec3d1 = avec3d[3];
                vec3d2 = avec3d[7];
                vec3d3 = avec3d[4];
            }
            tessellator.vertex(vec3d.x, vec3d.y, vec3d.z, f4, f7);
            tessellator.vertex(vec3d1.x, vec3d1.y, vec3d1.z, f5, f7);
            tessellator.vertex(vec3d2.x, vec3d2.y, vec3d2.z, f5, f6);
            tessellator.vertex(vec3d3.x, vec3d3.y, vec3d3.z, f4, f6);

        }

        return true;
    }

    private static final int handCrankTickRate = 3;
    private static final int handCrankDelayBeforeReset = 15;
    public static float handCrankBaseHeight = 0.25F;

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {
        float f = 0.5F;
        float f1 = 0.5F;
        float f2 = handCrankBaseHeight;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        f = 0.0625F;
        f1 = 0.0625F;
        f2 = 1.0F;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.handcrack_lever);
    }
}
