package net.kozibrodka.wolves.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.events.EntityListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class BroadheadArrowEntity extends Entity implements EntitySpawnDataProvider {

    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private int inTile = 0;
    private int inData = 0;
    private boolean inGround = false;
    public boolean spawnedByPlayer = false;
    public int shake = 0;
    public LivingEntity owner;
    private int ticksInGround;
    private int ticksFlying = 0;

    public BroadheadArrowEntity(World arg) {
        super(arg);
        this.setBoundingBoxSpacing(0.5F, 0.5F);
    }

    public BroadheadArrowEntity(World arg, double d, double e, double f) {
        super(arg);
        this.setBoundingBoxSpacing(0.5F, 0.5F);
        this.setPos(d, e, f);
        this.standingEyeHeight = 0.0F;
    }

    public BroadheadArrowEntity(World arg, LivingEntity arg2) {
        super(arg);
        this.owner = arg2;
        this.spawnedByPlayer = arg2 instanceof PlayerEntity;
        this.setBoundingBoxSpacing(0.5F, 0.5F);
        this.setPositionAndAnglesKeepPrevAngles(arg2.x, arg2.y + (double)arg2.getEyeHeight(), arg2.z, arg2.yaw, arg2.pitch);
        this.x -= (double)(MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.y -= 0.10000000149011612D;
        this.z -= (double)(MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.setPos(this.x, this.y, this.z);
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
        d += this.random.nextGaussian() * 0.007499999832361937D * (double)h;
        e += this.random.nextGaussian() * 0.007499999832361937D * (double)h;
        f += this.random.nextGaussian() * 0.007499999832361937D * (double)h;
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
    public void setVelocityClient(double d, double e, double f) {
        this.velocityX = d;
        this.velocityY = e;
        this.velocityZ = f;
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            float var7 = MathHelper.sqrt(d * d + f * f);
            this.prevYaw = this.yaw = (float)(Math.atan2(d, f) * 180.0D / 3.1415927410125732D);
            this.prevPitch = this.pitch = (float)(Math.atan2(e, (double)var7) * 180.0D / 3.1415927410125732D);
            this.prevPitch = this.pitch;
            this.prevYaw = this.yaw;
            this.setPositionAndAnglesKeepPrevAngles(this.x, this.y, this.z, this.yaw, this.pitch);
            this.ticksInGround = 0;
        }

    }

    public void tick() {
        super.tick();
        //ADDED
        if(dead || world.isRemote)
        {
            return;
        }
        //ADDED
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            float var1 = MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
            this.prevYaw = this.yaw = (float)(Math.atan2(this.velocityX, this.velocityZ) * 180.0D / 3.1415927410125732D);
            this.prevPitch = this.pitch = (float)(Math.atan2(this.velocityY, (double)var1) * 180.0D / 3.1415927410125732D);
        }

        int var15 = this.world.getBlockId(this.xTile, this.yTile, this.zTile);
        if (var15 > 0) {
            Block.BLOCKS[var15].updateBoundingBox(this.world, this.xTile, this.yTile, this.zTile);
            Box var2 = Block.BLOCKS[var15].getCollisionShape(this.world, this.xTile, this.yTile, this.zTile);
            if (var2 != null && var2.contains(Vec3d.createCached(this.x, this.y, this.z))) {
                this.inGround = true;
            }
        }

        if (this.shake > 0) {
            --this.shake;
        }

        if (this.inGround) {
            var15 = this.world.getBlockId(this.xTile, this.yTile, this.zTile);
            int var18 = this.world.getBlockMeta(this.xTile, this.yTile, this.zTile);
            if (var15 == this.inTile && var18 == this.inData) {
                ++this.ticksInGround;
                if (this.ticksInGround == 1200) {
                    this.markDead();
                }

            } else {
                this.inGround = false;
                this.velocityX *= (double)(this.random.nextFloat() * 0.2F);
                this.velocityY *= (double)(this.random.nextFloat() * 0.2F);
                this.velocityZ *= (double)(this.random.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksFlying = 0;
            }
        } else {
            ++this.ticksFlying;
            Vec3d var16 = Vec3d.createCached(this.x, this.y, this.z);
            Vec3d var17 = Vec3d.createCached(this.x + this.velocityX, this.y + this.velocityY, this.z + this.velocityZ);
            HitResult var3 = this.world.raycast(var16, var17, false, true);
            var16 = Vec3d.createCached(this.x, this.y, this.z);
            var17 = Vec3d.createCached(this.x + this.velocityX, this.y + this.velocityY, this.z + this.velocityZ);
            if (var3 != null) {
                var17 = Vec3d.createCached(var3.pos.x, var3.pos.y, var3.pos.z);
            }

            Entity var4 = null;
            List var5 = this.world.getEntities(this, this.boundingBox.stretch(this.velocityX, this.velocityY, this.velocityZ).expand(1.0D, 1.0D, 1.0D));
            double var6 = 0.0D;

            float var10;
            for(int var8 = 0; var8 < var5.size(); ++var8) {
                Entity var9 = (Entity)var5.get(var8);
                if (var9.isCollidable() && (var9 != this.owner || this.ticksFlying >= 5)) {
                    var10 = 0.3F;
                    Box var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
                    HitResult var12 = var11.raycast(var16, var17);
                    if (var12 != null) {
                        double var13 = var16.distanceTo(var12.pos);
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
                if (var3.entity != null) {
                    if (var3.entity.damage(this.owner, 10)) {
                        this.world.playSound(this, "random.drr", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                        this.markDead();
                    } else {
                        this.velocityX *= -0.10000000149011612D;
                        this.velocityY *= -0.10000000149011612D;
                        this.velocityZ *= -0.10000000149011612D;
                        this.yaw += 180.0F;
                        this.prevYaw += 180.0F;
                        this.ticksFlying = 0;
                    }
                } else {
                    this.xTile = var3.blockX;
                    this.yTile = var3.blockY;
                    this.zTile = var3.blockZ;
                    this.inTile = this.world.getBlockId(this.xTile, this.yTile, this.zTile);
                    this.inData = this.world.getBlockMeta(this.xTile, this.yTile, this.zTile);
                    this.velocityX = (double)((float)(var3.pos.x - this.x));
                    this.velocityY = (double)((float)(var3.pos.y - this.y));
                    this.velocityZ = (double)((float)(var3.pos.z - this.z));
                    var19 = MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityY * this.velocityY + this.velocityZ * this.velocityZ);
                    this.x -= this.velocityX / (double)var19 * 0.05000000074505806D;
                    this.y -= this.velocityY / (double)var19 * 0.05000000074505806D;
                    this.z -= this.velocityZ / (double)var19 * 0.05000000074505806D;
                    this.world.playSound(this, "random.drr", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
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
            if (this.isSubmergedInWater()) {
                for(int var21 = 0; var21 < 4; ++var21) {
                    float var22 = 0.25F;
                    this.world.addParticle("bubble", this.x - this.velocityX * (double)var22, this.y - this.velocityY * (double)var22, this.z - this.velocityZ * (double)var22, this.velocityX, this.velocityY, this.velocityZ);
                }

                var20 = 0.8F;
            }

            this.velocityX *= (double)var20;
            this.velocityY *= (double)var20;
            this.velocityZ *= (double)var20;
            this.velocityY -= (double)var10;
            this.setPos(this.x, this.y, this.z);
        }
    }

    public void writeNbt(NbtCompound arg) {
        arg.putShort("xTile", (short)this.xTile);
        arg.putShort("yTile", (short)this.yTile);
        arg.putShort("zTile", (short)this.zTile);
        arg.putByte("inTile", (byte)this.inTile);
        arg.putByte("inData", (byte)this.inData);
        arg.putByte("shake", (byte)this.shake);
        arg.putByte("inGround", (byte)(this.inGround ? 1 : 0));
        arg.putBoolean("player", this.spawnedByPlayer);
    }

    public void readNbt(NbtCompound arg) {
        this.xTile = arg.getShort("xTile");
        this.yTile = arg.getShort("yTile");
        this.zTile = arg.getShort("zTile");
        this.inTile = arg.getByte("inTile") & 255;
        this.inData = arg.getByte("inData") & 255;
        this.shake = arg.getByte("shake") & 255;
        this.inGround = arg.getByte("inGround") == 1;
        this.spawnedByPlayer = arg.getBoolean("player");
    }

    public void onPlayerInteraction(PlayerEntity arg) {
        if (!this.world.isRemote) {
            if (this.inGround && this.spawnedByPlayer && this.shake <= 0 && arg.inventory.method_671(new ItemStack(ItemListener.broadHeadArrow, 1))) {
                this.world.playSound(this, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                arg.method_491(this, 1);
                this.markDead();
            }

        }
    }

    @Environment(EnvType.CLIENT)
    public float getShadowRadius() {
        return 0.0F;
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.NAMESPACE, "BroadheadArrow");
    }
}
