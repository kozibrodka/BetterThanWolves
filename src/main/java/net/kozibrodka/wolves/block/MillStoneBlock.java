package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.block.entity.MillStoneBlockEntity;
import net.kozibrodka.wolves.container.MillStoneScreenHandler;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ScreenHandlerListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

public class MillStoneBlock extends TemplateBlockWithEntity
        implements MechanicalDevice {

    public MillStoneBlock(Identifier iid) {
        super(iid, Material.STONE);
        setHardness(3.5F);
        setSoundGroup(DEFAULT_SOUND_GROUP);
        setTickRandomly(true);
    }

    public int getTickRate() {
        return iMillStoneTickRate;
    }

    public int getTexture(int side) {
        if (side == 1) {
            return TextureListener.millstone_top;
        }
        if (side == 0) {
            return TextureListener.millstone_bottom;
        } else {
            return TextureListener.millstone_side;
        }
    }

    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, BlockListener.millStone.id, getTickRate());
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityplayer) {
        MillStoneBlockEntity tileEntityMillStone = (MillStoneBlockEntity) world.getBlockEntity(i, j, k);
        ScreenHandlerListener.TempGuiX = i;
        ScreenHandlerListener.TempGuiY = j;
        ScreenHandlerListener.TempGuiZ = k;
        if (world.isRemote) {
            PacketHelper.send(new ScreenPacket("mill", 0, i, j, k));
        }
        GuiHelper.openGUI(entityplayer, Identifier.of("wolves:openMillStone"), tileEntityMillStone, new MillStoneScreenHandler(entityplayer.inventory, tileEntityMillStone));
        return true;
    }

    protected BlockEntity createBlockEntity() {
        return new MillStoneBlockEntity();
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

    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        if (IsBlockOn(world, i, j, k)) {
            EmitMillingParticles(world, i, j, k, random);
            if (random.nextInt(5) == 0) {
                world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 0.1F + random.nextFloat() * 0.1F, 1.25F + random.nextFloat() * 0.1F);
            }
        }
    }

    public void onBreak(World world, int i, int j, int k) {
        InventoryHandler.ejectInventoryContents(world, i, j, k, (Inventory) world.getBlockEntity(i, j, k));
        super.onBreak(world, i, j, k);
    }

    public boolean IsBlockOn(World world, int i, int j, int k) {
        return world.getBlockMeta(i, j, k) > 0;
    }

    public void SetBlockOn(World world, int i, int j, int k, boolean bOn) {
        if (bOn) {
            world.setBlockMeta(i, j, k, 1);
            world.blockUpdateEvent(i, j, k);
        } else {
            world.setBlockMeta(i, j, k, 0);
            world.blockUpdateEvent(i, j, k);
        }
    }

    void EmitMillingParticles(World world, int i, int j, int k, Random random) {
        for (int counter = 0; counter < 5; counter++) {
            float smokeX = (float) i + random.nextFloat();
            float smokeY = (float) j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float) k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    public boolean canOutputMechanicalPower() {
        return false;
    }

    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public void powerMachine(World world, int x, int y, int z) {
        SetBlockOn(world, x, y, z, true);
    }

    @Override
    public void unpowerMachine(World world, int x, int y, int z) {
        SetBlockOn(world, x, y, z, false);
    }

    @Override
    public boolean isMachinePowered(World world, int x, int y, int z) {
        return IsBlockOn(world, x, y, z);
    }

    @Override
    public boolean canInputMechanicalPower(World world, int x, int y, int z, int side) {
        return side < 2;
    }

    private static final int iMillStoneTickRate = 10;
}
