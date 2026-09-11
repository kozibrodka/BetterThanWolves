package net.kozibrodka.wolves.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

public class CementBlockEntity extends BlockEntity {

    public CementBlockEntity() {
        spreadDist = 0;
        dryTime = 0;
    }

    @Override
    public void writeNbt(NbtCompound nbttagcompound) {
        super.writeNbt(nbttagcompound);
        nbttagcompound.putInt("dryTime", dryTime);
        nbttagcompound.putInt("spreadDist", spreadDist);
    }

    @Override
    public void readNbt(NbtCompound nbttagcompound) {
        super.readNbt(nbttagcompound);
        if (nbttagcompound.contains("dryTime")) {
            dryTime = nbttagcompound.getInt("dryTime");
        } else {
            dryTime = 12;
        }
        if (nbttagcompound.contains("spreadDist")) {
            spreadDist = nbttagcompound.getInt("spreadDist");
        } else {
            spreadDist = 16;
        }
    }

//    @Override
//    @Environment(EnvType.SERVER)
//    public Packet createUpdatePacket() { /// To się odpala automatycznie przy ładowaniu Entity na serwie na pewno
//        System.out.println("TWORZE UPDATE PACKET " + this.x + this.y + this.z);
//        return null;
//    }

    public int dryTime;
    public int spreadDist;
}
