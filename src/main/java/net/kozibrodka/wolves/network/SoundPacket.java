package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketHandler;
import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SoundPacket extends AbstractPacket implements IdentifiablePacket{

    //MIXIN "https://resourceproxy.pymcl.net/MinecraftResources/"

    //     "DestroyStupidResource404Errors",
    //    "MixinFixDepthBuffer",
    //    "MixinFixThreadCheckHasPaid"

    private String soundToPlay;

    public SoundPacket() {
    }

    public SoundPacket(String soundName) {
        this.soundToPlay = soundName;
    }

    @Override
    public void read(DataInputStream stream) {
        this.soundToPlay = stream.toString();
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeChars(this.soundToPlay);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(PacketHandler arg) {
        System.out.println("Packet Received = Sound : " + this.soundToPlay);

        switch (FabricLoader.INSTANCE.getEnvironmentType()){
            case CLIENT -> handleClient(arg);
            case SERVER -> handleServer(arg);
        }
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(PacketHandler networkHandler){
//        ClientPlayerAccessor accessor = (ClientPlayerAccessor) networkHandler;
//        Minecraft minecraft = accessor.getMinecraft();
//        ItemStack selectedItem = minecraft.player.inventory.getSelectedItem();
//        if (selectedItem.getItem() instanceof WrenchBase wrench) {
//            wrench.setWrenchMode(selectedItem, this.wrenchMode);
//        }

        Minecraft mc = (Minecraft) net.fabricmc.loader.api.FabricLoader.getInstance().getGameInstance();
        mc.level.playSound((double)mc.player.x + 0.5D, (double)mc.player.y + 0.5D, (double)mc.player.z + 0.5D, soundToPlay, 1.0F, 2.0F);
    }

    @Environment(EnvType.SERVER)
    public void handleServer(PacketHandler networkHandler){

    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    public Identifier getId() {
        return identifier;
    }


    private static final Identifier identifier = ItemListener.MOD_ID.id("btw_sound");

    public static void register() {
        IdentifiablePacket.register(identifier, true, true, SoundPacket::new);
    }

}
