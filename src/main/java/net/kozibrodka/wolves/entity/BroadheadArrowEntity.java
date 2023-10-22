package net.kozibrodka.wolves.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3f;

import java.util.List;

public class BroadheadArrowEntity extends EntityBase{

    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private int inTile = 0;
    private int inData = 0;
    private boolean inGround = false;
    public boolean spawnedByPlayer = false;
    public int shake = 0;
    public Living owner;
    private int ticksInGround;
    private int ticksFlying = 0;

    public BroadheadArrowEntity(Level arg) {
        super(arg);
        this.setSize(0.5F, 0.5F);
    }

    public BroadheadArrowEntity(Level arg, double d, double e, double f) {
        super(arg);
        this.setSize(0.5F, 0.5F);
        this.setPosition(d, e, f);
        this.standingEyeHeight = 0.0F;
    }

    public BroadheadArrowEntity(Level arg, Living arg2) {
        super(arg);
        this.owner = arg2;
        this.spawnedByPlayer = arg2 instanceof PlayerBase;
        this.setSize(0.5F, 0.5F);
        this.setPositionAndAngles(arg2.x, arg2.y + (double)arg2.getStandingEyeHeight(), arg2.z, arg2.yaw, arg2.pitch);
        this.x -= (double)(MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.y -= 0.10000000149011612D;
        this.z -= (double)(MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.setPosition(this.x, this.y, this.z);
        this.standingEyeHeight = 0.0F;
        this.velocityX = (double)(-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F));
        this.velocityZ = (double)(MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F));
        this.velocityY = (double)(-MathHelper.sin(this.pitch / 180.0F * 3.1415927F));
        this.method_1291(this.velocityX, this.velocityY, this.velocityZ, 1.5F, 1.0F);
    }

    protected void initDataTracker() {
    }

    public void method_1291(double d, double e, double f, float g, float h) {
        float var9 = MathHelper.sqrt(d * d + e * e + f * f);
        d /= (double)var9;
        e /= (double)var9;
        f /= (double)var9;
        d += this.rand.nextGaussian() * 0.007499999832361937D * (double)h;
        e += this.rand.nextGaussian() * 0.007499999832361937D * (double)h;
        f += this.rand.nextGaussian() * 0.007499999832361937D * (double)h;
        d *= (double)g;
        e *= (double)g;
        f *= (double)g;
        this.velocityX = d;
        this.velocityY = e;
        this.velocityZ = f;
        float var10 = MathHelper.sqrt(d * d + f * f);
        this.prevYaw = this.yaw = (float)(Math.atan2(d, f) * 180.0D / 3.1415927410125732D);
        this.prevPitch = this.pitch = (float)(Math.atan2(e, (double)var10) * 180.0D / 3.1415927410125732D);
        this.ticksInGround = 0;
    }

    @Environment(EnvType.CLIENT)
    public void setVelocity(double d, double e, double f) {
        this.velocityX = d;
        this.velocityY = e;
        this.velocityZ = f;
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            float var7 = MathHelper.sqrt(d * d + f * f);
            this.prevYaw = this.yaw = (float)(Math.atan2(d, f) * 180.0D / 3.1415927410125732D);
            this.prevPitch = this.pitch = (float)(Math.atan2(e, (double)var7) * 180.0D / 3.1415927410125732D);
            this.prevPitch = this.pitch;
            this.prevYaw = this.yaw;
            this.setPositionAndAngles(this.x, this.y, this.z, this.yaw, this.pitch);
            this.ticksInGround = 0;
        }

    }

    public void tick() {
        super.tick();
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            float var1 = MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
            this.prevYaw = this.yaw = (float)(Math.atan2(this.velocityX, this.velocityZ) * 180.0D / 3.1415927410125732D);
            this.prevPitch = this.pitch = (float)(Math.atan2(this.velocityY, (double)var1) * 180.0D / 3.1415927410125732D);
        }

        int var15 = this.level.getTileId(this.xTile, this.yTile, this.zTile);
        if (var15 > 0) {
            BlockBase.BY_ID[var15].updateBoundingBox(this.level, this.xTile, this.yTile, this.zTile);
            Box var2 = BlockBase.BY_ID[var15].getCollisionShape(this.level, this.xTile, this.yTile, this.zTile);
            if (var2 != null && var2.method_88(Vec3f.from(this.x, this.y, this.z))) {
                this.inGround = true;
            }
        }

        if (this.shake > 0) {
            --this.shake;
        }

        if (this.inGround) {
            var15 = this.level.getTileId(this.xTile, this.yTile, this.zTile);
            int var18 = this.level.getTileMeta(this.xTile, this.yTile, this.zTile);
            if (var15 == this.inTile && var18 == this.inData) {
                ++this.ticksInGround;
                if (this.ticksInGround == 1200) {
                    this.remove();
                }

            } else {
                this.inGround = false;
                this.velocityX *= (double)(this.rand.nextFloat() * 0.2F);
                this.velocityY *= (double)(this.rand.nextFloat() * 0.2F);
                this.velocityZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksFlying = 0;
            }
        } else {
            ++this.ticksFlying;
            Vec3f var16 = Vec3f.from(this.x, this.y, this.z);
            Vec3f var17 = Vec3f.from(this.x + this.velocityX, this.y + this.velocityY, this.z + this.velocityZ);
            HitResult var3 = this.level.method_162(var16, var17, false, true);
            var16 = Vec3f.from(this.x, this.y, this.z);
            var17 = Vec3f.from(this.x + this.velocityX, this.y + this.velocityY, this.z + this.velocityZ);
            if (var3 != null) {
                var17 = Vec3f.from(var3.field_1988.x, var3.field_1988.y, var3.field_1988.z);
            }

            EntityBase var4 = null;
            List var5 = this.level.getEntities(this, this.boundingBox.method_86(this.velocityX, this.velocityY, this.velocityZ).expand(1.0D, 1.0D, 1.0D));
            double var6 = 0.0D;

            float var10;
            for(int var8 = 0; var8 < var5.size(); ++var8) {
                EntityBase var9 = (EntityBase)var5.get(var8);
                if (var9.method_1356() && (var9 != this.owner || this.ticksFlying >= 5)) {
                    var10 = 0.3F;
                    Box var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
                    HitResult var12 = var11.method_89(var16, var17);
                    if (var12 != null) {
                        double var13 = var16.method_1294(var12.field_1988);
                        if (var13 < var6 || var6 == 0.0D) {
                            var4 = var9;
                            var6 = var13;
                        }
                    }
                }
            }

            if (var4 != null) {
                var3 = new HitResult(var4);
            }

            float var19;
            if (var3 != null) {
                if (var3.field_1989 != null) {
                    if (var3.field_1989.damage(this.owner, 10)) {
                        this.level.playSound(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                        this.remove();
                    } else {
                        this.velocityX *= -0.10000000149011612D;
                        this.velocityY *= -0.10000000149011612D;
                        this.velocityZ *= -0.10000000149011612D;
                        this.yaw += 180.0F;
                        this.prevYaw += 180.0F;
                        this.ticksFlying = 0;
                    }
                } else {
                    this.xTile = var3.x;
                    this.yTile = var3.y;
                    this.zTile = var3.z;
                    this.inTile = this.level.getTileId(this.xTile, this.yTile, this.zTile);
                    this.inData = this.level.getTileMeta(this.xTile, this.yTile, this.zTile);
                    this.velocityX = (double)((float)(var3.field_1988.x - this.x));
                    this.velocityY = (double)((float)(var3.field_1988.y - this.y));
                    this.velocityZ = (double)((float)(var3.field_1988.z - this.z));
                    var19 = MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityY * this.velocityY + this.velocityZ * this.velocityZ);
                    this.x -= this.velocityX / (double)var19 * 0.05000000074505806D;
                    this.y -= this.velocityY / (double)var19 * 0.05000000074505806D;
                    this.z -= this.velocityZ / (double)var19 * 0.05000000074505806D;
                    this.level.playSound(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.shake = 7;
                }
            }

            this.x += this.velocityX;
            this.y += this.velocityY;
            this.z += this.velocityZ;
            var19 = MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
            this.yaw = (float)(Math.atan2(this.velocityX, this.velocityZ) * 180.0D / 3.1415927410125732D);

            for(this.pitch = (float)(Math.atan2(this.velocityY, (double)var19) * 180.0D / 3.1415927410125732D); this.pitch - this.prevPitch < -180.0F; this.prevPitch -= 360.0F) {
            }

            while(this.pitch - this.prevPitch >= 180.0F) {
                this.prevPitch += 360.0F;
            }

            while(this.yaw - this.prevYaw < -180.0F) {
                this.prevYaw -= 360.0F;
            }

            while(this.yaw - this.prevYaw >= 180.0F) {
                this.prevYaw += 360.0F;
            }

            this.pitch = this.prevPitch + (this.pitch - this.prevPitch) * 0.2F;
            this.yaw = this.prevYaw + (this.yaw - this.prevYaw) * 0.2F;
            float var20 = 0.99F;
            var10 = 0.03F;
            if (this.method_1334()) {
                for(int var21 = 0; var21 < 4; ++var21) {
                    float var22 = 0.25F;
                    this.level.addParticle("bubble", this.x - this.velocityX * (double)var22, this.y - this.velocityY * (double)var22, this.z - this.velocityZ * (double)var22, this.velocityX, this.velocityY, this.velocityZ);
                }

                var20 = 0.8F;
            }

            this.velocityX *= (double)var20;
            this.velocityY *= (double)var20;
            this.velocityZ *= (double)var20;
            this.velocityY -= (double)var10;
            this.setPosition(this.x, this.y, this.z);
        }
    }

    public void writeCustomDataToTag(CompoundTag arg) {
        arg.put("xTile", (short)this.xTile);
        arg.put("yTile", (short)this.yTile);
        arg.put("zTile", (short)this.zTile);
        arg.put("inTile", (byte)this.inTile);
        arg.put("inData", (byte)this.inData);
        arg.put("shake", (byte)this.shake);
        arg.put("inGround", (byte)(this.inGround ? 1 : 0));
        arg.put("player", this.spawnedByPlayer);
    }

    public void readCustomDataFromTag(CompoundTag arg) {
        this.xTile = arg.getShort("xTile");
        this.yTile = arg.getShort("yTile");
        this.zTile = arg.getShort("zTile");
        this.inTile = arg.getByte("inTile") & 255;
        this.inData = arg.getByte("inData") & 255;
        this.shake = arg.getByte("shake") & 255;
        this.inGround = arg.getByte("inGround") == 1;
        this.spawnedByPlayer = arg.getBoolean("player");
    }

    public void onPlayerCollision(PlayerBase arg) {
        if (!this.level.isServerSide) {
            if (this.inGround && this.spawnedByPlayer && this.shake <= 0 && arg.inventory.addStack(new ItemInstance(ItemListener.broadHeadArrow, 1))) {
                this.level.playSound(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                arg.onItemPickup(this, 1);
                this.remove();
            }

        }
    }

    @Environment(EnvType.CLIENT)
    public float getEyeHeight() {
        return 0.0F;
    }
}
