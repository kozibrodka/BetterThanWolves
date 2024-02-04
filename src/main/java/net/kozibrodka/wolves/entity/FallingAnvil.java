package net.kozibrodka.wolves.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.block.Sand;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class FallingAnvil extends EntityBase implements EntitySpawnDataProvider {
    public int tile;
    public int facing;
    public int field_848 = 0;

    public FallingAnvil(Level arg) {
        super(arg);
    }

    public FallingAnvil(Level arg, double d, double e, double f, int i, int face) {
        super(arg);
        this.tile = i;
        this.facing = face;
        this.field_1593 = true;
        this.setSize(0.98F, 0.98F);
        this.standingEyeHeight = this.height / 2.0F;
        this.setPosition(d, e, f);
        this.velocityX = 0.0D;
        this.velocityY = 0.0D;
        this.velocityZ = 0.0D;
        this.prevX = d;
        this.prevY = e;
        this.prevZ = f;
    }

    public FallingAnvil(Level level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }


    protected boolean canClimb() {
        return false;
    }

    protected void initDataTracker() {
    }

    public boolean method_1356() {
        return !this.removed;
    }

    public void tick() {
        if (this.tile == 0) {
            this.remove();
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
            if (this.level.getTileId(var1, var2, var3) == this.tile) {
                this.level.setTile(var1, var2, var3, 0);
            }

            if (this.onGround) {
                this.velocityX *= 0.699999988079071D;
                this.velocityZ *= 0.699999988079071D;
                this.velocityY *= -0.5D;
                this.remove(); //TODO DMG
                if (!this.level.isServerSide) {
                    this.level.setTileWithMetadata(var1, var2, var3, this.tile, this.facing);
                }
            } else if (this.field_848 > 100 && !this.level.isServerSide) { //TODO: destroy after 100 ticks??
                this.dropItem(this.tile, 1);
                this.remove();
            }
            level.method_243(var1, var2, var3);

        }
    }

    protected void writeCustomDataToTag(CompoundTag arg) {
        arg.put("Tile", (byte)this.tile);
        arg.put("Facing", (byte)this.facing);
    }

    protected void readCustomDataFromTag(CompoundTag arg) {
        this.tile = arg.getByte("Tile") & 255;
        this.facing = arg.getByte("Facing") & 255;
    }

    @Environment(EnvType.CLIENT)
    public float getEyeHeight() {
        return 0.0F;
    }

    @Environment(EnvType.CLIENT)
    public Level getFallingLevel() {
        return this.level;
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(ConfigListener.MOD_ID, "FallingAnvil");
    }

}
