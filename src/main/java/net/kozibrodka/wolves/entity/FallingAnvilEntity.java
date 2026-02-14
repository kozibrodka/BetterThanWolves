package net.kozibrodka.wolves.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ConfigListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.TriState;

import java.util.List;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 1, sendVelocity = TriState.FALSE)
public class FallingAnvilEntity extends Entity implements EntitySpawnDataProvider {
    public int tile;
//    public int facing;
    public int timeFalling = 0;
    public boolean receivedP;

    public FallingAnvilEntity(World arg) {
        super(arg);
    }

    public FallingAnvilEntity(World arg, double d, double e, double f, int i, int face) {
        super(arg);
        this.tile = i;
        setFacingMeta(face);
        this.blocksSameBlockSpawning = true;
        this.setBoundingBoxSpacing(0.98F, 0.98F);
        this.standingEyeHeight = this.height / 2.0F;
        this.setPosition(d, e, f);
        this.velocityX = 0.0D;
        this.velocityY = 0.0D;
        this.velocityZ = 0.0D;
        this.prevX = d;
        this.prevY = e;
        this.prevZ = f;
    }

    public FallingAnvilEntity(World level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setPositionAndAnglesAvoidEntities(double x, double y, double z, float pitch, float yaw, int interpolationSteps) {
        if(!receivedP) {
            this.setPosition(x, y, z);
            this.setRotation(pitch, yaw);
            tile = BlockListener.anvil.id;
            receivedP = true;
        }
    }

    protected boolean bypassesSteppingEffects() {
        return false;
    }

    protected void initDataTracker() {
        dataTracker.startTracking(16, (byte) 0); //metaData
    }

    public boolean isCollidable() {
        return !this.dead;
    }

    public void tick() {
        if (this.tile == 0) {
            this.markDead();
        } else {
            this.prevX = this.x;
            this.prevY = this.y;
            this.prevZ = this.z;
            ++this.timeFalling;
            this.velocityY -= 0.03999999910593033D;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= 0.9800000190734863D;
            this.velocityY *= 0.9800000190734863D;
            this.velocityZ *= 0.9800000190734863D;
            int var1 = MathHelper.floor(this.x);
            int var2 = MathHelper.floor(this.y);
            int var3 = MathHelper.floor(this.z);
            if (this.world.getBlockId(var1, var2, var3) == this.tile) {
                this.world.setBlock(var1, var2, var3, 0);
            }

            if (this.onGround) {
                this.velocityX *= 0.699999988079071D;
                this.velocityZ *= 0.699999988079071D;
                this.velocityY *= -0.5D;
                List list1 = world.collectEntitiesByClass(LivingEntity.class, this.boundingBox);
                if (!list1.isEmpty()) {
                    for (int k = 0; k < list1.size(); k++) {
                        LivingEntity playertohit = (LivingEntity) list1.get(k);
                        playertohit.damage(this, (timeFalling/2));
                    }
                }
                world.playSound(this.x, this.y, this.z, "wolves:anvil_land", 1.0F, 1.2F);
                if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "wolves:anvil_land", (int)this.x, (int)this.y, (int)this.z, 1.0F, 1.2F);
                }
                this.markDead();
                if (!this.world.isRemote) {
                    this.world.setBlockWithoutNotifyingNeighbors(var1, var2, var3, this.tile, getFacingMeta());
                }
            } else if (this.timeFalling > 200 && !this.world.isRemote) { //TODO: should destroy after 100 ticks like sand? + dmg ammount balance
                this.dropItem(this.tile, 1);
                List list1 = world.collectEntitiesByClass(LivingEntity.class, this.boundingBox);
                if (!list1.isEmpty()) {
                    for (int k = 0; k < list1.size(); k++) {
                        LivingEntity playertohit = (LivingEntity) list1.get(k);
                        playertohit.damage(this, (timeFalling/2));
                    }
                }
                world.playSound(this.x, this.y, this.z, "wolves:anvil_land", 1.0F, 1.2F);
                if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "wolves:anvil_land", (int)this.x, (int)this.y, (int)this.z, 1.0F, 1.2F);
                }
                this.markDead();
            }
            world.blockUpdateEvent(var1, var2, var3);
        }
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

    protected void writeNbt(NbtCompound arg) {
        arg.putByte("Tile", (byte) this.tile);
        arg.putInt("Facing", getFacingMeta());
    }

    protected void readNbt(NbtCompound arg) {
        this.tile = arg.getByte("Tile") & 255;
        setFacingMeta(arg.getInt("Facing"));
    }

    //MetaData
    public int getFacingMeta()
    {
        return dataTracker.getByte(16);
    }

    public void setFacingMeta(int i)
    {
        dataTracker.set(16, (byte) i);
    }

    @Environment(EnvType.CLIENT)
    public float getShadowRadius() {
        return 0.0F;
    }

    @Environment(EnvType.CLIENT)
    public World getFallingLevel() {
        return this.world;
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(ConfigListener.NAMESPACE, "FallingAnvil");
    }

    @Override
    public boolean syncTrackerAtSpawn() {
        return true;
    }

}
