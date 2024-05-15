package net.kozibrodka.wolves.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class FallingAnvilEntity extends Entity implements EntitySpawnDataProvider {
    public int tile;
    public int facing;
    public int field_848 = 0;

    public FallingAnvilEntity(World arg) {
        super(arg);
    }

    public FallingAnvilEntity(World arg, double d, double e, double f, int i, int face) {
        super(arg);
        this.tile = i;
        this.facing = face;
        this.field_1593 = true;
        this.setBoundingBoxSpacing(0.98F, 0.98F);
        this.eyeHeight = this.spacingY / 2.0F;
        this.method_1340(d, e, f);
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


    protected boolean bypassesSteppingEffects() {
        return false;
    }

    protected void initDataTracker() {
    }

    public boolean method_1356() {
        return !this.dead;
    }

    public void tick() {
        if (this.tile == 0) {
            this.markDead();
        } else {
            this.prevX = this.x;
            this.prevY = this.y;
            this.prevZ = this.z;
            ++this.field_848;
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

            if (this.field_1623) {
                this.velocityX *= 0.699999988079071D;
                this.velocityZ *= 0.699999988079071D;
                this.velocityY *= -0.5D;
                List list1 = world.method_175(LivingEntity.class, this.boundingBox);
                if(!list1.isEmpty()) {
                    for (int k = 0; k < list1.size(); k++) {
                        LivingEntity playertohit = (LivingEntity) list1.get(k);
                        playertohit.damage(this, 10); //TODO DMG
                    }
                }
                world.playSound(this.x, this.y, this.z, "wolves:anvil_land", 1.0F, 1.2F);
                this.markDead();
                if (!this.world.isRemote) {
                    this.world.method_154(var1, var2, var3, this.tile, this.facing);
                }
            } else if (this.field_848 > 100 && !this.world.isRemote) { //TODO: destroy after 100 ticks??
                this.method_1339(this.tile, 1);
                List list1 = world.method_175(LivingEntity.class, this.boundingBox);
                if(!list1.isEmpty()) {
                    for (int k = 0; k < list1.size(); k++) {
                        LivingEntity playertohit = (LivingEntity) list1.get(k);
                        playertohit.damage(this, 10); //TODO DMG
                    }
                }
                world.playSound(this.x, this.y, this.z, "wolves:anvil_land", 1.0F, 1.2F);
                this.markDead();
            }
            world.method_243(var1, var2, var3);
        }
    }

    protected void writeNbt(NbtCompound arg) {
        arg.putByte("Tile", (byte)this.tile);
        arg.putByte("Facing", (byte)this.facing);
    }

    protected void readNbt(NbtCompound arg) {
        this.tile = arg.getByte("Tile") & 255;
        this.facing = arg.getByte("Facing") & 255;
    }

    @Environment(EnvType.CLIENT)
    public float method_1366() {
        return 0.0F;
    }

    @Environment(EnvType.CLIENT)
    public World getFallingLevel() {
        return this.world;
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(ConfigListener.MOD_ID, "FallingAnvil");
    }

}
